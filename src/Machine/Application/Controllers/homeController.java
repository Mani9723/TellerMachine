package Machine.Application.Controllers;

import Machine.Application.Controllers.Model.DatabaseModel;
import Machine.Application.Controllers.Date;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class homeController implements Initializable
{

	@FXML
	private AnchorPane secondPane;

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



	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		date = new Date();
		Image image =new Image(getClass().getResourceAsStream("/Machine/images/user-2517433_960_7201.png"));
		settingButton.setText("");
		loadScene = new LoadScene();
		dateTime.setText(date.getDate());
		accountType.setText("Checking");
		ImageView imageView = new ImageView(image);
		imageView.setPreserveRatio(false);
		imageView.setSmooth(true);
		imageView.setFitHeight(40);
		imageView.setFitWidth(40);
		settingButton.setGraphic(imageView);
		accountNumber.setVisible(false);
		isNumVisible = false;

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
			loadDepositPage(event);
		}
	}

	@FXML
	private void loadDepositPage(ActionEvent event)
	{
		if(event.getSource().equals(deposit)) {
			loadScene.setActionEvent(event);
			loadScene.depositPage(getuName(), databaseModel);
		}
	}

	@FXML
	void settings(ActionEvent event)
	{
		if(event.getSource().equals(settingButton)){
			loadScene.setActionEvent(event);
			loadScene.settingsScene(getuName(), databaseModel);
		}
	}

	@FXML
	void exitToHome(ActionEvent event)
	{
		if(event.getSource().equals(exitHome)){
			try {
				stackPane = FXMLLoader.load(getClass().getResource("/Machine/Application/FXMLs/loginPage.fxml"));
				secondPane.getChildren().setAll(stackPane);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	void handleStatementButton(ActionEvent event)
	{
		if(event.getSource().equals(statementButton)) {
			loadScene.setActionEvent(event);
			loadScene.statementScene(getuName(), databaseModel);
		}

	}

	@FXML
	void withdrawMoney(ActionEvent event)
	{
		if(event.getSource().equals(withdraw)) {
			loadScene.setActionEvent(event);
			loadScene.withdrawScene(getuName(), databaseModel);
		}
	}

	@FXML
	void quickCash(ActionEvent event)
	{
		if(event.getSource().equals(quickCash)) {
			loadScene.setActionEvent(event);
			loadScene.quickCashScene(uName, databaseModel);
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
//		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
//		Date date = new Date();
//		return dateFormat.format(date);
	}
//	private int getHour()
//	{
//		SimpleDateFormat hourformat = new SimpleDateFormat("HH");
//		Date date = new Date();
//		return Integer.parseInt(hourformat.format(date));
//	}
	private String getAppropriateGreeting(int hour)
	{
		if(hour>=0 && hour<12) return "Good Morning ";
		else if(hour>=12 && hour<16) return "Good Afternoon ";
		else return "Good Evening ";

	}

	public void handleAccountNumberButton(ActionEvent event)
	{
		if(event.getSource().equals(showAccButton)){
			if(!isNumVisible){
				showAccButton.setText("HIDE ACCOUNT NUMBER");
				accountNumber.setVisible(true);
				try {
					accountNumber.setText(databaseModel.getAccountInfo(getuName(),"AccountNumber"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				isNumVisible = true;
			} else{
				isNumVisible = false;
				showAccButton.setText("SHOW ACCOUNT NUMBER");
				accountNumber.setVisible(false);
			}
		}


	}
}