package Machine.Application.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ApplicationMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("..\\FXMLs\\loginPage.fxml"));
        primaryStage.setTitle("Bank of American");
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image("Machine/images/240_F_119004171_qekVyleykNEcYt11JGhyI8scUcsLMxv2.jpg"));
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
