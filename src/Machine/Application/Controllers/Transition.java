package Machine.Application.Controllers;

import javafx.animation.FadeTransition;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class Transition
{
	private static final int DURATION = 500;
	private StackPane stackPane;
	private AnchorPane anchorPane;

	public Transition(StackPane stackPane, AnchorPane anchorPane)
	{
		this.stackPane = stackPane;
		this.anchorPane = anchorPane;
	}

	protected void fadeIn()
	{
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(DURATION));
		if(stackPane != null)
			fadeTransition.setNode(stackPane);
		else if(anchorPane != null)
			fadeTransition.setNode(anchorPane);
		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(1);
		fadeTransition.play();
	}

	protected FadeTransition fadeOut()
	{
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(DURATION));
		if(stackPane != null)
			fadeTransition.setNode(stackPane);
		else if(anchorPane != null)
			fadeTransition.setNode(anchorPane);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);
		fadeTransition.play();
		return fadeTransition;
	}
}