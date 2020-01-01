package Machine.Application.Controllers;

import Machine.AccountManager.HashPassword;
import Machine.Application.Controllers.Model.DatabaseModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ChangePassController implements Initializable
{
	private final static long FIFTEEN_MIN = 900000;
	@FXML
	private StackPane NewPasswordPane;

	@FXML
	private JFXPasswordField tempPass;

	@FXML
	private JFXPasswordField newPass;

	@FXML
	private JFXPasswordField confNewPass;

	@FXML
	private JFXButton changeButton;

	@FXML
	private JFXTextField username;

	private DatabaseModel databaseModel;
	private DialogeBox dialogeBox;
	private static HashPassword hashPassword;


	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		hashPassword = new HashPassword();
		dialogeBox = new DialogeBox(NewPasswordPane);
		changeButton.setDisable(true);
		changeButton.setOpacity(0.50);
		confNewPass.setOpacity(1.0);
		username.setOpacity(1.0);
		tempPass.setOpacity(1.0);
		newPass.setOpacity(1.0);
		NewPasswordPane.requestFocus();
	}


	public void verifyNewPassword()
	{
		databaseModel = new DatabaseModel();
		String user = username.getText();
		String temp = tempPass.getText();
		String password = newPass.getText();
		String conf = confNewPass.getText();
		if(password.equals(conf)) {
			try {
				if(validPassword(password)){
					if(isWithinTimeLimit(user)) {
						if(validateTempPassword(temp,user)) {
							hashPassword.setHashPassword(conf);
							databaseModel.updateNewPassword(user, hashPassword.toString());
							clearAllFields();
							setDialogMessageAndShow("Success\nPassword Changed");
							databaseModel.unlockAccount(user);
						}else{
							dialogeBox.drawerOkButton("Invalid Code", new JFXDialog());
						}
					}else{
						dialogeBox.drawerOkButton("Code Expired", new JFXDialog());
					}
				}else {
					setDialogMessageAndShow("Password >= 8 Chars");
					newPass.setText("");
					confNewPass.setText("");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			setDialogMessageAndShow("Password Doesn't Match");
			newPass.setText("");
			confNewPass.setText("");
		}
	}

	private void setDialogMessageAndShow(String message)
	{
		dialogeBox.drawerOkButton(message,new JFXDialog());
	}

	private boolean validPassword(String pass)
	{
		return pass.length()>=8;
	}

	private boolean validateTempPassword(String userTempPass, String user) throws SQLException
	{
		String tempPassFromDB;
		if (databaseModel.isUsernameTaken(user)) {
			tempPassFromDB = databaseModel.getAccountInfo(user, "TempPassword");
			if (!tempPassFromDB.equals(userTempPass)) {
				return false;
			}else{
				return true;
			}
		}else {
			dialogeBox.drawerOkButton("Invalid Username", new JFXDialog());
			return false;
		}
	}

	private boolean isWithinTimeLimit(String user) throws SQLException
	{
		long currTime = System.currentTimeMillis();
		long oldTime = 0;
		if (databaseModel.isUsernameTaken(user)) {
			try {
				oldTime = Long.parseLong(databaseModel.getAccountInfo(user, "ExpireTime"));
			} catch (NumberFormatException e) {
				return false;
			}
			if (currTime - oldTime > FIFTEEN_MIN) {
				return false;
			}
		}
		return true;
	}

	private void clearAllFields()
	{
		username.setText("");
		tempPass.setText("");
		newPass.setText("");
		confNewPass.setText("");
	}
	public void KeyReleased(KeyEvent keyEvent)
	{
		if (confNewPass.getText().length() == newPass.getText().length()) {
			changeButton.setOpacity(0.9);
			changeButton.setDisable(false);
		}
		if(keyEvent.getCode().equals(KeyCode.ENTER)){
			verifyNewPassword();
		}
	}

	public void handleDragReleased(MouseDragEvent mouseDragEvent)
	{
		System.out.println("Drag detected");
	}
}
