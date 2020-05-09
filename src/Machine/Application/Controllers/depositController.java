package Machine.Application.Controllers;


import Machine.Application.Controllers.Model.DatabaseModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
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

public class depositController implements Initializable
{

	@FXML
	private StackPane stackPane;

	@FXML
	private AnchorPane depositPane;

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
	private JFXButton decimal;

	private DecimalFormat decimalFormat = new DecimalFormat("##.##");

	private String previousBalance, newBalance;
	private DatabaseModel databaseModel;
	private DialogeBox dialogeBox;
	private static int count;
	private LoadScene loadScene;

	private Transition transition;

	public static int getCount()
	{
		return count;
	}

	public static void setCount(int count)
	{
		depositController.count = count;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		stackPane.setOpacity(0);
		transition = new Transition(stackPane, null);
		loadScene = new LoadScene();
		dialogeBox = new DialogeBox(stackPane);
		dialogeBox.setNonStackPane(depositPane);
		setCount(1);
		stackPane.requestFocus();
		transition.fadeIn();
	}

	void init(DatabaseModel machine)
	{
		setModel(machine);
		try {
			setCurrBalance(databaseModel.getAccountInfo(username,"CurrentBalance"));
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
				request = Double.toString(Double.parseDouble(request));
				executeQuery(request);
				updateBalanceLabel();
			} else{
				dialogeBox.OkButton("Invalid Amount: $"+(request.isEmpty() ? "0" : request),new JFXDialog());
			}
		}
		depositInput.setText("");
		if(getCount() > 1) resetCount();
	}

	@FXML
	void clearButton(ActionEvent event)
	{
		if(event.getSource().equals(clear))
			depositInput.setText("");
		resetCount();
	}

	@FXML
	void logout(ActionEvent event)
	{
		if(event.getSource().equals(logout)) {
			transition.fadeOut().setOnFinished((ActionEvent evt) ->{
				StackPane stackPane1 = null;
				try {
					stackPane1 = FXMLLoader.load(getClass().getResource("/Machine/Application/FXMLs/loginPage.fxml"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				stackPane.getChildren().setAll(stackPane1);
				stackPane.setOpacity(1);
			});
		}
	}

	@FXML
	void menu(ActionEvent event)
	{
		if(event.getSource().equals(menu)) {
			transition.fadeOut().setOnFinished((ActionEvent evt) ->{
				loadScene.setActionEvent(event);
				loadScene.homeSceneAction(getUser(), databaseModel);
			});
		}
	}

	@FXML
	void withdraw(ActionEvent event)
	{
		if(event.getSource().equals(withdraw)) {
			transition.fadeOut().setOnFinished((ActionEvent evt) ->{
				loadScene.setActionEvent(event);
				loadScene.withdrawScene(getUser(), databaseModel);
			});
		}
	}

	@FXML
	public void handleKeyPad(ActionEvent event)
	{
		writeToTextField(depositInput,(JFXButton)event.getSource());
		if(event.getSource().equals(decimal)){
			++count;
			decimal.setDisable(true);
		}
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
			databaseModel.updateBalanceMainDB(updatedBalance,getUser());
			databaseModel.insertToStatementDB(getUser(),"Deposit",request,previousBalance,updatedBalance);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dialogeBox.OkButton("Deposit Amount: $"+Double.parseDouble(request),new JFXDialog());
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

	private void resetCount()
	{
		--count;
		decimal.setDisable(false);
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
		currBalance.setText("$"+decimalFormat.format(Double.parseDouble(balance)));
	}

	void setModel(DatabaseModel model)
	{
		databaseModel = model;
	}

	private void setPreviousBalance()
	{
		previousBalance = currBalance.getText().substring(1);
	}

	private void setNewBalance(Double bal)
	{
		newBalance = Double.toString(bal);
	}

	private void writeToTextField(JFXTextField field, JFXButton button)
	{
		depositInput.setText(field.getText()+button.getText());
	}
}
