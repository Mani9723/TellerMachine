package Machine.Application;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class DialogeBox
{
	private StackPane stackPane;
	private JFXDialog dialog;

	DialogeBox(StackPane stackPane)
	{
		this.stackPane = stackPane;
	}

	void OkButton(String buttonMessage, JFXDialog dialog)
	{
		this.dialog = dialog;
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button = new JFXButton("R E T U R N");
		button.setAlignment(Pos.CENTER);
		button.setPrefSize(100,50);
		//button.setStyle("-fx-font: 17 Sitka Small;");
		this.dialog = new JFXDialog(stackPane,dialogLayout,JFXDialog.DialogTransition.TOP);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED,(MouseEvent mouseEvent )->{
			this.dialog.close();
		});
		Label label = new Label(buttonMessage);
		label.setContentDisplay(ContentDisplay.CENTER);
		//label.setStyle("-fx-font: 15 Sitka Small;");
		dialogLayout.setBody(label);
		dialogLayout.setActions(button);
		this.dialog.show();
	}

	void cancelButton(String buttonMessage)
	{
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button = new JFXButton("C A N C E L");
		button.setAlignment(Pos.CENTER_LEFT);
		this.dialog = new JFXDialog(stackPane,dialogLayout,JFXDialog.DialogTransition.TOP);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED,(MouseEvent mouseEvent )->{
			this.dialog.close();
		});
		Label label = new Label("\t".concat(buttonMessage));
		label.setContentDisplay(ContentDisplay.CENTER);
		dialogLayout.setBody(label);
		dialogLayout.setActions(button);
		this.dialog.show();

	}

	void returnToLogin(String buttonMessage, AnchorPane rootPane, StackPane registerPane)
	{
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button = new JFXButton("LOGIN");
		button.setPrefSize(75,50);
		button.setAlignment(Pos.CENTER_LEFT);
		this.dialog = new JFXDialog(stackPane,dialogLayout,JFXDialog.DialogTransition.TOP);
		button.addEventHandler(MouseEvent.MOUSE_RELEASED,(MouseEvent mouseEvent )->{
			this.dialog.close();
			loadLoginPage(rootPane,registerPane);
		});
		Label label = new Label("\t".concat(buttonMessage));
		label.setContentDisplay(ContentDisplay.CENTER);
		dialogLayout.setBody(label);
		dialogLayout.setActions(button);
		this.dialog.show();
	}

	private void loadLoginPage(AnchorPane rootPane, StackPane registerPane)
	{
		try {
			rootPane = FXMLLoader.load(getClass().getResource("loginPage.fxml"));
			registerPane.getChildren().setAll(rootPane);
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}
