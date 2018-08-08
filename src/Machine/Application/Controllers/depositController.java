package Machine.Application.Controllers;


import Machine.Application.Controllers.Model.MachineModel;
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

	@FXML
	private JFXButton seven;

	@FXML
	private JFXButton three;

	@FXML
	private JFXButton two;

	@FXML
	private JFXButton one;

	@FXML
	private JFXButton six;

	@FXML
	private JFXButton five;

	@FXML
	private JFXButton four;

	@FXML
	private JFXButton nine;

	@FXML
	private JFXButton eight;

	@FXML
	private JFXButton zero;

	@FXML
	private JFXButton decimal;

	private String previousBalance, newBalance;

	private MachineModel machineModel;
	private DialogeBox dialogeBox;

	private LoadScene loadScene;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		loadScene = new LoadScene();
		dialogeBox = new DialogeBox(stackPane);
		dialogeBox.setNonStackPane(depositPane);
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
				dialogeBox.OkButton("Invalid Amount: $"+request,new JFXDialog());
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
		String updatedBalance = getNewBalance(request);
		try {
			machineModel.updateBalanceMainDB(updatedBalance,getUser());
			machineModel.insertToStatementDB(getUser(),"Deposit",request,previousBalance,updatedBalance);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dialogeBox.OkButton("Deposit Amount: $"+request,new JFXDialog());
	}

	private String getNewBalance(String request)
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
		StackPane stackPane1;
		if(event.getSource().equals(logout)) {
			try {
				 stackPane1 = FXMLLoader.load(getClass().getResource("..\\FXMLs\\loginPage.fxml"));
				stackPane.getChildren().setAll(stackPane1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	@FXML
	void menu(ActionEvent event)
	{
		if(event.getSource().equals(menu)) {
			loadScene.setActionEvent(event);
			loadScene.homeSceneAction("..\\FXMLs\\homePage.fxml",getUser(),machineModel);
		}
	}

	@FXML
	void withdraw(ActionEvent event)
	{
		if(event.getSource().equals(withdraw)) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("..\\FXMLs\\withdrawPage.fxml"));
			Parent loginParent = null;
			try {
				loginParent = loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Scene currScene = new Scene(loginParent);
			withdrawController controller = loader.getController();
			controller.setUsername(getUser());
			controller.init(machineModel);
			Stage homeWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
			homeWindow.setScene(currScene);
			homeWindow.show();
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

	@FXML
	public void handleKeyPad(ActionEvent event)
	{
		writeToTextField(depositInput,(JFXButton)event.getSource());
	}

	private void writeToTextField(JFXTextField field, JFXButton button)
	{
		depositInput.setText(field.getText()+button.getText());
	}
}
