package client.userSettings;

import calendar.UserModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import network.Query;

import java.util.Hashtable;

/**
 * Created by Sondre on 04.03.15.
 */
public class Main extends Application {
    static Stage stage;
    static UserModel user;


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

    public static void show(Stage primaryStage, UserModel model) {
        try {
            stage = primaryStage;
            user = model;
            GridPane root = (GridPane) FXMLLoader.load(Main.class.getResource("gui.fxml"));
            Scene scene = new Scene(root, 1200, 800);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean createUser(final String username, final String password, final String domain, final String firstName, final String lastName, final String phone) {
        // username, password, domain, firstName, lastName, phone
        Hashtable<String, String> data = new Hashtable<String, String>() {{
            put("username",username);
            put("password",password);
            put("domain", domain);
            put("firstName", firstName);
            put("lastName", lastName);
            put("phone", phone);
        }};

        Query reply = client.Main.socket.send(new Query("updateSettings", data));
        Hashtable<String, Boolean> response = reply.data;

        if(response.get("reply")){
            System.out.println("success");
        }

        return response.get("reply");
    }

}
