package Machine.Application.Controllers;

import Machine.Application.Controllers.Model.MachineModel;
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

	void setActionEvent(ActionEvent actionEvent)
	{
		this.actionEvent = actionEvent;
	}

	void setKeyEvent(KeyEvent keyEvent)
	{
		this.keyEvent = keyEvent;
	}

	private ActionEvent actionEvent;
	private KeyEvent keyEvent;

	LoadScene(StackPane stckpaneCurr, StackPane stckPaneGoal)
	{
		stackPaneCurr = stckpaneCurr;
		stackPaneGoal = stckPaneGoal;
	}

	LoadScene()
	{

	}


	void loginPage(String fxmlPath)
	{
		try {
			stackPaneGoal = FXMLLoader.load(getClass().getResource(fxmlPath));
			stackPaneCurr.getChildren().setAll(stackPaneGoal);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	void homeScene(String fxmlPath, JFXTextField username, MachineModel machineModel)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(fxmlPath));
		Parent loginParent = null;
		try {
			loginParent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene currScene = new Scene(loginParent);
		homeController controller = loader.getController();
		controller.setUsername(username.getText());
		controller.init(machineModel);
		Stage homeWindow = (Stage)((Node)keyEvent.getSource()).getScene().getWindow();
		homeWindow.setScene(currScene);
		homeWindow.show();
	}
	void homeSceneAction(String fxmlPath, String username, MachineModel machineModel)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(fxmlPath));
		Parent loginParent = null;
		try {
			loginParent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene currScene = new Scene(loginParent);
		homeController controller = loader.getController();
		controller.setUsername(username);
		controller.init(machineModel);
		Stage homeWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		homeWindow.setScene(currScene);
		homeWindow.show();
	}

	void registerScene(String fxmlPath, MachineModel machineModel)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(fxmlPath));
		Parent loginParent = null;
		try {
			loginParent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene currScene = new Scene(loginParent);
		registerController controller = loader.getController();
		controller.setMachineModel(machineModel);
		Stage homeWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		homeWindow.setScene(currScene);
		homeWindow.show();
	}

	void depositPage(String fxml, String username, MachineModel machineModel)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(fxml));
		Parent loginParent = null;
		try {
			loginParent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene currScene = new Scene(loginParent);
		depositController controller = loader.getController();
		controller.setUsername(username);
		controller.init(machineModel);
		Stage homeWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		homeWindow.setScene(currScene);
		homeWindow.show();
	}

	void statementScene(String fxml, String username, MachineModel machineModel)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(fxml));
		Parent loginParent = null;
		try {
			loginParent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene currScene = new Scene(loginParent);
		StatementController controller = loader.getController();
		controller.setUsername(username);
		controller.setMachineModel(machineModel);
		controller.init();
		Stage homeWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		homeWindow.setScene(currScene);
		homeWindow.show();
	}

	void withdrawScene(String fxml, String username, MachineModel machineModel)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(fxml));
		Parent loginParent = null;
		try {
			loginParent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene currScene = new Scene(loginParent);
		withdrawController controller = loader.getController();
		controller.setUsername(username);
		controller.init(machineModel);
		Stage homeWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		homeWindow.setScene(currScene);
		homeWindow.show();
	}

	void quickCashScene(String fxml, String username, MachineModel machineModel)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(fxml));
		Parent loginParent = null;
		try {
			loginParent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene currScene = new Scene(loginParent);
		quickWithdrawController controller = loader.getController();
		controller.setUsername(username);
		controller.init(machineModel);
		Stage homeWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
		homeWindow.setScene(currScene);
		homeWindow.show();
	}


}
