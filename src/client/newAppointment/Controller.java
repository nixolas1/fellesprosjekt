package client.newAppointment;

//import client.newAppointment.Main;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import static java.lang.Integer.parseInt;

public class Controller {


    @FXML
    private TextField room, from, to, purpose, repeat;

    @FXML
    private DatePicker date, stoprepeat;

    @FXML
    private Label stoplabel;

    @FXML
    private Button create;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void initialize() {

        createValidationListener(room, 0,   "[\\w- ]+ [\\d]+", 50);
        createValidationListener(from, 0,   "[\\d]{2}:[\\d]{2}", 5);
        createValidationListener(to,   2,   "[\\d]{2}:[\\d]{2}", 5);
        createValidationListener(purpose, 1, ".*", 50);
        createValidationListener(repeat,  3, "[0-9]*", 3);

        dateValidation(date);
        dateValidation(stoprepeat);

        stoprepeat.setVisible(false);
        stoplabel.setVisible(false);

        create.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(checkIfAllValid()) {
                    System.out.println("Created");
                    Platform.exit();

                    System.exit(0);
                }
                else
                    System.out.println("Some fields are invalid");
            }
        });

        create.setDisable(true);

    }

    public boolean checkIfAllValid(){
        Boolean ret = true;
        if(room.getOpacity()!=2.0) ret = false;
        if(from.getOpacity()!=2.0) ret = false;
        if(to.getOpacity()!=2.0)   ret = false;
        if(date.getOpacity()!=2.0) ret = false;
        if(stoprepeat.getOpacity()==3.0) ret = false;

        create.setDisable(!ret);
        return ret;
    }

    public boolean valid(String text, String match, int max, int extra) {
        if(extra==2 && valid(text, match, max,0) && valid(from.getText(), match, max,0)){
            int h = parseInt(from.getText(0, 2)), m = parseInt(from.getText(3, 5));
            int hh = parseInt(text.substring(0, 2)), mm = parseInt(text.substring(3, 5));
            if(hh<=h && mm<=m)return false;
        }
        return text.length() <= max && text.matches(match);
    }

    public void dateValidation(final DatePicker field) {
        field.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                LocalDate date = field.getValue();
                if (date == null || date.isBefore(LocalDate.now())) {
                    field.setStyle("-fx-text-inner-color: red;");
                    field.setOpacity(3.0);
                    checkIfAllValid();
                } else {
                    field.setStyle("-fx-text-inner-color: green;");
                    field.setOpacity(2.0);
                    checkIfAllValid();
                }
            }
        });
    }

    public void createValidationListener(final TextField field, final int forceCorrect, final String match, final int max){
        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String oldValue, String newValue) {
                if (!valid(newValue, match, max, forceCorrect)) {
                    field.setStyle("-fx-text-inner-color: red; -fx-text-box-border: red; -fx-focus-color: red;");
                    if(forceCorrect == 1 || forceCorrect==3) field.setText(oldValue);
                    System.out.println("Invalid:"+ newValue);
                    field.setOpacity(3.0);
                    checkIfAllValid();
                }
                else{
                    if(forceCorrect==3)updateRepeatVisibility(field);
                    System.out.println("VALID: "+ newValue);
                    field.setOpacity(2.0);
                    checkIfAllValid();
                    field.setStyle("-fx-text-inner-color: black; -fx-text-box-border: lightgreen; -fx-focus-color: lightgreen;");
                }
            }
        });
    }



    public void updateRepeatVisibility(TextField field){
        if("".equals(field.getText())) {
            stoprepeat.setVisible(false);
            stoplabel.setVisible(false);
        }else{
            stoprepeat.setVisible(true);
            stoplabel.setVisible(true);
        }
    }

}
