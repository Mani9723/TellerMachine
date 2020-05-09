package Machine.Application.Controllers;

import Machine.Application.Controllers.Model.DatabaseModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.events.JFXDrawerEvent;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class StatementController implements Initializable
{

	@FXML
	private StackPane stackPane, optionScene;
	@FXML
	private AnchorPane pane;
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
	private Label dateLabel;


	private DatabaseModel databaseModel;
	private String username;

	private Transition transition;


	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		stackPane.setOpacity(0);
		transition = new Transition(stackPane, null);
		DialogeBox dialogeBox = new DialogeBox(stackPane);
		dialogeBox.setNonStackPane(pane);
		dateLabel.setText(getDate());
		dateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
		amountCol.setCellValueFactory(new PropertyValueFactory<>("Amount"));
		prevCol.setCellValueFactory(new PropertyValueFactory<>("prevBal"));
		currCol.setCellValueFactory(new PropertyValueFactory<>("currBal"));
		stackPane.requestFocus();
		transition.fadeIn();
	}

	void init()
	{
		table.setItems(databaseModel.getStatement(username));
		ObservableList<StatementData> list = table.getItems();
		if(list.isEmpty()){
			table.setOpacity(0.0);
		}

	}

	@FXML
	public void handleMenu(ActionEvent event)
	{
		AtomicBoolean isDrawerClosing = new AtomicBoolean(false);
		if(event.getSource().equals(menuButton)){
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass()
						.getResource("/Machine/Application/FXMLs/StatementOptions.fxml"));
				optionScene = loader.load();
				StatementOptionController controller = loader.getController();
				controller.init(username, databaseModel,stackPane);
				drawerPane.setSidePane(optionScene);
				drawerPane.toFront();
				drawerPane.setOnDrawerClosed((JFXDrawerEvent drawerEvent)->{
					if(!isDrawerClosing.get()){
						drawerPane.toBack();
						drawerPane.close();
						if(table.getOpacity() != 0) {
							setTableOpacity(0.78);
						}
						menuButton.setText("Options");
						isDrawerClosing.set(true);
					}
				});

				if (drawerPane.isShown()) {
					drawerPane.toBack();
					drawerPane.close();
					if(table.getOpacity() != 0) {
						setTableOpacity(0.78);
					}
					menuButton.setText("Options");
				} else {
					drawerPane.open();
					setTableOpacity(table.getOpacity() == 0 ? 0.0 : 0.2);
					menuButton.setText("Return");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	private void setTableOpacity(double tableOpacity)
	{
		table.setOpacity(tableOpacity);
	}

	void setDatabaseModel(DatabaseModel model)
	{
		databaseModel = model;
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