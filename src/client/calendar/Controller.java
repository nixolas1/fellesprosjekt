package client.calendar;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

/**
 * Created by jonaslochsen on 02.03.15.
 */
public class Controller {



    @FXML
    private ComboBox chooseCalendar;

    @FXML
    void initialize() {
        chooseCalendar.setItems(FXCollections.observableArrayList("Gunnar Greve"));

    }

    /*public void onBtnShowNewAppointment(ActionEvent event) {
        client.newAppointment.Controller newApp = new client.newAppointment.Controller();
        try {
            newApp.showNewAppointment(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
