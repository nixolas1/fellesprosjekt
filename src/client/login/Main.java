package client.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import network.Query;
import network.ThreadClient;
import security.Crypto;
import server.database.ConnectDB;
import server.database.Logic;

import java.util.Hashtable;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            GridPane root = (GridPane) FXMLLoader.load(Main.class.getResource("login-gui.fxml"));
            Scene scene = new Scene(root, 1200, 800);
            //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
            ConnectDB connectDB = new ConnectDB();
            Logic dblogic = new Logic(connectDB.connect());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Hashtable<String, Boolean> checkLogin(final String user, final String pass, final String domain){

        System.out.println("Sjekk login: "+user+", "+pass+", "+domain);
        Hashtable<String, String> data = new Hashtable<String, String>(){{
            put("username",user);
            put("pass", pass);
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