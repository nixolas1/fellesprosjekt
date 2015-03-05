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
    private Stage myParent;
    private Stage newAppointmentStage;
    private UserModel loggedUser;

	@Override
	public void start(Stage primaryStage) {
		try {
			GridPane root = (GridPane) FXMLLoader.load(Main.class.getResource("view.fxml"));;
			Scene scene = new Scene(root,500,600);
			/*scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());*/
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

    public void showNewAppointment(Stage parentStage, UserModel loggedUser) {
        this.myParent = parentStage;
        this.loggedUser = loggedUser;

        try {
            newAppointmentStage = new Stage();
            GridPane pane = (GridPane) FXMLLoader.load(Controller.class.getResource("view.fxml"));
            Scene scene = new Scene(pane);
            newAppointmentStage.setScene(scene);
            newAppointmentStage.setTitle("Ny avtale");
            newAppointmentStage.initOwner(this.myParent);
            newAppointmentStage.initModality(Modality.WINDOW_MODAL);
            newAppointmentStage.show();
        } catch (Exception ex) {
            System.out.println("Exception found in newAppointment");
            ex.printStackTrace();
        }
    }
	
	public static void main(String[] args) {
		launch(args);
	}



}


