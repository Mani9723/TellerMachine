package Machine.Application.Controllers;

import Machine.AccountManager.HashPassword;
import Machine.Application.Controllers.Model.MachineModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
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
	@FXML
	private StackPane NewPasswordPane;

	@FXML
	private ImageView imgLeft;

	@FXML
	private ImageView imgRight;

	private final static long FIFTEEN_MIN = 900000;

	@FXML
	private JFXTextField tempPass;

	@FXML
	private JFXTextField newPass;

	@FXML
	private JFXTextField confNewPass;

	@FXML
	private JFXButton changeButton;

	@FXML
	private JFXTextField username;

	private MachineModel machineModel;
	private DialogeBox dialogeBox;


	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		dialogeBox = new DialogeBox(NewPasswordPane);
		changeButton.setDisable(true);
		changeButton.setOpacity(0.50);
		imgLeft.setVisible(false);
		imgRight.setVisible(false);
		confNewPass.setOpacity(1.0);
		username.setOpacity(1.0);
		tempPass.setOpacity(1.0);
		newPass.setOpacity(1.0);
//		username.setBackground(new Background(new BackgroundFill(Paint.valueOf("snow"),null,null)));;
//		tempPass.setBackground(new Background(new BackgroundFill(Paint.valueOf("snow"),null,null)));;
//		newPass.setBackground(new Background(new BackgroundFill(Paint.valueOf("snow"),null,null)));;
		confNewPass.setBackground(new Background(new BackgroundFill(Paint.valueOf("snow"),null,null)));
	}


	public void verifyNewPassword()
	{
		machineModel = new MachineModel();
		HashPassword hashPassword;
		String user = username.getText();
		String temp = tempPass.getText();
		String password = newPass.getText();
		String conf = confNewPass.getText();
		if(password.equals(conf)) {
			try {
				if(validTimeAndInput(temp,user)) {
					hashPassword = new HashPassword(conf);
					machineModel.updateNewPassword(user, hashPassword.toString());
					clearAllFields();
					dialogeBox.drawerOkButton("Success\nPassword Changed", new JFXDialog());

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			dialogeBox.drawerOkButton("Password Doesn't Match", new JFXDialog());
			newPass.setText("");
			confNewPass.setText("");
		}
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
