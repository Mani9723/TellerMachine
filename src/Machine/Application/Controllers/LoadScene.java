package Machine.Application.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

/**
 * @author Mani Shah
 * @version 1.0
 * @since 7/28/2018  22:53
 */

// TODO create a general LoadScene class. Design Properly.//
// NOT COMPLETE
@SuppressWarnings("SameParameterValue")

class LoadScene
{
	private StackPane stackPaneCurr, stackPaneGoal;
	private AnchorPane anchorPane;
	private Class controllerClass;

	LoadScene(StackPane stckpaneCurr, StackPane stckPaneGoal)
	{
		stackPaneCurr = stckpaneCurr;
		stackPaneGoal = stckPaneGoal;
	}

	LoadScene(AnchorPane anchorPane)
	{
		this.anchorPane = anchorPane;
	}

	void setController(Class controller)
	{
		controllerClass = controller;
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
	void homeScene(String fxmlPath)
	{

	}


}
