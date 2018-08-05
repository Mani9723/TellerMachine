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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class homeController implements Initializable
{

	@FXML
	private AnchorPane secondPane, rootPane;

	@FXML
	private StackPane stackPane;

	@FXML
	private JFXButton quickCash;

	@FXML
	private JFXButton deposit;

	@FXML
	private JFXButton withdraw;

	@FXML
	private JFXButton exitHome, statementButton;

	@FXML
	private Label greeting;

	@FXML
	private Label dateTime;

	@FXML
	private Label accountType;

	@FXML
	private Label balanceLabel;

	@FXML
	private Label accountName;

	@FXML
	private Label currBalance;

	private String uName;
	private String first;

	private MachineModel machineModel;
	private LoadScene loadScene;



	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		loadScene = new LoadScene();
		dateTime.setText(getDate());
		accountType.setText("Checking");
	}

	@FXML
	void depositMoney(ActionEvent event)
	{
		if(event.getSource().equals(deposit)){
			loadDepositPage(event);
		}
	}

	private void loadDepositPage(ActionEvent event)
	{
		loadScene.setActionEvent(event);
		loadScene.depositPage("..\\FXMLs\\depositPage.fxml",getuName(),machineModel);
	}

	@FXML
	void exitToHome(ActionEvent event)
	{
		if(event.getSource().equals(exitHome)){
			try {
				stackPane = FXMLLoader.load(getClass().getResource("..\\FXMLs\\loginPage.fxml"));
				secondPane.getChildren().setAll(stackPane);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	void handleStatementButton(ActionEvent event)
	{
		loadScene.setActionEvent(event);
		loadScene.statementScene("..\\FXMLs\\statementPage.fxml",getuName(),machineModel);

	}

	@FXML
	void withdrawMoney(ActionEvent event)
	{
		loadScene.setActionEvent(event);
		loadScene.withdrawScene("..\\FXMLs\\withdrawPage.fxml",getuName(),machineModel);
	}

	@FXML
	void quickCash(ActionEvent event)
	{
		loadScene.setActionEvent(event);
		loadScene.quickCashScene("..\\FXMLs\\QuickWithdraw.fxml",uName,machineModel);
	}

	void init(MachineModel machine)
	{
		setMachineModel(machine);
		try {
			setFirstName(machineModel.getAccountInfo(getuName(),"FirstName"));
			greeting.setText(getAppropriateGreeting(getHour()) +", " + getFirst());
			currBalance.setText("$"+machineModel.getAccountInfo(getuName(),"CurrentBalance"));
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void setFirstName(String name)
	{
		first = name;
	}

	private String getFirst()
	{
		return first;
	}
	void setUsername(String name)
	{
		uName = name;
	}

	private void setMachineModel(MachineModel model)
	{
		machineModel = model;
	}
	private String getuName()
	{
		return uName;
	}

	private String getDate()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		return dateFormat.format(date);
	}
	private int getHour()
	{
		SimpleDateFormat hourformat = new SimpleDateFormat("HH");
		Date date = new Date();
		return Integer.parseInt(hourformat.format(date));
	}
	private String getAppropriateGreeting(int hour)
	{
		if(hour>=0 && hour<12){
			return "Good Morning ";
		} else if(hour>=12 && hour<16){
			return "Good Afternoon ";
		} else{
			return "Good Evening ";
		}
	}

}
