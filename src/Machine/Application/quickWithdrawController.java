package Machine.Application;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class quickWithdrawController {

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
		String request = "";
		if(event.getSource().equals(twenty)){
			request = twenty.getText();
		}else if(event.getSource().equals(forty)){
			request = forty.getText();
		}else if(event.getSource().equals(sixty)){
			request = sixty.getText();
		}else if(event.getSource().equals(eighty)){
			request = eighty.getText();
		}else if(event.getSource().equals(hundred)){
			request = hundred.getText();
		}
		processRequest(request.substring(1));
	}

	private void processRequest(String request)
	{
		if(validRequest(request)) {
			executeQuery(request);
			updateBalanceLabel();
			new DialogeBox(stackPane).OkButton("Withdraw Amount $" + request, new JFXDialog());
		} else
			new DialogeBox(stackPane).OkButton("Invalid Amount: $"+request,new JFXDialog());
	}

	private boolean validRequest(String request)
	{
		return Double.parseDouble(request) <= Double.parseDouble(previousBalance);
	}

	private void executeQuery(String request)
	{
		try {
			machineModel.updateBalance(newBalance(request),getUser());
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
			loader.setLocation(getClass().getResource("withdrawPage.fxml"));
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
			loader.setLocation(getClass().getResource("depositPage.fxml"));
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
				rootPane = FXMLLoader.load(getClass().getResource("bankUI.fxml"));
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
	private String newBalance(String request)
	{
		Double pBal = Double.parseDouble(previousBalance);
		Double cBal = Double.parseDouble(request);
		setNewBalance(pBal-cBal);
		return newBalance;
	}



}
