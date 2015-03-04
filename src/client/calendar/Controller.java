package client.calendar;

import calendar.UserModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

/**
 * Created by jonaslochsen on 02.03.15.
 */
public class Controller {


    @FXML
    private ComboBox chooseCalendar;

    protected static Stage primaryStage;

    @FXML
    void initialize() {
        chooseCalendar.setItems(FXCollections.observableArrayList("Gunnar Greve"));

    }

    public void onBtnShowNewAppointment(ActionEvent event) {
        client.newAppointment.Controller newApp = new client.newAppointment.Controller();
        try {
            newApp.showNewAppointment(primaryStage, new UserModel("balle","okpwqeropgkr","stud.ntnu.no","balle","svin","911"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
