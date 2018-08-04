package Machine.Application;

import Machine.AccountManager.Email;
import Machine.AccountManager.HashPassword;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;


public class ResetPasswordController implements Initializable
{
	@FXML
	private StackPane stackPane;
	@FXML
	private JFXTextField usernameInput;

	private String actualEmail;

	@FXML
	public Label sent;

	@FXML
	private JFXButton sendButton;

	private Email email;
	private MachineModel machineModel;


	public void sendEmailCode(ActionEvent event)
	{
		try {
			if(event.getSource().equals(sendButton) && machineModel.isUsernameTaken(usernameInput.getText())){
				getEmailAddress();
				email.setRecipient(actualEmail);
				String hiddenEmail = hideEmail(actualEmail);
				email.sendEmail();
				//sent.setText("Sent: " + hiddenEmail);
				usernameInput.setText("");
				new DialogeBox(stackPane).drawerOkButton("Sent\n"+hiddenEmail,new JFXDialog());
			}
			else{
				usernameInput.setText("");
				new DialogeBox(stackPane).drawerOkButton("Incorrect Username",new JFXDialog());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sendButton.setDisable(true);
		sendButton.setOpacity(0.50);
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
		secureTempPass = new HashPassword(generateRandomWord()).toString();
		email = new Email(secureTempPass);
		try {
			setActualEmail(machineModel.getAccountInfo(usernameInput.getText(), "email"));
			machineModel.updateTempPassCells(usernameInput.getText()
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

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		sendButton.setDisable(true);
		sendButton.setOpacity(0.50);
		machineModel = new MachineModel();
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
