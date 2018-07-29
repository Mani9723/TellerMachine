package Machine.Application;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

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
	private Label date;

	@FXML
	private JFXButton mainMenu;

	@FXML
	private JFXButton exit;

	@FXML
	private JFXButton deposit;

	@FXML
	void cashButtons(ActionEvent event)
	{

	}

	@FXML
	void clickOther(ActionEvent event)
	{
		if(event.getSource().equals(other)) {
			try {
				stackPane2 = FXMLLoader.load(getClass().getResource("withdrawPage.fxml"));
				stackPane.getChildren().setAll(stackPane2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	void goToDeposit(ActionEvent event)
	{
		if(event.getSource().equals(deposit)) {
			try {
				stackPane2 = FXMLLoader.load(getClass().getResource("depositPage.fxml"));
				stackPane.getChildren().setAll(stackPane2);
			} catch (IOException e) {
				e.printStackTrace();
			}
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
			try {
				rootPane = FXMLLoader.load(getClass().getResource("homePage.fxml"));
				stackPane.getChildren().setAll(rootPane);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
