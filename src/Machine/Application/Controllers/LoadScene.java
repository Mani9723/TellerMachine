package Machine.Application.Controllers;

import Machine.Application.Controllers.Model.DatabaseModel;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Mani Shah
 * @version 1.0
 * @since 7/28/2018  22:53
 */

@SuppressWarnings({"SameParameterValue", "ConstantConditions"})

class LoadScene
{
	private StackPane stackPaneCurr, stackPaneGoal;
	private ActionEvent actionEvent;
	private KeyEvent keyEvent;
	private static final String adminFxml = "/Machine/Application/FXMLs/AdminPage.fxml";
	private static final String loginFxml = "/Machine/Application/FXMLs/loginPage.fxml";
	private static final String homeFxml = "/Machine/Application/FXMLs/homePage.fxml";
	private static final String depositFxml = "/Machine/Application/FXMLs/depositPage.fxml";
	private static final String withdrawFxml = "/Machine/Application/FXMLs/withdrawPage.fxml";
	private static final String registerFxml = "/Machine/Application/FXMLs/registerPage.fxml";
	private static final String statementFxml = "/Machine/Application/FXMLs/statementPage.fxml";
	private static final String quickFxml = "/Machine/Application/FXMLs/QuickWithdraw.fxml";
	private static final String settingsFxml = "/Machine/Application/FXMLs/SettingPage.fxml";

	private double xOffset = 0;
	private double yOffset = 0;



	LoadScene(StackPane stckpaneCurr, StackPane stckPaneGoal)
	{
		stackPaneCurr = stckpaneCurr;
		stackPaneGoal = stckPaneGoal;
	}

	LoadScene()
	{

	}


	void loginPage()
	{
		try {
			stackPaneGoal = FXMLLoader.load(getClass().getResource(loginFxml));
			stackPaneCurr.getChildren().setAll(stackPaneGoal);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void setActionEvent(ActionEvent actionEvent)
	{
		this.actionEvent = actionEvent;
	}

	void setKeyEvent(KeyEvent keyEvent)
	{
		this.keyEvent = keyEvent;
	}

	@SuppressWarnings("unused")
	void adminSceneKeyEvent(DatabaseModel databaseModel)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(adminFxml));
		Parent loginParent = null;
		try {
			loginParent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene currScene = new Scene(loginParent);
		AdministratorController controller = loader.getController();
		//controller.setUsername(username.getText());
		controller.init(databaseModel);
		Stage homeWindow = (Stage)((Node)keyEvent.getSource()).getScene().getWindow();
		allowDragableScene(loginParent,homeWindow);
		homeWindow.setScene(currScene);
		homeWindow.show();
	}

	void adminSceneActionEvent(DatabaseModel databaseModel)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(adminFxml));
		Parent loginParent = null;
		try {
			loginParent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene currScene = new Scene(loginParent);
		AdministratorController controller = loader.getController();
		//controller.setUsername(username.getText());
		controller.init(databaseModel);
		Stage homeWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		allowDragableScene(loginParent,homeWindow);
		homeWindow.setScene(currScene);
		homeWindow.show();
	}

	void homeScene(JFXTextField username, DatabaseModel databaseModel)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(homeFxml));
		Parent loginParent = null;
		try {
			loginParent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene currScene = new Scene(loginParent);
		homeController controller = loader.getController();
		controller.setUsername(username.getText());
		controller.init(databaseModel);
		Stage homeWindow = (Stage)((Node)keyEvent.getSource()).getScene().getWindow();
		allowDragableScene(loginParent,homeWindow);
		homeWindow.setScene(currScene);
		homeWindow.show();
	}

	private void allowDragableScene(Parent parent, Stage stage)
	{
		parent.setOnMousePressed(event -> {
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
		});

		parent.setOnMouseDragged(event -> {
			stage.setX(event.getScreenX() - xOffset);
			stage.setY(event.getScreenY() - yOffset);
		});
	}

	void homeSceneAction(String username, DatabaseModel databaseModel)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(homeFxml));
		Parent loginParent = null;
		try {
			loginParent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene currScene = new Scene(loginParent);
		homeController controller = loader.getController();
		controller.setUsername(username);
		controller.init(databaseModel);
		Stage homeWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		allowDragableScene(loginParent,homeWindow);
		homeWindow.setScene(currScene);
		homeWindow.show();
	}

	void registerScene(DatabaseModel databaseModel)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(registerFxml));
		Parent loginParent = null;
		try {
			loginParent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene currScene = new Scene(loginParent);
		registerController controller = loader.getController();
		controller.setDatabaseModel(databaseModel);
		Stage homeWindow;
		if(actionEvent != null) homeWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		else homeWindow = (Stage)((Node)keyEvent.getSource()).getScene().getWindow();
		allowDragableScene(loginParent,homeWindow);
		homeWindow.setScene(currScene);
		homeWindow.show();
	}


	void depositPage(String username, DatabaseModel databaseModel)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(depositFxml));
		Parent loginParent = null;
		try {
			loginParent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene currScene = new Scene(loginParent);
		depositController controller = loader.getController();
		controller.setUsername(username);
		controller.init(databaseModel);
		Stage homeWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		allowDragableScene(loginParent,homeWindow);
		homeWindow.setScene(currScene);
		homeWindow.show();
	}

	void statementScene(String username, DatabaseModel databaseModel)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(statementFxml));
		Parent loginParent = null;
		try {
			loginParent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene currScene = new Scene(loginParent);
		StatementController controller = loader.getController();
		controller.setUsername(username);
		controller.setDatabaseModel(databaseModel);
		controller.init();
		Stage homeWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		allowDragableScene(loginParent,homeWindow);
		homeWindow.setScene(currScene);
		homeWindow.show();
	}

	void settingsScene(String username, DatabaseModel databaseModel)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(settingsFxml));
		Parent loginParent = null;
		try {
			loginParent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene currScene = new Scene(loginParent);
		SettingsController controller = loader.getController();
		controller.init(username, databaseModel);
		Stage homeWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		allowDragableScene(loginParent,homeWindow);
		homeWindow.setScene(currScene);
		homeWindow.show();
	}

	void withdrawScene(String username, DatabaseModel databaseModel)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(withdrawFxml));
		Parent loginParent = null;
		try {
			loginParent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene currScene = new Scene(loginParent);
		withdrawController controller = loader.getController();
		controller.setUsername(username);
		controller.init(databaseModel);
		Stage homeWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		allowDragableScene(loginParent,homeWindow);
		homeWindow.setScene(currScene);
		homeWindow.show();
	}

	void quickCashScene( String username, DatabaseModel databaseModel)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(quickFxml));
		Parent loginParent = null;
		try {
			loginParent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene currScene = new Scene(loginParent);
		quickWithdrawController controller = loader.getController();
		controller.setUsername(username);
		controller.init(databaseModel);
		Stage homeWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		allowDragableScene(loginParent,homeWindow);
		homeWindow.setScene(currScene);
		homeWindow.show();
	}
}