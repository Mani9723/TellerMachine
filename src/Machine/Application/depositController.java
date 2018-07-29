package Machine.Application;


import Machine.AccountManager.CheckingAccount;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class depositController {

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

	@FXML
	private Label currBalance;

//	private CheckingAccount checkingAccount = new CheckingAccount();

	@FXML
	void clear(ActionEvent event)
	{
		depositInput.setText("");
	}

	@FXML
	void depositEvent(ActionEvent event)
	{
		//****** VALIDATE INPUT ******
		if(event.getSource().equals(depositButton)) {
			String amount = depositInput.getText();
	//		checkingAccount.deposit(Double.parseDouble(amount));
			depositInput.setText("");
		}
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
			try {
				secondPane = FXMLLoader.load(getClass().getResource("homePage.fxml"));
				stackPane.getChildren().setAll(secondPane);
			} catch (IOException e) {
				e.printStackTrace();
			}
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

}
