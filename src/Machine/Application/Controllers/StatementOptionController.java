package Machine.Application.Controllers;

import Machine.AccountManager.Email;
import Machine.Application.Controllers.Model.DatabaseModel;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;

import javafx.event.ActionEvent;
import javafx.stage.FileChooser;

import javax.swing.*;
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

	@FXML
	private StackPane parentPane;

	private DatabaseModel databaseModel;
	private String username;

	void init(String username, DatabaseModel databaseModel, StackPane stackPane)
	{
		setUsername(username);
		setDatabaseModel(databaseModel);
		showStatus(false,"");
		printButton.setDisable(true);
		//emailButton.setDisable(true);
		parentPane = stackPane;
		optionPane.requestFocus();
	}

	private void setUsername(String user)
	{
		username = user;
	}
	private void setDatabaseModel(DatabaseModel model)
	{
		databaseModel = model;
	}


	public void handleSavePdf(ActionEvent event)
	{
		if(event.getSource().equals(saveButton)) {
			File file = getDestinationFromUser();
			if(file != null) {
				//setPath(file.getPath());
				try {
					databaseModel.saveStatementToPdf(username, file,"123");
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
			new Transition(parentPane, null).fadeOut().setOnFinished((ActionEvent evt) ->{
				loadScene.setActionEvent(event);
				loadScene.homeSceneAction(username, databaseModel);
			});
		}
	}

	public void handleEmailButton(ActionEvent event)
	{
		Email email = new Email(true);
		if(event.getSource().equals(emailButton)){
			try {
				email.setRecipient(databaseModel.getAccountInfo(username,"Email"));
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
					Thread emailThread = new Thread(email);
					emailThread.start();
					//showStatus(true,"SENDING...");
					showStatus(true,"S E N T");
					emailButton.setDisable(true);
				} catch (RuntimeException e) {
					showStatus(true,"No Internet");
				}
			}catch (NullPointerException e){
				showStatus(true,"Choose File");
			}
		}
	}

	private File getDestinationFromUser()
	{
		DirectoryChooser fileChooser = new DirectoryChooser();
		fileChooser.setTitle("Select Location");
		return fileChooser.showDialog(null);
	}

	private void showStatus(boolean visible, String message)
	{
		statusImage.setVisible(visible);
		statusLabel.setText(message);
		statusLabel.setVisible(visible);
	}
}
