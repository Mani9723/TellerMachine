import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ApplicationMain extends Application {

	private double xOffset = 0;
	private double yOffset = 0;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/Machine/Application/FXMLs/loginPage.fxml"));
        primaryStage.setTitle("Bank of American");
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image("/Machine/images/240_F_119004171_qekVyleykNEcYt11JGhyI8scUcsLMxv2.jpg"));
        primaryStage.setMaxWidth(700);
        primaryStage.setMaxHeight(500);
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(700);

        //grab your root here
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        //move around here
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
