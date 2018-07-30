package Machine.Application;

import Machine.AccountManager.CheckingAccount;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ApplicationMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("bankUI.fxml"));
        primaryStage.setTitle("Bank of American");
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);
	    System.out.println("Making Sure Password File exists...");
		new CheckingAccount(true);
	    System.out.println("File Exists...");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
