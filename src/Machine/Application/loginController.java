package Machine.Application;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class loginController {

	@FXML
	private AnchorPane rootPane;

	@FXML
	private StackPane stackPane;

	@FXML
	private JFXTextField username;

	@FXML
	private JFXPasswordField password;

	@FXML
	private JFXButton login;

	@FXML
	private JFXButton registerButton;

	@FXML
	private JFXButton exit;

	@FXML
	private Label capsLockLabel;
	private AnchorPane secondPane, registerPane;

	@FXML
	void EnterKey(KeyEvent event)
	{
		if(event.getCode().equals(KeyCode.ENTER)){
			loginProcess();
		}
	}

	@FXML
	void exit(ActionEvent event)
	{
		if(event.getSource().equals(exit)){
			System.exit(0);
		}
	}

	@FXML
	void handleCapsLock(KeyEvent event)
	{

	}

	@FXML
	void loginHandler(ActionEvent event)
	{
		if(event.getSource().equals(login)){
			loginProcess();
		}
	}

	@FXML
	void register(ActionEvent event)
	{
		if(event.getSource().equals(registerButton)){
			try{
				stackPane= FXMLLoader.load(getClass().getResource("registerPage.fxml"));
				rootPane.getChildren().setAll(stackPane);
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void loginProcess()
	{
		//First make sure that the credentials are valid.
		//then load home page
		loadHomePage();
	}
	
	private void loadHomePage()
	{
		try{
			secondPane= FXMLLoader.load(getClass().getResource("homePage.fxml"));
			rootPane.getChildren().setAll(secondPane);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

}
