package Machine.Application.Controllers;

import Machine.AccountManager.HashPassword;

import Machine.Application.Controllers.Model.MachineModel;
import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class loginController implements Initializable
{

	@FXML
	private AnchorPane rootPane, secondPane, registerPane;

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
	private Label capsLockLabel;

	private MachineModel machineModel;
	private DialogeBox dialogeBox;
	private GaussianBlur gaussianBlur;
	private LoadScene loadScene;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		loadScene = new LoadScene();
		gaussianBlur = new GaussianBlur();
		gaussianBlur.setRadius(7.5);
		dialogeBox = new DialogeBox(stackPane);
		dialogeBox.setNonStackPane(rootPane);
		machineModel = new MachineModel();

		capsLockLabel.setVisible(false);
		login.setDisable(true);
		exitResetPass.setDisable(true);
		exitResetPass.setVisible(false);

		if(machineModel.isDbConnected()) {
			capsLockLabel.setText("Connected");
			capsLockLabel.setVisible(true);
		}else {
			capsLockLabel.setVisible(true);
			capsLockLabel.setText("Not Connected");
		}
	}

	@FXML
	void EnterKey(KeyEvent event)
	{
		if(password.getText().length()>3)
			login.setDisable(false);
		if(password.getText().length()<3 || username.getText().length() < 3)
			login.setDisable(true);
		if(event.getCode().equals(KeyCode.ENTER)){
			try {
				if(machineModel.isFirstTimeRunning()){
					dialogeBox.OkButton("Welcome, please register first",new JFXDialog());
					username.setText("");
					password.setText("");
					login.setDisable(true);
				} else if(processCredentials()){
					loadScene.setKeyEvent(event);
					loadScene.homeScene(username, machineModel);
				}
				else {
					dialogeBox.OkButton("Incorrect Credentials", new JFXDialog());
					username.setText("");
					password.setText("");
					login.setDisable(true);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

	@FXML
	void exit(ActionEvent event)
	{
		if(event.getSource().equals(exit)){
			System.exit(0);
		}
	}

	@FXML
	void handleCapsLock(KeyEvent event)
	{
		// DO NOTHING
	}

	@FXML
	void resetPassword(ActionEvent event)
	{
		exitResetPass.setDisable(false);
		exitResetPass.setVisible(true);
		try {
			StackPane resetPane = FXMLLoader.load(getClass().getResource("/Machine/Application/FXMLs/ResetPasswordPage.fxml"));
			drawerPane.setSidePane(resetPane);
			drawerPane.setDirection(JFXDrawer.DrawerDirection.TOP);

			if(drawerPane.isOpened() && event.getSource().equals(exitResetPass)) {
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

	private void modifyButtonVisibility(boolean disable)
	{
		registerButton.setDisable(disable);
		forgotButton.setDisable(disable);
		makeNewPass.setDisable(disable);
		username.setDisable(disable);
		password.setDisable(disable);
	}

	@FXML
	void createNewPassword(ActionEvent event)
	{
		exitResetPass.setDisable(false);
		exitResetPass.setVisible(true);
		try {
			StackPane resetPane = FXMLLoader.load(getClass().getResource("/Machine/Application/FXMLs/ChangePassPage.fxml"));
			drawerPane.setSidePane(resetPane);


			if(drawerPane.isOpened() && event.getSource().equals(exitResetPass)) {
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
	void loginHandler(ActionEvent event)
	{
		if(event.getSource().equals(login)){
			loginProcess(event);
		}
	}

	@FXML
	void register(ActionEvent event)
	{
		if(event.getSource().equals(registerButton)){
			loadScene.setActionEvent(event);
			//loadScene.setResgisterController(new registerController());
			loadScene.registerScene(machineModel);

		}
	}

	private void loginProcess(ActionEvent event)
	{
		try {
			if(machineModel.isFirstTimeRunning()){
				dialogeBox.OkButton("Welcome, please register first",new JFXDialog());
			}
			else if(processCredentials()){
				loadHomePage(event);
			} else{
				dialogeBox.OkButton("Incorrect Credentials", new JFXDialog());
				username.setText("");
				password.setText("");
				login.setDisable(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean processCredentials()
	{
		String securePass = new HashPassword(password.getText()).toString();
		try {
			return machineModel.validateLogin(username.getText(),securePass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void loadHomePage(ActionEvent event)
	{
		loadScene.setActionEvent(event);
		loadScene.homeSceneAction(username.getText(),machineModel);
	}
}
