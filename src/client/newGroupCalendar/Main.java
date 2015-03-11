package client.newGroupCalendar;

import calendar.UserModel;
import client.newAppointment.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage groupParent;
    private static Stage newGroupStage;
    private static UserModel user;

    @Override
    public void start(Stage primaryStage) {
        try {
            GridPane root = (GridPane) FXMLLoader.load(Main.class.getResource("gui.fxml"));
            Scene scene = new Scene(root,500,680);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void showNewGroup(Stage parentStage, UserModel loggedUser) {
        groupParent = parentStage;
        user = loggedUser;

        try {
            newGroupStage = new Stage();
            GridPane pane = (GridPane) FXMLLoader.load(Main.class.getResource("gui.fxml"));
            Scene scene = new Scene(pane);
            newGroupStage.setScene(scene);
            //newGroupStage.setTitle("Ny gruppe");
            newGroupStage.initOwner(groupParent);
            newGroupStage.initModality(Modality.WINDOW_MODAL);
            newGroupStage.show();
        } catch (Exception ex) {
            System.out.println("Exception found in newGroup");
            ex.printStackTrace();
        }
    }

    public static void closeStage() {
        newGroupStage.close();
    }

    public static void main(String[] args) { launch(args); }
}
