package client.newAppointment;

import calendar.UserModel;
import com.sun.deploy.util.SessionState;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import network.Query;
import network.ThreadClient;

import java.util.ArrayList;
import java.util.Hashtable;

public class Main extends Application {
    private static Stage appParent;
    private static Stage newAppointmentStage;
    private static UserModel user;

	@Override
	public void start(Stage primaryStage) {
		try {
			GridPane root = (GridPane) FXMLLoader.load(Main.class.getResource("view.fxml"));;
			Scene scene = new Scene(root,500,680);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

    public static void showNewAppointment(Stage parentStage, UserModel loggedUser) {
        appParent = parentStage;
        user = loggedUser;

        try {
            newAppointmentStage = new Stage();
            GridPane pane = (GridPane) FXMLLoader.load(Controller.class.getResource("view.fxml"));
            Scene scene = new Scene(pane);
            newAppointmentStage.setScene(scene);
            //newAppointmentStage.setTitle("Ny avtale");
            newAppointmentStage.initOwner(appParent);
            newAppointmentStage.initModality(Modality.WINDOW_MODAL);
            newAppointmentStage.show();
        } catch (Exception ex) {
            System.out.println("Exception found in newAppointment");
            ex.printStackTrace();
        }
    }

	public static void main(String[] args) { launch(args); }
}