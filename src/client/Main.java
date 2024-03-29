package client;

import calendar.Appointment;
import calendar.UserModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import network.ClientDB;
import network.Query;
import network.ThreadClient;
import security.Crypto;

import java.util.Hashtable;

/**
 * Created by nixo on 2/26/15.
 */
public class Main extends Application {
    Stage stage;
    public static UserModel user = new UserModel();
    public static ThreadClient socket = null;
    @Override
    public void start(Stage mainStage) {
        try {
            this.stage=mainStage;
            System.out.println("Starting main client stage");
            client.login.Main.show(mainStage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        socket = new ThreadClient();
        //System.out.println(ClientDB.getAllTableRows("Calendar", socket).toString());
        launch(args);


    }


}
