package Machine.Application.Controllers;

import Machine.AccountManager.HashPassword;

import Machine.Application.Controllers.Model.DatabaseModel;
import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registerController {

	@FXML
	private StackPane stackPane;

	@FXML
	private AnchorPane registerPane;

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
	private JFXTextField lastName, emailLabel;

	@FXML
	private JFXTextField accountType;

	@FXML
	private JFXButton returnButton;

	private final static double VISIBLE_VAL = 0.85, INVISIBLE_VAL = 0.42;

	private DialogeBox dialogeBox;
	private DatabaseModel databaseModel;
	private LoadScene loadScene;
	private static final String EMAIL_PATTERN =
			"^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static HashPassword hashPassword;


	@FXML
	public void initialize()
	{
		hashPassword = new HashPassword();
		loadScene = new LoadScene(stackPane,new StackPane());
//		accountType.setText("CHECKING");
		dialogeBox = new DialogeBox(stackPane);
		dialogeBox.setNonStackPane(registerPane);
		setRegisterButtonVisbility(true,INVISIBLE_VAL);

		stackPane.requestFocus();
	}

	void setDatabaseModel(DatabaseModel model)
	{
		databaseModel = model;
	}

	@FXML
	void handleRegisterButton(ActionEvent event)
	{
		if(event.getSource().equals(registerButton)) {
			String first = firstName.getText(), last = lastName.getText();
			String user = username.getText(), password = pass.getText();
			String confPass = confirmPass.getText(), email = emailLabel.getText();


			if(isValidEmailFromat(email)) {
				if(!emailExists(email)) {
					if (emptyFieldExists(pass, confirmPass, firstName, lastName, username))
						dialogeBox.OkButton("Fields are empty", new JFXDialog());
					else if (password.equals(confPass) && isValidPasswordLength(confPass)) {
						if (usernameAlreadyExists(user)) {
							dialogeBox.OkButton("Username is taken", new JFXDialog());
						} else {
							saveUserToFile(user, password, first, last, email);
							clearFields(pass, confirmPass, firstName, lastName, username);
							dialogeBox.OkButton("Welcome " + first, new JFXDialog());
							loadScene.loginPage();
						}
					} else {
						dialogeBox.OkButton("Invalid Password", new JFXDialog());
						pass.setText(""); confirmPass.setText("");
					}
				}else{
					dialogeBox.OkButton("Email is already registered", new JFXDialog());
					emailLabel.setText("");
				}
			} else {
				dialogeBox.OkButton("Invalid Email", new JFXDialog());
				emailLabel.setText("");
			}
		}
	}

	@FXML
	void returnButtonHandler(ActionEvent event)
	{
		if(event.getSource().equals(returnButton))
			loadScene.loginPage();
	}

	private boolean isValidPasswordLength(String request)
	{
		return request.length()>= 8;
	}

	private boolean isValidEmailFromat(String request)
	{
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(request);
		return matcher.matches();
	}

	private boolean usernameAlreadyExists(String user)
	{
		try {
			return databaseModel.usernameExists(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean emailExists(String request)
	{
		try{
			return databaseModel.emailAlreadyExists(request.toLowerCase());
		}catch (SQLException e){
			e.printStackTrace();
		}
		return false;
	}

	private void saveUserToFile(String...details)
	{
		hashPassword.setHashPassword(details[1]);
		try {
			databaseModel.saveUserToMainDB(details[0],hashPassword.toString(),
					details[2],details[3],details[4]);
			databaseModel.createStatementTable(details[0]);
		} catch (SQLException e) {
			e.printStackTrace();
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
			if(textField.getText().isEmpty()) return true;
		}
		return pass.getText().isEmpty() || passwordField.getText().isEmpty();
	}

	public void enableRegisterButton()
	{
		if(emailLabel.getText().length()>6) {
			setRegisterButtonVisbility(false,VISIBLE_VAL);
		}else if(emailLabel.getText().isEmpty() || emailLabel.getText().length()<6){
			setRegisterButtonVisbility(true,INVISIBLE_VAL);
		}
	}

	private void setRegisterButtonVisbility(boolean disable, double opacity)
	{
		registerButton.setDisable(disable);
		registerButton.setOpacity(opacity);
	}
}