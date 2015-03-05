package client.calendar;

import calendar.UserModel;
import client.newAppointment.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Created by jonaslochsen on 03.03.15.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            client.calendar.Controller.primaryStage = primaryStage;

            GridPane root = (GridPane) FXMLLoader.load(Main.class.getResource("main-gui.fxml"));
            Scene scene = new Scene(root, 1200, 800);
			/*scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());*/
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void show(Stage primaryStage) {
        show(primaryStage, null);
    }

    static Stage stage;
    static UserModel user;
    public static void show(Stage primaryStage, UserModel loggedUser) {
        try {
            stage = primaryStage;
            user = loggedUser;
            GridPane root = (GridPane) FXMLLoader.load(Main.class.getResource("main-gui.fxml"));
            Scene scene = new Scene(root, 1200, 800);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
