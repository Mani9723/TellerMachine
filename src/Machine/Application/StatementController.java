package Machine.Application;

import com.jfoenix.controls.JFXButton;
import com.sun.xml.internal.bind.v2.TODO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
	private Label dateLabel;

	private MachineModel machineModel;

	private String username;

	private ObservableList<StatementData> observableList = FXCollections.observableArrayList();


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
			fillTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void fillTable() throws SQLException
	{
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		String query = "SELECT * from "+ username;
		Connection connection = DatabaseConnect.connector("AccountDB.sqlite");
		try {
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				observableList.add(new StatementData(resultSet.getString("Date"),resultSet.getString("Type"),
						resultSet.getString("Amount"),resultSet.getString("PreviousBalance"),
						resultSet.getString("CurrentBalance")));
			}
			table.setItems(observableList);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			resultSet.close();
			preparedStatement.close();
			connection.close();
		}
	}


	public void handleMenu(ActionEvent event)
	{
		if(event.getSource().equals(menuButton)){
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("homePage.fxml"));
			Parent loginParent = null;
			try {
				loginParent = loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Scene currScene = new Scene(loginParent);
			homeController controller = loader.getController();
			controller.setUsername(username);
			controller.init(machineModel);
			Stage homeWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
			homeWindow.setScene(currScene);
			homeWindow.show();
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
