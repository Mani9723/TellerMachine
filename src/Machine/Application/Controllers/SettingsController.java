package Machine.Application.Controllers;

import Machine.AccountManager.HashPassword;
import Machine.Application.Controllers.Model.MachineModel;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.awt.event.KeyEvent;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class SettingsController implements Initializable
{

	@FXML
	private JFXButton confirmButton, menuButton, delAccount, confDel, cancel;

	@FXML
	private StackPane stackPane;

	@FXML
	private Label firstname;

	@FXML
	private AnchorPane anchor;

	@FXML
	private Label lastName;

	@FXML
	private Label username;

	@FXML
	private Label email;

	@FXML
	private JFXPasswordField currPass;

	@FXML
	private JFXPasswordField newPass;

	@FXML
	private JFXPasswordField confnewPass;

	@FXML
	private JFXPasswordField reenterPassword;

	@FXML
	private Label date;

	private MachineModel machineModel;
	private LoadScene loadScene;
	private DialogeBox dialogeBox;
	private HashPassword hashPassword;
	private GaussianBlur gaussianBlur;


	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		gaussianBlur = new GaussianBlur();
		loadScene = new LoadScene();
		dialogeBox = new DialogeBox(stackPane);
		dialogeBox.setNonStackPane(anchor);
		date.setText(getDate());
		date.setTextFill(Color.valueOf("white"));
		gaussianBlur.setRadius(7.5);
		confirmButton.setDisable(true);
		cancel.setVisible(false);
		confDel.setVisible(false);
		reenterPassword.setVisible(false);
	}

	void init(String user, MachineModel machine)
	{
		machineModel = machine;
		try {
			firstname.setText(machineModel.getAccountInfo(user,"Firstname"));
			username.setText(user);
			lastName.setText(machineModel.getAccountInfo(user,"Lastname"));
			email.setText(machineModel.getAccountInfo(user,"Email"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void handleDelButton(ActionEvent event)
	{
		if(event.getSource().equals(delAccount)){
			reenterPassword.setVisible(true);
			confDel.setVisible(true);
			cancel.setVisible(true);
			currPass.setDisable(true);
			newPass.setDisable(true);
			confnewPass.setDisable(true);
		}
	}

	public void confirmDelButton(ActionEvent event)
	{
		if(event.getSource().equals(confDel) && reenterPassword.getText().length()>=8){
			String pass = reenterPassword.getText();
			HashPassword hashPassword = new HashPassword(pass);
			try{
				if(machineModel.getAccountInfo(username.getText(),"Password").equals(hashPassword.toString())){
					machineModel.deleteUser(username.getText());
					loadScene = new LoadScene(stackPane,new StackPane());
					loadScene.loginPage();
					dialogeBox.OkButton("Thank you for you being a customer.\nYour account is deleted",
							new JFXDialog());
				}else{
					dialogeBox.OkButton("Incorrect Password", new JFXDialog());
					reenterPassword.setText("");
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
		}else{
			dialogeBox.OkButton("Please Enter Password", new JFXDialog());
		}
	}

	public void cancelDelButton(ActionEvent event)
	{
		if(event.getSource().equals(cancel)){
			reenterPassword.setVisible(false);
			reenterPassword.setText("");
			confDel.setVisible(false);
			cancel.setVisible(false);
			currPass.setDisable(false);
			newPass.setDisable(false);
			confnewPass.setDisable(false);
		}
	}

	public void loadHomeScene(ActionEvent event)
	{
		if(event.getSource().equals(menuButton)){
			loadScene.setActionEvent(event);
			loadScene.homeSceneAction(username.getText(),machineModel);
		}
	}

	public void handlePassChange(ActionEvent event)
	{
		if(event.getSource().equals(confirmButton)){
			if(isCurrentValid()) {
				if (validatePass()) {
					hashPassword = new HashPassword(newPass.getText());
					try {
						machineModel.updateNewPassword(username.getText(), hashPassword.toString());
					} catch (SQLException e) {
						e.printStackTrace();
					}
					dialogeBox.OkButton("Password Changed", new JFXDialog());
					confnewPass.setText(""); newPass.setText(""); confnewPass.setText("");
				} else {
					dialogeBox.OkButton("Password does not match", new JFXDialog());
					newPass.setText("");confnewPass.setText("");
				}
			}else{
				dialogeBox.OkButton("Original Password is incorrect", new JFXDialog());
				currPass.setText("");
			}
		}
	}

	private boolean isCurrentValid()
	{
		hashPassword = new HashPassword(currPass.getText());
		String encryptedPass = hashPassword.toString();
		try {
			return encryptedPass.equals(machineModel.getAccountInfo(username.getText(),"Password"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean validatePass()
	{
		return newPass.getText().equals(confnewPass.getText())
				&& newPass.getText().length() >=8 && confnewPass.getText().length() >=8;
	}

	private String getDate()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		return dateFormat.format(date);
	}

	@FXML
	public void handleButtonEnable()
	{
		String pass = confnewPass.getText();
		if (pass.length() >= 8)
			confirmButton.setDisable(false);
		else
			confirmButton.setDisable(true);
	}
}
