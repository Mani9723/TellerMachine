package Machine.Application;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class homeController {

	@FXML
	private AnchorPane secondPane, rootPane;

	@FXML
	private StackPane stackPane;

	@FXML
	private JFXButton quickCash;

	@FXML
	private JFXButton deposit;

	@FXML
	private JFXButton withdraw;

	@FXML
	private JFXButton exitHome;

	@FXML
	private Label greeting;

	@FXML
	private Label dateTime;

	@FXML
	private Label accountType;

	@FXML
	private Label balanceLabel;

	@FXML
	private Label accountName;

	@FXML
	private Label currBalance;

	@FXML
	void depositMoney(ActionEvent event)
	{
		if(event.getSource().equals(deposit)){
			try {
				stackPane = FXMLLoader.load(getClass().getResource("depositPage.fxml"));
				secondPane.getChildren().setAll(stackPane);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	@FXML
	void exitToHome(ActionEvent event)
	{
		if(event.getSource().equals(exitHome)){
			try {
				rootPane = FXMLLoader.load(getClass().getResource("bankUI.fxml"));
				secondPane.getChildren().setAll(rootPane);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	void withdrawMoney(ActionEvent event)
	{
		if(event.getSource().equals(withdraw)) {
			try {
				stackPane = FXMLLoader.load(getClass().getResource("withdrawPage.fxml"));
				secondPane.getChildren().setAll(stackPane);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
