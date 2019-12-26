package Machine.Application.Controllers;

import Machine.AccountManager.Email;
import Machine.AccountManager.HashPassword;

import Machine.Application.Controllers.Model.DatabaseModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;


public class SendCodeController implements Initializable
{
	@FXML
	private StackPane stackPane;
	@FXML
	private JFXTextField usernameInput;
	@FXML
	private JFXToggleButton toggleUP;
	@FXML
	public Label titleLabel;
	@FXML
	private JFXButton sendButton;

	private String actualEmail;
	private boolean toggleSelected = false;
	private Email email;
	private DatabaseModel databaseModel;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		sendButton.setDisable(true);
		sendButton.setOpacity(0.50);
		databaseModel = new DatabaseModel();
		usernameInput.setPromptText("U S E R N A M E");

		stackPane.requestFocus();
	}


	public void handleToggle(ActionEvent event)
	{
		if(((JFXToggleButton)event.getSource()).isSelected()) {
			usernameInput.setPromptText("E M A I L");
			toggleSelected = true;
			titleLabel.setText("GET USERNAME");
		}else{
			usernameInput.setPromptText("U S E R N A M E");
			toggleSelected = false;
			titleLabel.setText("GET PASSWORD");
		}
	}


	public void sendEmailCode(ActionEvent event)
	{
		String request = usernameInput.getText();
		try {
			if(toggleSelected)
				processUsernameRequest(request);
			else
				processPasswordResetCode(event);

		}catch (SQLException e) {
			e.printStackTrace();
		}
		sendButton.setDisable(true);
		sendButton.setOpacity(0.50);
	}

	private void processUsernameRequest(String request) throws SQLException
	{
		String hiddenEmail;
		if(verifyEmailRegistered(request)){
			email = new Email(null);
			email.setRecipient(request);
			email.setUsernameRequestContent(databaseModel.getUsername(request));
			sendRequest(email);
			hiddenEmail = hideEmail(request);
			usernameInput.setText("");
			new DialogeBox(stackPane).drawerOkButton("Sent\n" + hiddenEmail, new JFXDialog());
		} else{
			new DialogeBox(stackPane).drawerOkButton("Email doesn't Exist", new JFXDialog());
		}
	}

	private void processPasswordResetCode(ActionEvent event) throws SQLException
	{
		String hiddenEmail;
		if (event.getSource().equals(sendButton) && databaseModel.isUsernameTaken(usernameInput.getText())) {
			getEmailAddress();
			email.setRecipient(actualEmail);
			hiddenEmail = hideEmail(actualEmail);
			if (sendRequest(email)){
				usernameInput.setText("");
				new DialogeBox(stackPane).drawerOkButton("Sent\n" + hiddenEmail, new JFXDialog());
			}else{
				sendButton.setDisable(false);
			}
		} else {
			usernameInput.setText("");
			new DialogeBox(stackPane).drawerOkButton("Incorrect Username", new JFXDialog());
		}
	}

	private boolean sendRequest(Email email)
	{
		try {
			email.run();
		}catch (RuntimeException e){
			new DialogeBox(stackPane).drawerOkButton("Not connected to Internet\n",new JFXDialog());
			return false;
		}
		return true;
	}

	private boolean verifyEmailRegistered(String request) throws SQLException
	{
		return request.equalsIgnoreCase(databaseModel.verfiyEmailAddres(request));
	}

	private String hideEmail(String emailToHide)
	{
		int indexToContinue=0;
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = 0 ; i<emailToHide.length();i++){
			if(emailToHide.charAt(i+2)=='@'){
				indexToContinue = i;
				break;
			}
		}
		return stringBuilder.append("***").append(emailToHide.substring(indexToContinue)).toString();
	}


	private void getEmailAddress()
	{
		String secureTempPass;
		HashPassword hashPassword = new HashPassword();
		hashPassword.setHashPassword(generateRandomWord());
		secureTempPass = hashPassword.toString().substring(0,9);
		email = new Email(secureTempPass);
		try {
			setActualEmail(databaseModel.getAccountInfo(usernameInput.getText(), "email"));
			databaseModel.updateTempPassCells(usernameInput.getText()
					, secureTempPass, Long.toString(System.currentTimeMillis()));
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String generateRandomWord()
	{
		Random random = new Random();
		StringBuilder randomWord = new StringBuilder();
		for(int i = 0; i < 3; i++)
			randomWord.append((char) (random.nextInt(26) + 65));
		return randomWord.toString();
	}

	private void setActualEmail(String email)
	{
		actualEmail = email;
	}

	public void handleKeyPressed()
	{
		usernameInput.setPromptText("USERNAME");
		if(usernameInput.getText().length() > 3){
			sendButton.setDisable(false);
			sendButton.setOpacity(0.9);
		}
		if(usernameInput.getText().isEmpty() || usernameInput.getText().length() < 3){
			sendButton.setDisable(true);
			sendButton.setOpacity(0.50);
		}
	}
}
