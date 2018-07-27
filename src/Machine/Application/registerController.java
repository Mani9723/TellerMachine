package Machine.Application;

import Machine.AccountManager.CheckingAccount;
import Machine.AccountManager.hashPassword;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.*;
import java.util.Scanner;

public class registerController {

	@FXML
	private StackPane stackPane;

	@FXML
	private AnchorPane registerPane, rootPane;

	@FXML
	private JFXButton registerButton;

	@FXML
	private JFXTextField username;

	@FXML
	private JFXPasswordField pass;

	@FXML
	private JFXPasswordField confirmPass;

	@FXML
	private JFXTextField firstName;

	@FXML
	private JFXTextField lastName;

	@FXML
	private JFXTextField accountType;

	@FXML
	private JFXButton returnButton;

	private CheckingAccount checkingAccount;
	private String passFilePath;

	@FXML
	public void initialize()
	{
		accountType.setText("CHECKING");
		System.out.println("Fetching Password File Path...");
		passFilePath = new CheckingAccount().getLoginDirPath();
		System.out.println(passFilePath);
	}


	@FXML
	void handleRegisterButton(ActionEvent event)
	{
		String first = firstName.getText();
		String last = lastName.getText();
		String user = username.getText();
		String password = pass.getText();
		String confPass = confirmPass .getText();

		if(password.equals(confPass)) {
			File file = new File(passFilePath);
			if (file.length() == 0) {
				System.out.println("File is empty");
				checkingAccount = new CheckingAccount(user);
				saveUserToFile(user, password, first, last);
			} else {
				System.out.println("Not Empty");
				if (userAlreadyExists(user)) {
					System.out.println("Checking username availability");
					System.out.println("Username is taken");
					System.exit(-1);
				} else {
					System.out.println("Unique username...creating and saving file...");
					checkingAccount = new CheckingAccount(user);
					saveUserToFile(user, password, first, last);
				}
			}
		}
		else {
			System.out.println("Password does not match");
			System.exit(-1);
		}

		System.out.println("User created");
		clearFields(pass,confirmPass,firstName,lastName,username);
	}

	private boolean userAlreadyExists(String user)
	{
		String currUserName;
		Scanner inputStream = null;
		try{
			inputStream = new Scanner(new File(passFilePath));
			while (inputStream.hasNextLine()){
				currUserName = inputStream.nextLine().split(",")[0];
				System.out.println(currUserName);
				if(currUserName.equals(user)){
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

	private void saveUserToFile(String...details)
	{
		hashPassword hash = new hashPassword(details[1]);
		PrintWriter outputStream = null;
		try{
			outputStream = new PrintWriter(new FileOutputStream(passFilePath,true));
		}catch (IOException e){
			e.printStackTrace();
		}
		assert outputStream != null;
		outputStream.append(details[0].concat(",").concat(hash.toString().concat(",")
				.concat(details[2]).concat(",").concat(details[3]).concat(",")
				.concat(checkingAccount.getAccountDirPath())).concat("\n"));

		outputStream.close();
	}

	@FXML
	void returnHandler(ActionEvent event)
	{
		if(event.getSource().equals(returnButton)) {
			try {
				rootPane = FXMLLoader.load(getClass().getResource("bankUI.fxml"));
				stackPane.getChildren().setAll(rootPane);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void clearFields(JFXPasswordField passwordField, JFXPasswordField field2,JFXTextField...textFields)
	{
		for(JFXTextField field: textFields)
			field.setText("");
		passwordField.setText("");
		field2.setText("");
	}

}
