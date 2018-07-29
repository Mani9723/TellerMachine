package Machine.Application;


import Machine.AccountManager.CheckingAccount;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
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

import javax.crypto.Mac;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class depositController implements Initializable
{

	@FXML
	private StackPane stackPane, withdrawPane;

	@FXML
	private AnchorPane depositPane, rootPane, secondPane;

	@FXML
	private JFXButton menu;

	@FXML
	private JFXButton withdraw;

	@FXML
	private JFXButton logout;

	@FXML
	private JFXTextField depositInput;

	@FXML
	private JFXButton depositButton;

	@FXML
	private JFXButton clear;

	private String username;

	@FXML
	private Label currBalance;

	private String previousBalance, newBalance;

	private MachineModel machineModel;

//	private CheckingAccount checkingAccount = new CheckingAccount();

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{

	}

	void init(MachineModel machine)
	{
		setModel(machine);
		try {
			setCurrBalance(machineModel.getAccountInfo(username,"CurrentBalance"));
			setPreviousBalance();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void depositEvent(ActionEvent event)
	{
		String request = depositInput.getText();
		if(event.getSource().equals(depositButton)){
			if(isAmountValid(request)){
				executeQuery(request);
				updateBalanceLabel();
			} else{
				new DialogeBox(stackPane).OkButton("Invalid Amount: $"+request,new JFXDialog());
			}
		}
		depositInput.setText("");
	}

	private void updateBalanceLabel()
	{
		setCurrBalance(newBalance);
		setPreviousBalance();
	}

	private void executeQuery(String request)
	{
		try {
			machineModel.updateBalance(newBalance(request),getUser());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		new DialogeBox(stackPane).OkButton("Deposit Amount: $"+request,new JFXDialog());
	}

	private String newBalance(String request)
	{
		Double pBal = Double.parseDouble(previousBalance);
		Double cBal = Double.parseDouble(request);
		setNewBalance(pBal+cBal);
		return newBalance;
	}

	private boolean isAmountValid(String request)
	{
		try{
			Double.parseDouble(request);
			return true;
		}catch (NumberFormatException e){
			return false;
		}
	}

	@FXML
	void clearButton(ActionEvent event)
	{
		if(event.getSource().equals(clear))
			depositInput.setText("");
	}

	@FXML
	void logout(ActionEvent event)
	{
		if(event.getSource().equals(logout)) {
			try {
				rootPane = FXMLLoader.load(getClass().getResource("bankUI.fxml"));
				stackPane.getChildren().setAll(rootPane);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	void menu(ActionEvent event)
	{
		if(event.getSource().equals(menu)) {
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
			controller.setUsername(getUser());
			controller.init(machineModel);
			Stage homeWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
			homeWindow.setScene(currScene);
			homeWindow.show();
		}
	}

	@FXML
	void withdraw(ActionEvent event)
	{
		if(event.getSource().equals(withdraw)) {
			try {
				withdrawPane = FXMLLoader.load(getClass().getResource("withdrawPage.fxml"));
				stackPane.getChildren().setAll(withdrawPane);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	void setUsername(String user)
	{
		username = user;
	}

	private String getUser()
	{
		return username;
	}
	private void setCurrBalance(String balance)
	{
		currBalance.setText("$"+balance);
	}

	void setModel(MachineModel model)
	{
		machineModel = model;
	}

	private void setPreviousBalance()
	{
		previousBalance = currBalance.getText().substring(1);
	}

	public void setNewBalance(Double bal)
	{
		newBalance = Double.toString(bal);
	}
}
