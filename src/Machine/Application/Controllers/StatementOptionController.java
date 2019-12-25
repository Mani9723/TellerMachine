package Machine.Application.Controllers;

import Machine.AccountManager.Email;
import Machine.Application.Controllers.Model.MachineModel;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;

import javafx.event.ActionEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;

public class StatementOptionController {

	@FXML
	private StackPane optionPane;

	@FXML
	private JFXButton homeButton;

	@FXML
	private JFXButton saveButton;

	@FXML
	private JFXButton emailButton;

	@FXML
	private JFXButton printButton;

	@FXML
	private ImageView statusImage;

	@FXML
	private Label statusLabel;

	private MachineModel machineModel;
	private String username, statementPath;

	void init(String username, MachineModel machineModel)
	{
		setUsername(username);
		setMachineModel(machineModel);
		showStatus(false,"");
		printButton.setDisable(true);
		//emailButton.setDisable(true);

		optionPane.requestFocus();
	}

	private void setUsername(String user)
	{
		username = user;
	}
	private void setMachineModel(MachineModel model)
	{
		machineModel = model;
	}


	public void handleSavePdf(ActionEvent event)
	{
		if(event.getSource().equals(saveButton)) {
			File file = getDestinationFromUser();
			if(file != null) {
				setPath(file.getPath());
				try {
					machineModel.saveStatementToPdf(username, file);
					showStatus(true,"S A V E D");
				} catch (SQLException | FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			emailButton.setDisable(false);
		}
	}

	public void handleHomeButton(ActionEvent event)
	{
		LoadScene loadScene = new LoadScene();
		if(event.getSource().equals(homeButton)){
			loadScene.setActionEvent(event);
			loadScene.homeSceneAction(username,machineModel);
		}
	}

	public void handleEmailButton(ActionEvent event)
	{
		Email email = new Email(true);
		if(event.getSource().equals(emailButton)){
			try {
				email.setRecipient(machineModel.getAccountInfo(username,"Email"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Choose Statement");
			File file = fileChooser.showOpenDialog(null);
			try {
				email.setFilePath(file.getPath());
				email.setFileName(file.getName());
				try {
					showStatus(true,"SENDING...");
					email.run();
					email.start();
					//showStatus(true,"SENDING...");
					email.join();
					showStatus(true,"S E N T");
					emailButton.setDisable(true);
				} catch (RuntimeException e) {
					showStatus(true,"No Internet");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}catch (NullPointerException e){
				showStatus(true,"Choose File");
			}
		}
	}

	private void setPath(String path)
	{
		statementPath = path;
	}
	private File getDestinationFromUser()
	{
		DirectoryChooser fileChooser = new DirectoryChooser();
		fileChooser.setTitle("Select Location");
		File file = fileChooser.showDialog(null);
		if(file != null) {
			return file;
		}
		return null;
	}

	private void showStatus(boolean visible, String message)
	{
		statusImage.setVisible(visible);
		statusLabel.setText(message);
		statusLabel.setVisible(visible);
	}
}
