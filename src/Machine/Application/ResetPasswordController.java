package Machine.Application;

import Machine.AccountManager.Email;
import Machine.AccountManager.HashPassword;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class ResetPasswordController implements Initializable
{

	@FXML
	private StackPane stackPane;

	@FXML
	private AnchorPane rootPane;
	@FXML
	private JFXTextField usernameInput;

	@FXML
	private JFXTextField emailInput;

	private String hiddenEmail,actualEmail;

	@FXML
	public Label sent;

	@FXML
	private JFXButton sendButton;

	MachineModel machineModel;
	Email email;


	public void sendEmailCode(ActionEvent event)
	{
		if(event.getSource().equals(sendButton)){
			getEmailAddress();
			email.setRecipient(actualEmail);
			hiddenEmail = hideEmail(actualEmail);
			email.sendEmail();
			sent.setText("Sent: " + hiddenEmail);
		}
	}

	private String hideEmail(String emailToHide)
	{
		int indexToContinue=0;
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = 0 ; i<emailToHide.length();i++){
			if(emailToHide.charAt(i+2)=='@'){
				indexToContinue = i;
				break;
			}
			stringBuilder.append("*");
		}
		return stringBuilder.append(emailToHide.substring(indexToContinue)).toString();
	}


	public void getEmailAddress()
	{
		String secureTempPass = new HashPassword("tempPass").toString();
		email = new Email(secureTempPass);
		try {
			setActualEmail(machineModel.getAccountInfo(usernameInput.getText(),"email"));
			machineModel.updateSpecificValueMainDB(usernameInput.getText(),"TempPassword",secureTempPass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//emailInput.setText(hideEmail(actualEmail));
	}

	private void setActualEmail(String email)
	{
		actualEmail = email;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		machineModel = new MachineModel();
	}
}
