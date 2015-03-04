package client.calendar;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by jonaslochsen on 02.03.15.
 */
public class Controller {


    @FXML private ComboBox chooseCalendar;
    @FXML private Text name;
    @FXML private Button logOff;
    @FXML private Button userSettings;
    @FXML private Button yourCalendar;
    @FXML private Button today;



    protected static Stage primaryStage;

    @FXML
    void initialize() {
        chooseCalendar.setItems(FXCollections.observableArrayList("Gunnar Greve"));

    }

    public void onBtnShowNewAppointment(ActionEvent event) {
        client.newAppointment.Controller newApp = new client.newAppointment.Controller();
        try {
            newApp.showNewAppointment(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logOff(ActionEvent event) {
        client.login.Main.show(Main.stage);
    }

    public void showUserSettings(ActionEvent event) {
    }
}
