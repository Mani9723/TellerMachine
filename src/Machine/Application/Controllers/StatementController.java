package Machine.Application.Controllers;

import Machine.Application.Controllers.Model.MachineModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDrawer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class StatementController implements Initializable
{

	@FXML
	private StackPane stackPane, optionScene;
	@FXML
	private AnchorPane pane, optionPane;
	@FXML
	private JFXDrawer drawerPane;

	@FXML
	private TableView<StatementData> table;

	@FXML
	private TableColumn<StatementData, String> dateCol;

	@FXML
	private TableColumn<StatementData, String> typeCol;

	@FXML
	private TableColumn<StatementData, String> amountCol;

	@FXML
	private TableColumn<StatementData, String> prevCol;

	@FXML
	private TableColumn<StatementData, String> currCol;

	@FXML
	private Label accountname;

	@FXML
	private JFXButton menuButton;

	@FXML
	private MenuItem saveButton, emailButton;

	@FXML
	private Label dateLabel;

	private MachineModel machineModel;
	private String username;
	private DialogeBox dialogeBox;


	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		dialogeBox = new DialogeBox(stackPane);
		dialogeBox.setNonStackPane(pane);
		dateLabel.setText(getDate());
		dateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
		amountCol.setCellValueFactory(new PropertyValueFactory<>("Amount"));
		prevCol.setCellValueFactory(new PropertyValueFactory<>("prevBal"));
		currCol.setCellValueFactory(new PropertyValueFactory<>("currBal"));
	}

	void init()
	{
		try {
			table.setItems(machineModel.getStatement(username));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleMenu(ActionEvent event)
	{
		if(event.getSource().equals(menuButton)){
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/Machine/Application/FXMLs/StatementOptions.fxml"));
				optionScene = loader.load();
				StatementOptionController controller = loader.getController();
				controller.init(username,machineModel);
				drawerPane.setSidePane(optionScene);

				if (drawerPane.isShown()) {
					drawerPane.close();
					table.setOpacity(0.78);
				} else {
					drawerPane.open();
					table.setOpacity(0.2);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	@FXML
	public void handleSavePdf()
	{
		File file = getDestinationFromUser();
		try {
			if(file != null) {
				String path = machineModel.saveStatementToPdf(username, file);
				dialogeBox.OkButton("Statement saved:"+path, new JFXDialog());
			}else dialogeBox.OkButton("Please Choose a location", new JFXDialog());
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

	void setMachineModel(MachineModel model)
	{
		machineModel = model;
	}

	void setUsername(String user)
	{
		username = user;
	}


	private String getDate()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		return dateFormat.format(date);
	}


}