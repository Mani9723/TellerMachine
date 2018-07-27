package Machine.Application;

import Machine.AccountManager.CheckingAccount;
import Machine.AccountManager.hashPassword;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class loginController {

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

	@FXML
	public void initialize()
	{
		capsLockLabel.setVisible(false);
	}

	@FXML
	void EnterKey(KeyEvent event)
	{
		if(event.getCode().equals(KeyCode.ENTER)){
			loginProcess();
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
		if(isValidCredentials(username.getText(),password.getText()))
			loadHomePage();

	}

	private boolean isValidCredentials(String username, String password)
	{
		hashPassword hash = new hashPassword(password);
		String securePassword = hash.toString();
		String[] data;
		Scanner inputStream = null;
		try{
			inputStream = new Scanner(new File(new CheckingAccount().getLoginDirPath()));
			while(inputStream.hasNextLine()) {
				data = inputStream.nextLine().split(",");
				if (username.equals(data[0]) && securePassword.equals(data[1])){
					inputStream.close();
					return true;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		inputStream.close();
		return false;
	}

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
