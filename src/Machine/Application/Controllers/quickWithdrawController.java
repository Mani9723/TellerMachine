package Machine.Application.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
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

public class quickWithdrawController implements Initializable
{

	@FXML
	private StackPane stackPane, stackPane2;

	@FXML
	private AnchorPane quickPane, rootPane;

	@FXML
	private JFXButton twenty;

	@FXML
	private JFXButton forty;

	@FXML
	private JFXButton sixty;

	@FXML
	private JFXButton eighty;

	@FXML
	private JFXButton hundred;

	@FXML
	private JFXButton other;

	@FXML
	private Label balanceLabel;

	@FXML
	private JFXButton mainMenu;

	@FXML
	private JFXButton exit;

	@FXML
	private JFXButton deposit;

	private MachineModel machineModel;

	private String username,previousBalance,newBalance,currBalance;

	private DialogeBox dialogeBox;

	void init(MachineModel machineModel)
	{
		setModel(machineModel);
		try {
			setCurrBalance(machineModel.getAccountInfo(getUser(),"CurrentBalance"));
			setPreviousBalance();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	@FXML
	void cashButtons(ActionEvent event)
	{
		JFXButton request = (JFXButton)event.getSource();
		processRequest(request.getText().substring(1));
	}

	private void processRequest(String request)
	{
		if(validRequest(request)) {
			executeQuery(request);
			updateBalanceLabel();
			dialogeBox.OkButton("Withdraw Amount $" + request, new JFXDialog());
		} else
			dialogeBox.OkButton("Invalid Amount: $"+request,new JFXDialog());
	}

	private boolean validRequest(String request) throws NumberFormatException
	{
		return Double.parseDouble(request) <= Double.parseDouble(previousBalance);
	}

	private void executeQuery(String request)
	{
		String updatedBal = getNewBalance(request);
		try {
			machineModel.updateMainDB(updatedBal,getUser());
			machineModel.insertToStatementDB(getUser(),"Withdrawal",request,previousBalance,updatedBal);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void updateBalanceLabel()
	{
		setCurrBalance(newBalance);
		setPreviousBalance();
	}

	@FXML
	void clickOther(ActionEvent event)
	{
		if(event.getSource().equals(other)) {
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

	@FXML
	void goToDeposit(ActionEvent event)
	{
		if(event.getSource().equals(deposit)) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("..\\FXMLs\\depositPage.fxml"));
			Parent loginParent = null;
			try {
				loginParent = loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Scene currScene = new Scene(loginParent);
			depositController controller = loader.getController();
			controller.setUsername(getUser());
			controller.init(machineModel);
			Stage homeWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
			homeWindow.setScene(currScene);
			homeWindow.show();
		}
	}

	@FXML
	void goToExit(ActionEvent event)
	{
		if(event.getSource().equals(exit)) {
			try {
				rootPane = FXMLLoader.load(getClass().getResource("..\\FXMLs\\loginPage.fxml"));
				stackPane.getChildren().setAll(rootPane);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	void goToMenu(ActionEvent event)
	{
		if(event.getSource().equals(mainMenu)) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("..\\FXMLs\\homePage.fxml"));
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
		balanceLabel.setText("$"+balance);
	}

	void setModel(MachineModel model)
	{
		machineModel = model;
	}

	private void setPreviousBalance()
	{
		previousBalance = balanceLabel.getText().substring(1);
	}

	public void setNewBalance(Double bal)
	{
		newBalance = Double.toString(bal);
	}
	private String getNewBalance(String request)
	{
		Double pBal = Double.parseDouble(previousBalance);
		Double cBal = Double.parseDouble(request);
		setNewBalance(pBal-cBal);
		return newBalance;
	}


	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		dialogeBox = new DialogeBox(stackPane);
		dialogeBox.setNonStackPane(quickPane);
	}
}
