package client.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import network.Query;
import network.ThreadClient;
import security.Crypto;

import java.util.Hashtable;

public class Main extends Application {
    static Stage stage;
    static String message = null;
    @Override
    public void start(Stage primaryStage) {
        try {
            GridPane root = (GridPane) FXMLLoader.load(Main.class.getResource("gui.fxml"));
            Scene scene = new Scene(root, 1200, 800);
            //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void show(Stage primaryStage) {
        show(primaryStage, null);
    }

    public static void show(Stage primaryStage, String msg) {
        try {
            stage = primaryStage;
            message = msg;
            GridPane root = (GridPane) FXMLLoader.load(Main.class.getResource("gui.fxml"));
            Scene scene = new Scene(root, 1200, 800);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Hashtable<String, Boolean> checkLogin(final String user, final String pass, final String domain){

        System.out.println("Sjekk login: "+user+", "+pass+", "+domain);
        Hashtable<String, String> data = new Hashtable<String, String>(){{
            put("username",user);
            put("pass", Crypto.hash(pass));
            put("domain", domain);
        }};
        ThreadClient socket = new ThreadClient();

        Query reply = socket.send(new Query("login", data));
        Hashtable<String, Boolean> response = reply.data;

        return response;
    }

    public static void main(String[] args) {
        launch(args);
    }

}