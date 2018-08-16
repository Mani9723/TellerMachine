package Machine.Application.Controllers;

import Machine.Application.Controllers.Model.MachineModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
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
import java.text.DecimalFormat;
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
	private DecimalFormat decimalFormat = new DecimalFormat("##.##");
	private String username,previousBalance,newBalance;
	private LoadScene loadScene;
	private DialogeBox dialogeBox;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		dialogeBox = new DialogeBox(stackPane);
		dialogeBox.setNonStackPane(quickPane);
	}

	void init(MachineModel machineModel)
	{
		loadScene = new LoadScene();
		setModel(machineModel);
		try {
			String bal = machineModel.getAccountInfo(getUser(),"CurrentBalance");
			setCurrBalance(decimalFormat.format(Double.parseDouble(bal)));
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

	@FXML
	void clickOther(ActionEvent event)
	{
		loadScene.setActionEvent(event);
		loadScene.withdrawScene(username,machineModel);
	}

	@FXML
	void goToDeposit(ActionEvent event)
	{
		loadScene.setActionEvent(event);
		loadScene.depositPage(username,machineModel);
	}

	@FXML
	void goToExit(ActionEvent event)
	{
		if(event.getSource().equals(exit)) {
			try {
				stackPane2 = FXMLLoader.load(getClass().getResource("/Machine/Application/FXMLs/loginPage.fxml"));
				stackPane.getChildren().setAll(stackPane2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	void goToMenu(ActionEvent event)
	{
		loadScene.setActionEvent(event);
		loadScene.homeSceneAction(username,machineModel);
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
			machineModel.updateBalanceMainDB(updatedBal,getUser());
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

	private void setNewBalance(Double bal)
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
}