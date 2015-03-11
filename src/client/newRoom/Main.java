package client.newRoom;

import calendar.UserModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Rae on 11.03.15.
 */
public class Main extends Application {
    private Stage roomParent;
    private Stage newRoomStage;
    private UserModel loggedUser;

    @Override
    public void start(Stage primaryStage) {
        try {
            GridPane root = (GridPane) FXMLLoader.load(Main.class.getResource("view.fxml"));
            Scene scene = new Scene(root, 500, 680);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showNewRoom(Stage parentStage, UserModel loggedUser) {
        this.roomParent = parentStage;
        this.loggedUser = loggedUser;

        try {
            newRoomStage = new Stage();
            GridPane pane = (GridPane) FXMLLoader.load(client.newRoom.Controller.class.getResource("view.fxml"));
            Scene scene = new Scene(pane);
            newRoomStage.setScene(scene);
            newRoomStage.setTitle("Nytt rom");
            newRoomStage.initOwner(this.roomParent);
            newRoomStage.initModality(Modality.WINDOW_MODAL);
            newRoomStage.show();
        } catch (Exception ex) {
            System.out.println("Exception found in newRoom");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}