package client.newGroupCalendar;

import calendar.UserModel;
import client.newAppointment.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Rae on 06.03.15.
 */
public class Main extends Application {

    private Stage myParent;
    private Stage newGroupStage;
    private UserModel loggedUser;

    @Override
    public void start(Stage primaryStage) {
        try {
            GridPane root = (GridPane) FXMLLoader.load(Main.class.getResource("groupGui.fxml"));
            Scene scene = new Scene(root,500,600);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void showNewGroup(Stage parentStage, UserModel loggedUser) {
        this.myParent = parentStage;
        this.loggedUser = loggedUser;

        try {
            newGroupStage = new Stage();
            GridPane pane = (GridPane) FXMLLoader.load(Controller.class.getResource("groupGui.fxml"));
            Scene scene = new Scene(pane);
            newGroupStage.setScene(scene);
            newGroupStage.setTitle("Ny gruppe");
            newGroupStage.initOwner(this.myParent);
            newGroupStage.initModality(Modality.WINDOW_MODAL);
            newGroupStage.show();
        } catch (Exception ex) {
            System.out.println("Exception found in newGroup" + ex);
            ex.printStackTrace();
        }
    }
}
