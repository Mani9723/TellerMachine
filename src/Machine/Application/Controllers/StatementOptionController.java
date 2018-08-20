package Machine.Application.Controllers;

import Machine.AccountManager.Email;
import Machine.Application.Controllers.Model.MachineModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
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
	private DialogeBox dialogeBox;
	private String username, statementPath;

	void init(String username, MachineModel machineModel)
	{
		dialogeBox = new DialogeBox(optionPane);
		setUsername(username);
		setMachineModel(machineModel);
		statusImage.setVisible(false);
		statusLabel.setVisible(false);
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
					statusImage.setVisible(true);
					statusLabel.setText("S A V E D");
					statusLabel.setVisible(true);
				} catch (SQLException | FileNotFoundException e) {
					e.printStackTrace();
				}
			}
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

	//TODO: Complete this method
	public void handleEmailButton(ActionEvent event)
	{
		Email email = new Email(null);
		if(event.getSource().equals(emailButton)){
			FileChooser fileChooser = new FileChooser();
			fileChooser.showOpenDialog(null);


		}
	}

	private void setPath(String path)
	{
		statementPath = path;
	}
	private File getDestinationFromUser()
	{
		DirectoryChooser fileChooser = new DirectoryChooser();
		File file = fileChooser.showDialog(null);
		if(file != null) {
			return file;
		}
		return null;
	}
}
