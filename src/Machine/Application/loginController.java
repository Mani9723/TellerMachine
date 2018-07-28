package Machine.Application;

import Machine.AccountManager.CheckingAccount;
import Machine.AccountManager.hashPassword;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Scanner;

public class loginController implements Initializable
{

	@FXML
	private AnchorPane rootPane, secondPane, registerPane;

	@FXML
	private StackPane stackPane;

	@FXML
	private JFXTextField username;

	@FXML
	private JFXPasswordField password;

	@FXML
	private JFXButton login;

	@FXML
	private JFXButton registerButton;

	@FXML
	private JFXButton exit;

	@FXML
	private Label capsLockLabel;

	private MachineModel machineModel;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		machineModel = new MachineModel();

		capsLockLabel.setVisible(false);
		login.setDisable(true);

		if(machineModel.isDbConnected()) {
			capsLockLabel.setText("Connected");
			capsLockLabel.setVisible(true);
		}else {
			capsLockLabel.setVisible(true);
			capsLockLabel.setText("Not Connected");
		}
	}

	@FXML
	public void initialize()
	{
		//Default
	}

	@FXML
	void EnterKey(KeyEvent event)
	{
		if(password.getText().length()>0)
			login.setDisable(false);
		if(event.getCode().equals(KeyCode.ENTER))
			loginProcess();
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

	}

	@FXML
	void loginHandler(ActionEvent event)
	{
		if(event.getSource().equals(login)){
			loginProcess();
		}
	}

	@FXML
	void register(ActionEvent event)
	{
		if(event.getSource().equals(registerButton)){
			try{
				stackPane= FXMLLoader.load(getClass().getResource("registerPage.fxml"));
				rootPane.getChildren().setAll(stackPane);
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void loginProcess()
	{
		String securePass = new hashPassword(password.getText()).toString();
		try {
			if(machineModel.validateLogin(username.getText(),securePass)){
				loadHomePage();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


//	private void loginProcess()
//	{
//		if(isValidCredentials(username.getText(),password.getText()))
//			loadHomePage();
//		else {
//			new DialogeBox(stackPane).OkButton("Incorrect Credentials", new JFXDialog());
//			username.setText("");
//			password.setText("");
//		}
//
//	}
//
//	private boolean isValidCredentials(String username, String password)
//	{
//		hashPassword hash = new hashPassword(password);
//		String securePassword = hash.toString();
//		String[] data;
//		Scanner inputStream = null;
//		try{
//			inputStream = new Scanner(new File(new CheckingAccount().getLoginDirPath()));
//			while(inputStream.hasNextLine()) {
//				data = inputStream.nextLine().split(",");
//				if (username.equals(data[0]) && securePassword.equals(data[1])){
//					inputStream.close();
//					return true;
//				}
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		inputStream.close();
//		return false;
//	}
//
	private void loadHomePage()
	{
		try{
			secondPane= FXMLLoader.load(getClass().getResource("homePage.fxml"));
			rootPane.getChildren().setAll(secondPane);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

}
