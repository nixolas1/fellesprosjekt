package client.forgottenPassword;

/**
 * Created by jonaslochsen on 24.02.15.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import network.Query;
import network.ThreadClient;
import security.Crypto;

import java.util.Hashtable;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            GridPane root = (GridPane) FXMLLoader.load(Main.class.getResource("gui.fxml"));
            Scene scene = new Scene(root,1200,800);
            //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    static Stage stage;
    public static void show(Stage primaryStage) {
        try {
            stage = primaryStage;
            GridPane root = (GridPane) FXMLLoader.load(Main.class.getResource("gui.fxml"));
            Scene scene = new Scene(root, 1200, 800);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Hashtable<String, Boolean> resetPass(final String user, final String domain){

        System.out.println("Reset passord: "+user+", "+domain);
        Hashtable<String, String> data = new Hashtable<String, String>(){{
            put("username",user);
            put("domain", domain);
        }};
        ThreadClient socket = new ThreadClient();

        Query reply = socket.send(new Query("reset", data));
        Hashtable<String, Boolean> response = reply.data;

        return response;
    }

    public static void main(String[] args) {
        launch(args);
    }


}
