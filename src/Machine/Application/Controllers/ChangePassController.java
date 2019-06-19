package Machine.Application.Controllers;

import Machine.AccountManager.HashPassword;
import Machine.Application.Controllers.Model.MachineModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

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

	private MachineModel machineModel;
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
		machineModel = new MachineModel();
		String user = username.getText();
		String temp = tempPass.getText();
		String password = newPass.getText();
		String conf = confNewPass.getText();
		if(password.equals(conf)) {
			try {
				if(validPassword(password)){
					if(validTimeAndInput(temp,user)) {
						hashPassword.setHashPassword(conf);
						machineModel.updateNewPassword(user, hashPassword.toString());
						clearAllFields();
						setDialogMessageAndShow("Success\nPassword Changed");
					}else{
						setDialogMessageAndShow("Password Doesn't Match");
						newPass.setText("");
						confNewPass.setText("");
					}
				}else {
					setDialogMessageAndShow("Password >= 8 Chars");
					newPass.setText("");
					confNewPass.setText("");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
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

	private boolean validTimeAndInput(String userTempPass, String user) throws SQLException
	{
		String tempPassFromDB;
		long currTime = System.currentTimeMillis();
		long oldTime;
		if (machineModel.isUsernameTaken(user)) {
			oldTime = Long.parseLong(machineModel.getAccountInfo(user, "ExpireTime"));
			tempPassFromDB = machineModel.getAccountInfo(user, "TempPassword");
			if (currTime - oldTime > FIFTEEN_MIN) {
				dialogeBox.drawerOkButton("Code Expired", new JFXDialog());
				return false;
			} else if (!tempPassFromDB.equals(userTempPass)) {
				dialogeBox.drawerOkButton("Invalid Code", new JFXDialog());
				return false;
			}
			return true;
		}else {
			dialogeBox.drawerOkButton("Invalid Username", new JFXDialog());
			return false;
		}
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
		if(confNewPass.getText().length() == newPass.getText().length())
			changeButton.setOpacity(0.9);
		changeButton.setDisable(false);
	}
}
