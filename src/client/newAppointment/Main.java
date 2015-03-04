package client.newAppointment;
	
import calendar.UserModel;
import com.sun.deploy.util.SessionState;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import network.Query;
import network.ThreadClient;

import java.util.ArrayList;
import java.util.Hashtable;


public class Main extends Application {
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
	
	public static void main(String[] args) {
		launch(args);
	}

    public static ArrayList<UserModel> getAllUsers() {
        ThreadClient socket = new ThreadClient();
        Query reply = socket.send(new Query("getAllUsers",new ArrayList<UserModel>()));
        System.out.println(reply.function);
        Hashtable<String, ArrayList<UserModel>> response = reply.data;
        return response.get("reply");
    }

}


