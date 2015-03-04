package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import network.Query;
import network.ThreadClient;
import security.Crypto;

import java.util.Hashtable;

/**
 * Created by nixo on 2/26/15.
 */
public class Main extends Application {
    Stage stage;
    @Override
    public void start(Stage mainStage) {
        try {
            this.stage=mainStage;
            System.out.println("Starting main client stage");
            client.login.Main.show(mainStage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        launch(args);
    }


}
