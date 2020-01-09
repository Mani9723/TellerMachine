package Machine.Application.Controllers;

import Machine.AccountManager.HashPassword;

import Machine.Application.Controllers.Model.DatabaseModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class loginController implements Initializable {

	@FXML
	private AnchorPane rootPane;

	@FXML
	private JFXDrawer drawerPane;

	@FXML
	private StackPane stackPane;

	@FXML
	private JFXTextField username;

	@FXML
	private JFXPasswordField password;

	@FXML
	private JFXButton login, exitResetPass, forgotButton, makeNewPass;

	@FXML
	private JFXButton registerButton;

	@FXML
	private JFXButton exit;

	@FXML
	private Label capsLockLabel, welcomeLabel, bankTitle;

	private static final int MAX_LOGIN_ATTEMPT = 3;
	private boolean isCapsOn = false;
	private JFXDialog jfxDialog = new JFXDialog();
	private static DatabaseModel databaseModel;
	private DialogeBox dialogeBox;
	private LoadScene loadScene;
	private static HashPassword hashPassword;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadScene = new LoadScene();
		dialogeBox = new DialogeBox(stackPane);
		dialogeBox.setNonStackPane(rootPane);
		databaseModel = new DatabaseModel();
		hashPassword = new HashPassword();

		capsLockLabel.setVisible(false);
		capsLockLabel.setText("CAPS LOCK ON");
		login.setDisable(true);
		exitResetPass.setDisable(true);
		exitResetPass.setVisible(false);

		//stackPane.requestFocus();
	}

	@FXML
	public void verifyMinLoginInput(KeyEvent event)
	{
		if (password.getText().length() > 3)
			login.setDisable(false);
		if (password.getText().length() < 3 || username.getText().length() < 3)
			login.setDisable(true);
		if (event.getCode().equals(KeyCode.CAPS))
			switchCapsLockLabel();
		if(event.getCode().equals(KeyCode.ENTER)) {
			try {
				EnterKey(event);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void EnterKey(KeyEvent event) throws SQLException{
		if (event.getCode().equals(KeyCode.ENTER)) {
			if (databaseModel.isFirstTimeRunning()) {
				showFirstTimeUsingDialog();
			} else if(databaseModel.usernameExists(username.getText().toLowerCase())){
				if(attemptsRemaining()) {
					if (processCredentials() && !databaseModel.accountLocked(username.getText())) {
						if (username.getText().equalsIgnoreCase("admin")) {
							loadScene.setKeyEvent(event);
							loadScene.adminSceneKeyEvent(databaseModel);
						} else {
							loadHomePage(null, event);
						}
						databaseModel.resetLoginAttempts(username.getText());
					} else {
						handleIncorrectCredentials();
					}
				}
				else{
					dialogeBox.OkButton("Account Locked. Reset Password",jfxDialog);
					resetLoginAreas();
				}
			}else{
				dialogeBox.OkButton("Incorrent Credentials", jfxDialog);
				resetLoginAreas();
			}
		}
	}

	private void resetLoginAreas()
	{
		username.setText("");
		password.setText("");
		login.setDisable(true);
	}

	@FXML
	public void EnterKeyRegister(KeyEvent event)
	{
		if(event.getCode().equals(KeyCode.ENTER) && event.getSource().equals(registerButton)){
			registerButtonKeyEvent(event);
		}
	}

	@FXML
	void exit(ActionEvent event) {
		if (event.getSource().equals(exit)) {
			System.exit(0);
		}
	}

	@FXML
	void resetPassword(ActionEvent event) {
		openDrawerPane("/Machine/Application/FXMLs/SendCodePage.fxml",event);
	}

	@FXML
	void createNewPassword(ActionEvent event)
	{
		openDrawerPane("/Machine/Application/FXMLs/ChangePassPage.fxml",event);
	}

	@FXML
	void loginButtonHandler(ActionEvent event)
	{
		if(event.getSource().equals(login)){
			try {
				loginProcess(event);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	public void registerButtonHandler(ActionEvent event)
	{
		if(event.getSource().equals(registerButton)){
			loadScene.setActionEvent(event);
			loadScene.registerScene(databaseModel);
		}
	}

	@FXML
	public void registerButtonKeyEvent(KeyEvent keyEvent)
	{
		loadScene.setKeyEvent(keyEvent);
		loadScene.registerScene(databaseModel);
	}
	private void modifyButtonVisibility(boolean disable)
	{
		registerButton.setDisable(disable);
		forgotButton.setDisable(disable);
		makeNewPass.setDisable(disable);
		username.setDisable(disable);
		password.setDisable(disable);
		welcomeLabel.setDisable(disable);
		bankTitle.setDisable(disable);
	}

	private void loginProcess(ActionEvent event) throws SQLException
	{
		if(databaseModel.isFirstTimeRunning()){
			showFirstTimeUsingDialog();
		} else if(databaseModel.usernameExists(username.getText())){
			if(attemptsRemaining()) {
				if (processCredentials() && !databaseModel.accountLocked(username.getText())) {
					if (username.getText().equalsIgnoreCase("admin")) {
						loadScene.setActionEvent(event);
						loadScene.adminSceneActionEvent(databaseModel);
					} else {
						loadHomePage(event, null);
					}
					databaseModel.resetLoginAttempts(username.getText());
				} else {
					handleIncorrectCredentials();
				}
			}else{
				dialogeBox.OkButton("Account Locked. Reset Password",jfxDialog);
				username.setText("");
				password.setText("");
				login.setDisable(true);
			}
		}else{
			dialogeBox.OkButton("Incorrent Credentials", jfxDialog);
		}
	}


	private void switchCapsLockLabel()
	{
		if (isCapsOn) {
			isCapsOn = false;
			capsLockLabel.setVisible(false);
		} else {
			isCapsOn = true;
			capsLockLabel.setVisible(true);
		}
	}

	private void handleIncorrectCredentials() {
		jfxDialog.requestFocus();
		try {
			databaseModel.updateAttempts(username.getText());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(!attemptsRemaining()) {
			try {
				lockThisAccount();
				username.setText("");
				password.setText("");
				login.setDisable(true);
				return;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		dialogeBox.OkButton("Incorrect Credentials", jfxDialog);
		username.setText("");
		password.setText("");
		login.setDisable(true);
	}

	private void lockThisAccount() throws SQLException
	{
		dialogeBox.OkButton("Account Locked. Reset Password",jfxDialog);
		databaseModel.lockAccount(username.getText());

	}

	private boolean attemptsRemaining()
	{
		try {
			return databaseModel.getLoginAttempts(username.getText()) < MAX_LOGIN_ATTEMPT;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void showFirstTimeUsingDialog()
	{
		dialogeBox.OkButton("Welcome, please register first", new JFXDialog());
		username.setText("");
		password.setText("");
		login.setDisable(true);
	}

	private boolean processCredentials()
	{
		hashPassword.setHashPassword(password.getText());
		String securePass = hashPassword.toString();
		try {
			return databaseModel.validateLogin(username.getText(), securePass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void loadHomePage(ActionEvent event, KeyEvent keyEvent)
	{
		if(event != null) {
			loadScene.setActionEvent(event);
			loadScene.homeSceneAction(username.getText(), databaseModel);
		}
		else if(keyEvent != null) {
			loadScene.setKeyEvent(keyEvent);
			loadScene.homeScene(username, databaseModel);
		}
	}

	private void openDrawerPane(String scenePath, ActionEvent event)
	{
		exitResetPass.setDisable(false);
		exitResetPass.setVisible(true);
		try {
			StackPane resetPane = FXMLLoader.load(getClass().getResource(scenePath));
			drawerPane.setSidePane(resetPane);

			if ((drawerPane.isOpened() && event.getSource().equals(exitResetPass)) || drawerPane.isClosing()) {
				drawerPane.close();
				exitResetPass.setDisable(true);
				exitResetPass.setVisible(false);
				modifyButtonVisibility(false);
			} else {
				drawerPane.open();
				modifyButtonVisibility(true);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleDragUp(MouseDragEvent event)
	{
		if(event.isDragDetect())
			System.out.println("Dragup hit");
	}
}
