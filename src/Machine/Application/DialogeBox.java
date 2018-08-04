package Machine.Application;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

import java.io.IOException;

public class DialogeBox
{
	private StackPane stackPane;
	private JFXDialog dialog;
	private Parent parent;
	GaussianBlur gaussianBlur;

	DialogeBox(StackPane stackPane)
	{
		this.stackPane = stackPane;
		gaussianBlur = new GaussianBlur();
		gaussianBlur.setRadius(7.5);
	}

	void setNonStackPane(Parent root)
	{
		parent = root;
	}

	void OkButton(String buttonMessage, JFXDialog dialog)
	{
		this.dialog = dialog;
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button = new JFXButton("R E T U R N");
		button.setAlignment(Pos.CENTER);
		button.setPrefSize(100,50);
		this.dialog = new JFXDialog(stackPane,dialogLayout,JFXDialog.DialogTransition.CENTER);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED,(MouseEvent mouseEvent )->{
			parent.setEffect(null);
			this.dialog.close();
		});
		Label label = new Label(buttonMessage);
		label.setContentDisplay(ContentDisplay.CENTER);
		dialogLayout.setBody(label);
		dialogLayout.setActions(button);
		this.dialog.show();
		parent.setEffect(gaussianBlur);
	}
	void drawerOkButton(String buttonMessage, JFXDialog dialog)
	{
		this.dialog = dialog;
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button = new JFXButton("RETURN");
		button.setAlignment(Pos.CENTER);
		button.setPrefSize(80,50);
		this.dialog = new JFXDialog(stackPane,dialogLayout,JFXDialog.DialogTransition.CENTER);
		button.addEventHandler(MouseEvent.MOUSE_CLICKED,(MouseEvent mouseEvent )->{
			parent.setEffect(null);
			this.dialog.close();
		});
		Label label = new Label(buttonMessage);
		label.setFont(Font.font(12));
		label.setContentDisplay(ContentDisplay.CENTER);
		dialogLayout.setBody(label);
		dialogLayout.setActions(button);
		dialogLayout.setPrefWidth(250);
		this.dialog.show();
		parent.setEffect(gaussianBlur);
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

	void returnToLogin(String buttonMessage, StackPane rootPane, StackPane registerPane)
	{
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXButton button = new JFXButton("LOGIN");
		button.setPrefSize(75,50);
		button.setAlignment(Pos.CENTER_LEFT);
		this.dialog = new JFXDialog(stackPane,dialogLayout,JFXDialog.DialogTransition.TOP);
		button.addEventHandler(MouseEvent.MOUSE_RELEASED,(MouseEvent mouseEvent )->{
			this.dialog.close();
			parent.setEffect(null);
			loadLoginPage(rootPane,registerPane);
		});
		Label label = new Label("\t".concat(buttonMessage));
		label.setContentDisplay(ContentDisplay.CENTER);
		dialogLayout.setBody(label);
		dialogLayout.setActions(button);
		this.dialog.show();
		parent.setEffect(gaussianBlur);
	}

	private void loadLoginPage(StackPane rootPane, StackPane registerPane)
	{
		try {
			rootPane = FXMLLoader.load(getClass().getResource("loginPage.fxml"));
			registerPane.getChildren().setAll(rootPane);
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}
