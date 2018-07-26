package Machine.Application;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class registerController {

	@FXML
	private StackPane stackPane;

	@FXML
	private AnchorPane registerPane, rootPane;

	@FXML
	private JFXButton registerButton;

	@FXML
	private JFXTextField username;

	@FXML
	private JFXPasswordField pass;

	@FXML
	private JFXPasswordField confirmPass;

	@FXML
	private JFXTextField firstName;

	@FXML
	private JFXTextField lastName;

	@FXML
	private JFXTextField accountType;

	@FXML
	private JFXButton returnButton;

	@FXML
	void handleRegisterButton(ActionEvent event)
	{
		//Process Registration
	}

	@FXML
	void returnHandler(ActionEvent event)
	{
		if(event.getSource().equals(returnButton)) {
			try {
				rootPane = FXMLLoader.load(getClass().getResource("bankUI.fxml"));
				stackPane.getChildren().setAll(rootPane);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
