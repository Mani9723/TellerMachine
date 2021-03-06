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

public class withdrawController implements Initializable
{

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

	private DecimalFormat decimalFormat = new DecimalFormat("##.##");
	private int count = 1;
	private String username;
	private String previousBalance;
	private double currBalance;
	private DatabaseModel databaseModel;
	private String newBalance;
	private DialogeBox dialogeBox;
	private LoadScene loadScene;

	private Transition transition;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		stackPane.setOpacity(0);
		transition = new Transition(stackPane,null);
		loadScene = new LoadScene();
		dialogeBox = new DialogeBox(stackPane);
		dialogeBox.setNonStackPane(withdrawPane);
		moneyTextField.setEditable(false);
		moneyTextField.requestFocus();
		transition.fadeIn();
	}

	void init(DatabaseModel databaseModel)
	{
		setMachine(databaseModel);

		try {
			setCurrBalance(databaseModel.getAccountInfo(username,"CurrentBalance"));
			setPreviousBalance();
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


	@FXML
	void logOut(ActionEvent event)
	{
		if(event.getSource().equals(LOGOUT)) {
			transition.fadeOut().setOnFinished((ActionEvent evt) ->{
				try {
					stackPane2 = FXMLLoader.load(getClass().getResource("/Machine/Application/FXMLs/loginPage.fxml"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				stackPane.getChildren().setAll(stackPane2);
				stackPane.setOpacity(1);
			});
		}
	}

	@FXML
	void menu(ActionEvent event)
	{
		if(event.getSource().equals(mainMenu)) {
			transition.fadeOut().setOnFinished((ActionEvent evt) ->{
				loadScene.setActionEvent(event);
				loadScene.homeSceneAction(username, databaseModel);
			});
		}
	}

	@FXML
	void qkCash(ActionEvent event)
	{
		if(event.getSource().equals(qCash)) {
			transition.fadeOut().setOnFinished((ActionEvent evt) ->{
				loadScene.setActionEvent(event);
				loadScene.quickCashScene(username, databaseModel);
			});
		}
	}

	@FXML
	void withdraw(ActionEvent event)
	{
		String request = moneyTextField.getText();
		if(event.getSource().equals(withdrawButton)){
			if(validRequest(request) && isWithinBounds(request)){
				request = Double.toString(Double.parseDouble(request));
				if(getCurrBalance() >= 100) {
					executeQuery(request);
					updateBalanceLabel();
				}else{
					dialogeBox.OkButton("BALANCE LOW, Please make a deposit", new JFXDialog());
				}
			}else{
				dialogeBox.OkButton("Invalid Amount: $"+request,new JFXDialog());
			}
		}
		resetCount();
		moneyTextField.setText("");
	}

	private void writeToField(JFXTextField field, JFXButton button)
	{
		moneyTextField.setText(field.getText()+button.getText());
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
			databaseModel.updateBalanceMainDB(updatedBal,username);
			databaseModel.insertToStatementDB(username,"Withdrawal",request,previousBalance,updatedBal);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dialogeBox.OkButton("Withdraw Amount: $"+Double.parseDouble(request),new JFXDialog());
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

	void setMachine(DatabaseModel model)
	{
		databaseModel = model;
	}

	private double getCurrBalance()
	{
		return currBalance;
	}

	private void setPreviousBalance()
	{
		this.previousBalance = balanceLabel.getText().substring(1);
	}

	private void setCurrBalance(String currBalance)
	{
		this.currBalance = Double.parseDouble(currBalance);
		balanceLabel.setText("$"+decimalFormat.format(Double.parseDouble(currBalance)));
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
