package Machine.Application.Controllers;

import Machine.Application.Controllers.Model.DatabaseModel;
import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class homeController implements Initializable
{

	@FXML
	private AnchorPane rootPane;

	@FXML
	private StackPane stackPane;

	@FXML
	private JFXButton quickCash, showAccButton;

	@FXML
	private JFXButton deposit, settingButton;

	@FXML
	private JFXButton withdraw;

	@FXML
	private JFXButton exitHome, statementButton;

	@FXML
	private Label greeting, accountNumber;

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

	private DecimalFormat decimalFormat = new DecimalFormat("##.##");

	private Date date;

	private String uName,first;
	private boolean isNumVisible;
	private static DatabaseModel databaseModel;
	private LoadScene loadScene;

	private Transition transition;


	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		rootPane.setOpacity(0);
		transition = new Transition(null,rootPane);
		date = new Date();
		Image image =new Image(getClass().getResourceAsStream("/Machine/images/user-2517433_960_7201.png"));
		settingButton.setText("");
		loadScene = new LoadScene();
		dateTime.setText(getDate());
		//accountType.setText("Checking");
		ImageView imageView = new ImageView(image);
		imageView.setPreserveRatio(false);
		imageView.setSmooth(true);
		imageView.setFitHeight(40);
		imageView.setFitWidth(40);
		settingButton.setGraphic(imageView);
		accountNumber.setVisible(false);
		isNumVisible = false;
		transition.fadeIn();
	}

	void init(DatabaseModel machine)
	{
		setMachineModel(machine);
		try {
			setFirstName(databaseModel.getAccountInfo(getuName(),"FirstName"));
			greeting.setText(getAppropriateGreeting(date.getHour()) +", " + getFirst());
			String bal = databaseModel.getAccountInfo(getuName(),"CurrentBalance");
			currBalance.setText("$"+decimalFormat.format(Double.parseDouble(bal)));
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void depositMoney(ActionEvent event)
	{
		if(event.getSource().equals(deposit)){
			transition.fadeOut().setOnFinished((ActionEvent transitionEvent) ->{
				loadScene.setActionEvent(event);
				loadScene.depositPage(getuName(), databaseModel);
			});
		}
	}


	@FXML
	void settings(ActionEvent event)
	{
		if(event.getSource().equals(settingButton)){
			transition.fadeOut().setOnFinished((ActionEvent transtionEvent) -> {
				loadScene.setActionEvent(event);
				loadScene.settingsScene(getuName(), databaseModel);
			});
		}
	}

	@FXML
	void exitToHome(ActionEvent event)
	{
		if(event.getSource().equals(exitHome)){
			transition.fadeOut().setOnFinished((ActionEvent) -> {
				try {
					stackPane = FXMLLoader.load(getClass()
							.getResource("/Machine/Application/FXMLs/loginPage.fxml"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				rootPane.getChildren().setAll(stackPane);
				rootPane.setOpacity(1);
			});
		}
	}

	@FXML
	void handleStatementButton(ActionEvent event)
	{
		if(event.getSource().equals(statementButton)) {
			transition.fadeOut().setOnFinished((ActionEvent transitionEvent) -> {
				loadScene.setActionEvent(event);
				loadScene.statementScene(getuName(), databaseModel);
			});
		}
	}

	@FXML
	void withdrawMoney(ActionEvent event)
	{
		if(event.getSource().equals(withdraw)) {
			transition.fadeOut().setOnFinished((ActionEvent transitionEvent) ->{
				loadScene.setActionEvent(event);
				loadScene.withdrawScene(getuName(), databaseModel);
			});
		}
	}

	@FXML
	void quickCash(ActionEvent event)
	{
		if(event.getSource().equals(quickCash)) {
			transition.fadeOut().setOnFinished((ActionEvent transitionEvent) ->{
				loadScene.setActionEvent(event);
				loadScene.quickCashScene(getuName(), databaseModel);
			});
		}
	}

	private void setFirstName(String name)
	{
		first = name;
	}

	private String getFirst()
	{
		return first;
	}
	void setUsername(String name)
	{
		uName = name;
	}

	private void setMachineModel(DatabaseModel model)
	{
		databaseModel = model;
	}
	private String getuName()
	{
		return uName;
	}

	private String getDate()
	{
		return date.getDate();
	}
	private String getAppropriateGreeting(int hour)
	{
		if(hour>=0 && hour<12) return "Good Morning ";
		else if(hour>=12 && hour<=16) return "Good Afternoon ";
		else return "Good Evening ";

	}

	public void handleAccountNumberButton(ActionEvent event)
	{
		if(event.getSource().equals(showAccButton)){
			if(!isNumVisible){
				showAccButton.setText("Hide account number");
				accountNumber.setVisible(true);
				try {
					accountNumber.setText(databaseModel.getAccountInfo(getuName(),"AccountNumber"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				isNumVisible = true;
			} else{
				isNumVisible = false;
				showAccButton.setText("Show account number");
				accountNumber.setVisible(false);
			}
		}


	}
}