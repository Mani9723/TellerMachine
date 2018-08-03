package Machine.Application;

import Machine.AccountManager.HashPassword;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ChangePassController implements Initializable
{

	@FXML
	private ImageView imgLeft;

	@FXML
	private ImageView imgRight;


	@FXML
	private AnchorPane NewPasswordPane;

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
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		changeButton.setDisable(true);
		changeButton.setOpacity(0.50);
		imgLeft.setVisible(false);
		imgRight.setVisible(false);
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
			String tempPassFromDB;
			try {
				tempPassFromDB = machineModel.getAccountInfo(user, "TempPassword");
				if(tempPassFromDB.equals(temp)) {
					hashPassword = new HashPassword(conf);
					machineModel.updateNewPassword(user, hashPassword.toString());
					imgLeft.setVisible(true);
					imgRight.setVisible(true);
					username.setText("");
					tempPass.setText("");
					newPass.setText("");
					confNewPass.setText("");
				}else{
					tempPass.setText("");
					tempPass.setPromptText("TRY AGAIN");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			confNewPass.setPromptText("");
			confNewPass.setPromptText("NO MATCH");
		}

	}

	public void KeyReleased(KeyEvent keyEvent)
	{
		if(confNewPass.getText().length() == newPass.getText().length())
			changeButton.setOpacity(0.9);
			changeButton.setDisable(false);
	}
}
