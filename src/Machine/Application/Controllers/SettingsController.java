package Machine.Application.Controllers;

import Machine.AccountManager.HashPassword;
import Machine.Application.Controllers.Model.DatabaseModel;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class SettingsController implements Initializable
{

	@FXML
	private JFXButton confirmButton, menuButton, delAccount, confDel, cancel, changeEmailButton;

	@FXML
	private StackPane stackPane;

	@FXML
	private Label firstname, memberSinceLabel;

	@FXML
	private AnchorPane anchor;

	@FXML
	private Label lastName;

	@FXML
	private Label username;

	@FXML
	private Label email;

	@FXML
	private JFXPasswordField currPass;

	@FXML
	private JFXPasswordField newPass;

	@FXML
	private JFXPasswordField confnewPass;

	@FXML
	private JFXPasswordField reenterPassword;

	@FXML
	private Label date;

	private static DatabaseModel databaseModel;
	private LoadScene loadScene;
	private DialogeBox dialogeBox;
	private static HashPassword hashPassword;
	private boolean isEmailChangeMode = false;


	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		hashPassword = new HashPassword();
		loadScene = new LoadScene();
		dialogeBox = new DialogeBox(stackPane);
		dialogeBox.setNonStackPane(anchor);
		date.setText(getDate());
		date.setTextFill(Color.valueOf("white"));
		confirmButton.setDisable(true);
		cancel.setVisible(false);
		confDel.setVisible(false);
		reenterPassword.setVisible(false);
		stackPane.requestFocus();
	}

	void init(String user, DatabaseModel machine)
	{
		databaseModel = machine;
		try {
			firstname.setText(databaseModel.getAccountInfo(user,"Firstname"));
			username.setText(user);
			lastName.setText(databaseModel.getAccountInfo(user,"Lastname"));
			email.setText(databaseModel.getAccountInfo(user,"Email"));
			memberSinceLabel.setText("Member Since: " +
					databaseModel.getAccountInfo(username.getText(),"DateCreated").substring(0,10));
			memberSinceLabel.setContentDisplay(ContentDisplay.CENTER);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void handleButtonEnable()
	{
		String pass = confnewPass.getText();
		if (pass.length() >= 8)
			confirmButton.setDisable(false);
		else
			confirmButton.setDisable(true);
	}

	@FXML
	public void handleDelButton(ActionEvent event)
	{
		if(event.getSource().equals(delAccount)){
			setFunctionality(true);
			changeEmailButton.setDisable(true);
			reenterPassword.setPromptText("RE-ENTER PASSWORD");
		}
	}

	@FXML
	public void handleChangeEmailButton(ActionEvent event)
	{
		if(event.getSource().equals(changeEmailButton)){
			this.isEmailChangeMode = true;
			setFunctionality(true);
			changeUserEmailAddress();
			delAccount.setDisable(true);
		}
	}

	@FXML
	public void confirmDelButton(ActionEvent event)
	{
		if(event.getSource().equals(confDel)){
			confirmAccountDeletion();
		}

	}

	private void changeUserEmailAddress()
	{
		reenterPassword.setPromptText("NEW EMAIL ADDRESS");
		delAccount.setDisable(true);
	}

	private void confirmAccountDeletion()
	{
		if(isEmailChangeMode){
			if(!reenterPassword.getText().isEmpty()){
				try {
					databaseModel.updateEmailAddress(username.getText(),reenterPassword.getText());
					dialogeBox.OkButton("Email Succesfully Changed", new JFXDialog());
					delAccount.setDisable(false);
					email.setText(reenterPassword.getText());
					reenterPassword.setText("");
					setFunctionality(false);
					changeEmailButton.setDisable(false);
					this.isEmailChangeMode = false;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}else {
			if (!reenterPassword.getText().isEmpty()) {
				String pass = reenterPassword.getText();
				hashPassword.setHashPassword(pass);
				try {
					if (databaseModel.getAccountInfo(username.getText(), "Password").equals(hashPassword.toString())) {
						databaseModel.deleteUser(username.getText());
						loadScene = new LoadScene(stackPane, new StackPane());
						loadScene.loginPage();
						dialogeBox.OkButton("Thank you for you being a customer.\nYour account is deleted",
								new JFXDialog());
					} else {
						dialogeBox.OkButton("Incorrect Password", new JFXDialog());
						reenterPassword.setText("");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				dialogeBox.OkButton("Please Enter Password", new JFXDialog());
			}
		}
		stackPane.requestFocus();
	}

	@FXML
	public void cancelDelButton(ActionEvent event)
	{
		if(event.getSource().equals(cancel)){
			setFunctionality(false);
			reenterPassword.setText("");
			changeEmailButton.setDisable(false);
			delAccount.setDisable(false);
			stackPane.requestFocus();

		}
	}

	@FXML
	public void loadHomeScene(ActionEvent event)
	{
		if(event.getSource().equals(menuButton)){
			loadScene.setActionEvent(event);
			loadScene.homeSceneAction(username.getText(), databaseModel);
		}
	}

	@FXML
	public void handlePassChange(ActionEvent event)
	{
		if(event.getSource().equals(confirmButton)){
			if(isCurrentPassValid()) {
				if (validateNewPass()) {
					hashPassword.setHashPassword(newPass.getText());
					try {
						databaseModel.updateNewPassword(username.getText(), hashPassword.toString());
					} catch (SQLException e) {
						e.printStackTrace();
					}
					dialogeBox.OkButton("Password Changed", new JFXDialog());
					currPass.setText("");
				} else {
					dialogeBox.OkButton("Password does not match", new JFXDialog());
				}
				newPass.setText("");
				confnewPass.setText("");
			}else{
				dialogeBox.OkButton("Original Password is incorrect", new JFXDialog());
				currPass.setText("");
			}
		}
	}

	private boolean isCurrentPassValid()
	{
		hashPassword.setHashPassword(currPass.getText());
		String encryptedPass = hashPassword.toString();
		try {
			return encryptedPass.equals(databaseModel.getAccountInfo(username.getText(),"Password"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void setFunctionality(boolean isFunctional)
	{
		confDel.setVisible(isFunctional);
		cancel.setVisible(isFunctional);
		currPass.setDisable(isFunctional);
		newPass.setDisable(isFunctional);
		confnewPass.setDisable(isFunctional);
		reenterPassword.setVisible(isFunctional);
	}

	private boolean validateNewPass()
	{
		return newPass.getText().equals(confnewPass.getText())
				&& newPass.getText().length() >=8 && confnewPass.getText().length() >=8;
	}

	private String getDate()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		return dateFormat.format(date);
	}
}