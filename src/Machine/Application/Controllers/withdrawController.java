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
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class withdrawController implements Initializable
{
	private DecimalFormat df = new DecimalFormat("##.##");

	@FXML
	private StackPane stackPane, stackPane2;

	@FXML
	private AnchorPane withdrawPane;

	@FXML
	private JFXButton seven;

	@FXML
	private JFXButton eight;

	@FXML
	private JFXButton nine;

	@FXML
	private JFXButton four;

	@FXML
	private JFXButton five;

	@FXML
	private JFXButton six;

	@FXML
	private JFXButton one;

	@FXML
	private JFXButton two;

	@FXML
	private JFXButton three;

	@FXML
	private JFXButton zero;

	@FXML
	private JFXButton decimal;

	@FXML
	private JFXButton withdrawButton;

	@FXML
	private JFXButton reset;

	@FXML
	private Label date;

	@FXML
	private Label balanceLabel;

	@FXML
	private JFXTextField moneyTextField;

	@FXML
	private JFXButton mainMenu;

	@FXML
	private JFXButton LOGOUT;

	@FXML
	private JFXButton qCash;
	private AnchorPane rootPane;
	private int count = 1;
	private String username;
	private String previousBalance;
	private String currBalance;
	private MachineModel machineModel;
	private String newBalance;
	private DialogeBox dialogeBox;
	private LoadScene loadScene;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		loadScene = new LoadScene();
		dialogeBox = new DialogeBox(stackPane);
		dialogeBox.setNonStackPane(withdrawPane);
		moneyTextField.setEditable(false);
	}

	void init(MachineModel machineModel)
	{
		setMachine(machineModel);

		try {
			setCurrBalance(machineModel.getAccountInfo(username,"CurrentBalance"));
			setPreviousBalance(getCurrBalance());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void clear(ActionEvent event)
	{
		if(event.getSource().equals(reset)){
			moneyTextField.setText("");
		}
		resetCount();
	}

	@FXML
	void keypadPressed(ActionEvent event)
	{
		writeToField(moneyTextField,(JFXButton)event.getSource());
		if(event.getSource()==decimal){
			++count;
			if(count>1) { decimal.setDisable(true);}
		}
	}
	private void writeToField(JFXTextField field, JFXButton button)
	{
		moneyTextField.setText(field.getText()+button.getText());
	}

	@FXML
	void logOut(ActionEvent event)
	{
		if(event.getSource().equals(LOGOUT)) {
			try {
				stackPane2 = FXMLLoader.load(getClass().getResource("..\\FXMLs\\loginPage.fxml"));
				stackPane.getChildren().setAll(stackPane2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	void menu(ActionEvent event)
	{
		if(event.getSource().equals(mainMenu)) {
			loadScene.setActionEvent(event);
			loadScene.homeSceneAction("..\\FXMLs\\homePage.fxml",username,machineModel);
		}
	}

	@FXML
	void qkCash(ActionEvent event)
	{
		if(event.getSource().equals(qCash)) {
			loadScene.setActionEvent(event);
			loadScene.quickCashScene("..\\FXMLs\\QuickWithdraw.fxml",username,machineModel);
		}
	}

	@FXML
	void withdraw(ActionEvent event)
	{
		String request = moneyTextField.getText();
		if(event.getSource().equals(withdrawButton)){
			if(validRequest(request) && isWithinBounds(request)){
				executeQuery(request);
				updateBalanceLabel();
			}else{
				dialogeBox.OkButton("Invalid Amount: $"+request,new JFXDialog());
			}
		}
		resetCount();
		moneyTextField.setText("");
	}

	private void resetCount()
	{
		count = 1;
		decimal.setDisable(false);
	}

	private boolean validRequest(String request)
	{
		try{
			Double.parseDouble(request);
			return true;
		}catch (NumberFormatException e){
			return false;
		}
	}
	private boolean isWithinBounds(String request)
	{
		double bal = Double.parseDouble(request);
		return bal <= Double.parseDouble(previousBalance);
	}

	private void executeQuery(String request)
	{
		String updatedBal = getNewBalance(request);
		try {
			machineModel.updateBalanceMainDB(updatedBal,username);
			machineModel.insertToStatementDB(username,"Withdrawal",request,previousBalance,updatedBal);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dialogeBox.OkButton("Withdraw Amount: $"+request,new JFXDialog());
	}

	private void updateBalanceLabel()
	{
		setCurrBalance(newBalance);
		setPreviousBalance(currBalance);
	}

	void setUsername(String user)
	{
		username = user;
	}

	void setMachine(MachineModel model)
	{
		machineModel = model;
	}

	private String getCurrBalance()
	{
		return currBalance;
	}

	private void setPreviousBalance(String previousBalance)
	{
		this.previousBalance = balanceLabel.getText().substring(1);
	}

	private void setCurrBalance(String currBalance)
	{
		balanceLabel.setText("$"+currBalance);
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
