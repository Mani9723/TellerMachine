package Machine.Application.Controllers;

import Machine.Application.Controllers.Model.MachineModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;

import javax.crypto.Mac;
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

	private MachineModel machineModel;
	private DialogeBox dialogeBox;
	private String username;

	public void init(String username, MachineModel machineModel)
	{
		dialogeBox = new DialogeBox(new StackPane());
		setUsername(username);
		setMachineModel(machineModel);
	}

	private void setUsername(String user)
	{
		username = user;
	}
	private void setMachineModel(MachineModel model)
	{
		machineModel = model;
	}

	//FIXEME Null pointer at line 59. And somehow use the init method

	@FXML
	public void handleSavePdf()
	{
		File file = getDestinationFromUser();
		try {
			if(file != null) {
				String path = machineModel.saveStatementToPdf(username, file);
				dialogeBox.drawerOkButton("Statement saved:"+path, new JFXDialog());
			}else dialogeBox.drawerOkButton("Please Choose a location", new JFXDialog());
		} catch (SQLException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private File getDestinationFromUser()
	{
		DirectoryChooser fileChooser = new DirectoryChooser();
		File file = fileChooser.showDialog(null);
		if(file != null) {
			return file;
		}else{
			dialogeBox.OkButton("Invalid File", new JFXDialog());
		}
		return null;
	}


}
