package client.newAppointment;

import calendar.Appointment;
import calendar.Room;
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

import java.lang.reflect.Array;
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
			Scene scene = new Scene(root,500,800);
			/*scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());*/
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

    public static UserModel getLoggedUser() {
        return user;
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
    public static ArrayList<Room> getRooms(Appointment data) {
        Hashtable<String, ArrayList<Room>> reply = client.Main.socket.send(new Query("roomLogic",data)).data;
        return reply.get("reply");
    }

    public static Hashtable<String, Boolean> sendAppointment() {
        return new Hashtable<String,Boolean>();
    }

    public static void closeStage() {
        newAppointmentStage.close();
    }

	public static void main(String[] args) { launch(args); }
}
