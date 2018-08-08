package Machine.Application.Controllers;

import Machine.AccountManager.HashPassword;

import Machine.Application.Controllers.Model.MachineModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registerController {

	private Pattern pattern;
	private Matcher matcher;

	@FXML
	private StackPane stackPane, stackPane1;

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
	private JFXTextField lastName, emailLabel;

	@FXML
	private JFXTextField accountType;

	@FXML
	private JFXButton returnButton;

	private DialogeBox dialogeBox;

	private MachineModel machineModel;

	private LoadScene loadScene;

	private static final String EMAIL_PATTERN =
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@FXML
	public void initialize()
	{
		loadScene = new LoadScene(stackPane,stackPane1);
		accountType.setText("CHECKING");
		dialogeBox = new DialogeBox(stackPane);
		dialogeBox.setNonStackPane(registerPane);
		disableRegisterButton();
	}

	void setMachineModel(MachineModel model)
	{
		machineModel = model;
	}

	@FXML
	void handleRegisterButton(ActionEvent event)
	{
		if(event.getSource().equals(registerButton)) {
			String first = firstName.getText(), last = lastName.getText();
			String user = username.getText(), password = pass.getText();
			String confPass = confirmPass.getText(), email = emailLabel.getText();

			if(isValidEmailAddress(email)) {
				if (emptyFieldExists(pass, confirmPass, firstName, lastName, username))
					dialogeBox.OkButton("Fields are empty", new JFXDialog());
				else if (password.equals(confPass) && isValidPassword(confPass)) {
					if (userAlreadyExists(user)) {
						dialogeBox.OkButton("Username is taken", new JFXDialog());
					} else {
						saveUserToFile(user, password, first, last, email);
						clearFields(pass, confirmPass, firstName, lastName, username);
						dialogeBox.returnToLogin("Welcome " + first, stackPane, stackPane);
					}
				} else{
					dialogeBox.OkButton("Invalid Password", new JFXDialog());
				}
			} else {
				dialogeBox.OkButton("Invalid Email", new JFXDialog());
				pass.setText(""); confirmPass.setText("");
			}
		}
	}

	private boolean isValidPassword(String request)
	{
		return request.length()>= 8;
	}

	private boolean isValidEmailAddress(String request)
	{
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(request);
		return matcher.matches();
	}

	private boolean userAlreadyExists(String user)
	{
		try {
			return machineModel.isUsernameTaken(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void saveUserToFile(String...details)
	{
		HashPassword hash = new HashPassword(details[1]);
		try {
			machineModel.saveUserToDatabase(details[0],hash.toString(),
					details[2],details[3],details[4]);
			machineModel.createStatementTable(details[0]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void returnHandler(ActionEvent event)
	{
		if(event.getSource().equals(returnButton)) {
			loadScene.loginPage("..\\FXMLs\\loginPage.fxml");
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

	public void enableRegisterButton(KeyEvent keyEvent)
	{
		if(emailLabel.getText().length()>6) {
			enableRegisterButton();
		}else if(emailLabel.getText().isEmpty() || emailLabel.getText().length()<6){
			disableRegisterButton();
		}
	}

	private void enableRegisterButton()
	{
		registerButton.setDisable(false);
		registerButton.setOpacity(0.85);
	}
	private void disableRegisterButton()
	{
		registerButton.setDisable(true);
		registerButton.setOpacity(0.42);
	}
}
