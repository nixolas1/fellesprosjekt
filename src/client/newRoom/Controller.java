package client.newRoom;

import client.newRoom.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Rae on 11.03.15.
 */
public class Controller implements Initializable{

    @FXML private TextField name, capacity, from, to;
    @FXML private Button add, remove, create, cancel;
    @FXML private ListView utilityList;
    @FXML private URL location;
    @FXML private ResourceBundle resources;
    @FXML private ComboBox utilityComboBox;


    private ObservableList<String> utilities;
    private String timeRegex = "[\\d]{2}:[\\d]{2}";
    private String numberRegex = "-?\\d+";


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        add.setDisable(true);
        remove.setDisable(true);

        utilityComboBox.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (utilityComboBox.isFocused()) {
                    add.setDisable(false);
                }
            }
        });

        utilityList.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (utilityList.isFocused()) {
                    remove.setDisable(false);
                }
            }
        });

        create.setDisable(true);
        utilities = FXCollections.observableArrayList();
        utilityList.setItems(utilities);

        //todo hente liste med mulige utstyr for rom

    }


    @FXML
    public void addUtility(ActionEvent event) {

    }

    @FXML
    public void removeUtility(ActionEvent event) {

    }

    public boolean validTime(String match) {
        if(from.getText().length()!=5 || to.getText().length()!=5) return false;
        if(!(from.getText().matches(match)) || !(to.getText().matches(match))) return false;
        int h = Integer.parseInt(from.getText(0, 2)), m = Integer.parseInt(from.getText(3, 5));
        int hh = Integer.parseInt(to.getText(0, 2)), mm = Integer.parseInt(to.getText(3, 5));
        if(h>=24 || hh>=24 || m>=60 || mm>=60) return false;
        if(h < hh) return false;
        if(h == hh) {
            if(m > mm) return false;
        }
        return true;
    }

    public boolean checkIfAllValid(){
        Boolean ret = true;
        if(name.getText()==null || name.getText().equals("")) ret = false;
        if (Integer.parseInt(capacity.getText()) <= 0) ret = false;
        if (validTime(timeRegex) == false) ret = false;

        create.setDisable(!ret);
        System.out.println("checkIfAllValid() ret = " +ret);
        return ret;
    }

    @FXML
    public void createRoom(ActionEvent event) {
        if (checkIfAllValid()) {
            String name = this.name.getText();
            if (checkCapacity(capacity)) {
                int capacity = Integer.parseInt(this.capacity.getText());
            }
        }
    }

    public boolean valid(String text, String match, int max, int extra) {
        if(extra==2 && valid(text, match, max,0) && valid(from.getText(), match, max,0)) {
            return updateTimeValid();
        }
        return text.length() <= max && text.matches(match);
    }

    public boolean updateTimeValid() {
        return validTime(timeRegex);
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
                    //if(forceCorrect==3)updateRepeatVisibility(field);
                    System.out.println("VALID: "+ newValue);
                    field.setOpacity(2.0);
                    checkIfAllValid();
                    field.setStyle("-fx-text-inner-color: black; -fx-text-box-border: lightgreen; -fx-focus-color: lightgreen;");
                }
            }
        });
    }

    public boolean checkCapacity(TextField capacity) {
        int roomsize = Integer.parseInt(this.capacity.getText());
        if (roomsize > 0) {

        }
        return false;
    }

}
