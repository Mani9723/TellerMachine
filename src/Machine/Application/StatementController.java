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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
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
	private TableView<MachineModel> table;

	@FXML
	private TableColumn<MachineModel, String> dateCol;

	@FXML
	private TableColumn<MachineModel, String> typeCol;

	@FXML
	private TableColumn<MachineModel, String> amountCol;

	@FXML
	private TableColumn<MachineModel, String> prevCol;

	@FXML
	private TableColumn<MachineModel, String> currCol;

	@FXML
	private Label accountname;

	@FXML
	private JFXButton menuButton;

	@FXML
	private Label dateLabel;

	private MachineModel machineModel;

	private String username;

	ObservableList<MachineModel> observableList = FXCollections.observableArrayList();


	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		dateLabel.setText(getDate());
		//fillTable();
	}

	// TODO Finish Tableview
//	private void fillTable()
//	{
//		Connection connection = DatabaseConnect.connector(username+".sqlite");
//		try {
//			ResultSet resultSet = connection.createStatement().executeQuery("SELECT * from "+username);
//			while(resultSet.next()){
//				observableList.add(resultSet.getString(""))
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}


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
