package client.newGroupCalendar;

import calendar.Calendar;
import calendar.UserModel;
import client.newAppointment.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import network.Query;

import java.util.Hashtable;

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

    public static UserModel getLoggedUser() {
        return user;
    }

    public static Hashtable<String, Boolean> createGroup(Calendar cal) {
        Query reply = client.Main.socket.send(new Query("createGroup", cal));
        return reply.data;
    }

    public static void closeStage() {
        newGroupStage.close();
    }

    public static void main(String[] args) { launch(args); }
}
