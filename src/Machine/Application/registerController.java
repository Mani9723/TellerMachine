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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

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
	private DialogeBox dialogeBox;

	private MachineModel machineModel = new MachineModel();

	@FXML
	public void initialize()
	{
		accountType.setText("CHECKING");
		System.out.println("Fetching Password File Path...");
		passFilePath = new CheckingAccount().getLoginDirPath();
		System.out.println(passFilePath);
		dialogeBox = new DialogeBox(stackPane);
	}

	@FXML
	void handleRegisterButton(ActionEvent event)
	{
		if(event.getSource().equals(registerButton)) {
			String first = firstName.getText(), last = lastName.getText();
			String user = username.getText(), password = pass.getText();
			String confPass = confirmPass.getText();

			if(emptyFieldExists(pass,confirmPass,firstName,lastName,username))
				dialogeBox.OkButton("Fields are empty", new JFXDialog());

			else if (password.equals(confPass)) {
//				File file = new File(passFilePath);
//				if (file.length() == 0) {
//					checkingAccount = new CheckingAccount(user);
//					saveUserToFile(user, password, first, last);
//					dialogeBox.returnToLogin("Welcome " + first, rootPane, stackPane);
//				}
//           {
					if (userAlreadyExists(user)) {
						dialogeBox.OkButton("Username is taken", new JFXDialog());
					} else {
						checkingAccount = new CheckingAccount();
						saveUserToFile(user, password, first, last);
						clearFields(pass, confirmPass, firstName, lastName, username);
						dialogeBox.returnToLogin("Welcome " + first, rootPane, stackPane);
					}
				}
			else {
				dialogeBox.OkButton("Password does not match", new JFXDialog());
				pass.setText(""); confirmPass.setText("");
			}
		}
	}

	private boolean userAlreadyExists(String user)
	{
		try {
			return machineModel.isUsernameTaken(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
//		String currUserName;
//		Scanner inputStream = null;
//		try{
//			inputStream = new Scanner(new File(passFilePath));
//			while (inputStream.hasNextLine()){
//				currUserName = inputStream.nextLine().split(",")[0];
//				System.out.println(currUserName);
//				if(currUserName.equals(user)){
//					inputStream.close();
//					return true;
//				}
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		inputStream.close();
//		return false;
	}

	private void saveUserToFile(String...details)
	{
		hashPassword hash = new hashPassword(details[1]);
		try {
			machineModel.saveToDatabase(username.getText(),hash.toString(),
					firstName.getText(),lastName.getText());
		} catch (SQLException e) {
			e.printStackTrace();
		}


//		PrintWriter outputStream = null;
//		try{
//			outputStream = new PrintWriter(new FileOutputStream(passFilePath,true));
//		}catch (IOException e){
//			e.printStackTrace();
//		}
//		assert outputStream != null;
//		outputStream.append(details[0].concat(",").concat(hash.toString().concat(",")
//				.concat(details[2]).concat(",").concat(details[3]).concat(",")
//				.concat(checkingAccount.getAccountDirPath())).concat("\n"));
//
//		outputStream.close();
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

	private boolean emptyFieldExists(JFXPasswordField passwordField, JFXPasswordField pass, JFXTextField...field)
	{
		for(JFXTextField textField: field){
			if(textField.getText().length() == 0) return true;
		}
		return pass.getText().length() == 0 || passwordField.getText().length() == 0;
	}

}
