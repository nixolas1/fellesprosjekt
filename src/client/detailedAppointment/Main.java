package client.detailedAppointment;

import calendar.Appointment;
import calendar.UserModel;
import client.newAppointment.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import network.Query;

import java.util.Hashtable;

/**
 * Created by Sondre on 04.03.15.
 */
public class Main extends Application {

    private static Stage detAppParent;
    private static Stage detAppointmentStage;
    public static UserModel user;
    static Appointment appointment;

    @Override
    public void start(Stage primaryStage) {
        try {
            GridPane root = (GridPane) FXMLLoader.load(Main.class.getResource("gui.fxml"));
            Scene scene = new Scene(root, 500, 660);
            //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void showDetAppointment(Stage parentStage, UserModel loggedUser, Appointment app) {
        detAppParent = parentStage;
        user = loggedUser;
        appointment = app;

        try {
            detAppointmentStage = new Stage();
            GridPane pane = (GridPane) FXMLLoader.load(client.detailedAppointment.Controller.class.getResource("gui.fxml"));
            Scene scene = new Scene(pane);
            detAppointmentStage.setScene(scene);
            //detAppointmentStage.setTitle("");
            detAppointmentStage.initOwner(detAppParent);
            detAppointmentStage.initModality(Modality.WINDOW_MODAL);
            detAppointmentStage.show();
        } catch (Exception ex) {
            System.out.println("Exception found in detAppointment");
            ex.printStackTrace();
        }
    }

    public static UserModel getLoggedUser() {
        return user;
    }

    public static void closeStage() {
        detAppointmentStage.close();
    }

    public static void main(String[] args) { launch(args); }

}
