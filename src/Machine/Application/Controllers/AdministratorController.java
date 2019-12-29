package Machine.Application.Controllers;

import Machine.Application.Controllers.Model.DatabaseModel;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class AdministratorController
{

	@FXML
	private JFXButton logoutButton;

	@FXML
	private StackPane stackpane;

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Label dateLabel;

	private static DatabaseModel databaseModel;

	public void init(DatabaseModel model)
	{
		databaseModel = model;
	}

	@FXML
	public void handleHomeButton(ActionEvent actionEvent)
	{
		if(actionEvent.getSource().equals(logoutButton)){
			try {
				stackpane = FXMLLoader.load(getClass().getResource("/Machine/Application/FXMLs/loginPage.fxml"));
				anchorPane.getChildren().setAll(stackpane);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
