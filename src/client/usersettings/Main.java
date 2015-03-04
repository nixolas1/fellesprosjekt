package client.userSettings;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Created by Sondre on 04.03.15.
 */
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
            System.out.println("EXCEPTION FFS");
            e.printStackTrace();
        }
    }
    /*

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
    */
}
