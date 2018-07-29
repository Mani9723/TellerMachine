package Machine.Application;

import Machine.AccountManager.CheckingAccount;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

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
	private JFXButton exitHome;

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

	private MachineModel machineModel;


	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		dateTime.setText(getDate());
		greeting.setText(getAppropriateGreeting(getHour()));
		accountType.setText("Checking");
	}

	@FXML
	void depositMoney(ActionEvent event)
	{
		if(event.getSource().equals(deposit)){
			try {
				stackPane = FXMLLoader.load(getClass().getResource("depositPage.fxml"));
				secondPane.getChildren().setAll(stackPane);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	@FXML
	void exitToHome(ActionEvent event)
	{
		if(event.getSource().equals(exitHome)){
			try {
				rootPane = FXMLLoader.load(getClass().getResource("bankUI.fxml"));
				secondPane.getChildren().setAll(rootPane);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	void withdrawMoney(ActionEvent event)
	{
		if(event.getSource().equals(withdraw)) {
			try {
				stackPane = FXMLLoader.load(getClass().getResource("withdrawPage.fxml"));
				secondPane.getChildren().setAll(stackPane);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	void quickCash(ActionEvent event)
	{
		if(event.getSource().equals(quickCash)) {
			try {
				stackPane = FXMLLoader.load(getClass().getResource("QuickWithdraw.fxml"));
				secondPane.getChildren().setAll(stackPane);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	void init(MachineModel machine)
	{
		setMachineModel(machine);
		try {
			accountName.setText(machineModel.getAccountInfo(getuName(),"Firstname"));
			currBalance.setText("$"+machineModel.getAccountInfo(getuName(),"CurrentBalance"));
		} catch (SQLException e) {
			e.printStackTrace();
		}

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
