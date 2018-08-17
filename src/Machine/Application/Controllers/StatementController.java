package Machine.Application.Controllers;

import Machine.Application.Controllers.Model.MachineModel;
import com.jfoenix.controls.JFXButton;
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
import javafx.stage.Stage;


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
	private AnchorPane pane;

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


	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
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
		LoadScene loadScene = new LoadScene();
		if(event.getSource().equals(menuButton)){
			loadScene.setActionEvent(event);
			loadScene.homeSceneAction(username,machineModel);
		}
	}

	@FXML
	public void handleSavePdf()
	{
		try {
			machineModel.saveStatementToPdf(username);
		} catch (SQLException | FileNotFoundException e) {
			e.printStackTrace();
		}
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