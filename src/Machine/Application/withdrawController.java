package Machine.Application;

import Machine.AccountManager.CheckingAccount;
import com.jfoenix.controls.JFXButton;
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
	private AnchorPane rootPane;
	private int count = 1;


	@FXML
	void clear(ActionEvent event)
	{
		if(event.getSource().equals(reset)){
			moneyTextField.setText("");
		}
	}

	@FXML
	void keypadPressed(ActionEvent event)
	{
		System.out.println("Pressing Key");
		if(event.getSource()==zero){
			writeToField(moneyTextField,zero);
		}
		else if(event.getSource()==one){
			writeToField(moneyTextField,one);
		}
		else if(event.getSource()==two){
			writeToField(moneyTextField,two);
		}
		else if(event.getSource()==three){
			writeToField(moneyTextField,three);
		}
		else if(event.getSource()==four){
			writeToField(moneyTextField,four);
		}
		else if(event.getSource()==five){
			writeToField(moneyTextField,five);
		}
		else if(event.getSource()==six){
			writeToField(moneyTextField,six);
		}
		else if(event.getSource()==seven){
			writeToField(moneyTextField,seven);
		}
		else if(event.getSource()==eight){
			writeToField(moneyTextField,eight);
		}
		else if(event.getSource()==nine){
			writeToField(moneyTextField,nine);
		}
		else if(event.getSource()==decimal){
			writeToField(moneyTextField,decimal);
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
		if(event.getSource().equals(mainMenu)) {
			try {
				rootPane = FXMLLoader.load(getClass().getResource("homePage.fxml"));
				stackPane.getChildren().setAll(rootPane);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	void qkCash(ActionEvent event)
	{
		if(event.getSource().equals(qCash)) {
			try {
				stackPane2 = FXMLLoader.load(getClass().getResource("QuickWithdraw.fxml"));
				stackPane.getChildren().setAll(stackPane2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	void withdraw(ActionEvent event)
	{
		//****** VALIDATE INPUT *******
		if(event.getSource().equals(withdrawButton)){
		//	checkingAccount.withdraw(Double.parseDouble(moneyTextField.getText()));
			moneyTextField.setText("");
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		moneyTextField.setEditable(false);
	}
}
