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
    private ObservableList<String> addedUtilities;
    private String timeRegex = "[\\d]{2}:[\\d]{2}";
    private String numberRegex = "-?\\d+";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        create.setDisable(true);
        utilityList.setItems(addedUtilities);

        name.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String s = name.getText();
                if(s.length()>1 && s.length()<30) {
                    name.setStyle("-fx-text-inner-color: black; -fx-text-box-border: lightgreen; -fx-focus-color: lightgreen;");
                    name.setOpacity(2.0);
                } else {
                    name.setStyle("-fx-text-inner-color: red; -fx-text-box-border: red; -fx-focus-color: red;");
                    name.setOpacity(1.0);
                }
                checkIfAllValid();
            }
        });

        capacity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if((!capacity.getText().matches("[0-9]")) || Integer.parseInt(capacity.getText())<0) {
                    capacity.setStyle("-fx-text-inner-color: red; -fx-text-box-border: red; -fx-focus-color: red;");
                    capacity.setOpacity(2.0);
                } else {
                    capacity.setStyle("-fx-text-inner-color: black; -fx-text-box-border: lightgreen; -fx-focus-color: lightgreen;");
                    capacity.setOpacity(1.0);
                }
                checkIfAllValid();
            }
        });

        from.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!from.getText().matches(timeRegex)) {
                    from.setStyle("-fx-text-inner-color: red; -fx-text-box-border: red; -fx-focus-color: red;");
                    from.setOpacity(2.0);
                } else {
                    from.setStyle("-fx-text-inner-color: black; -fx-text-box-border: lightgreen; -fx-focus-color: lightgreen;");
                    from.setOpacity(1.0);
                }
                checkIfAllValid();
            }
        });

        to.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if((!to.getText().matches(timeRegex))) {
                    to.setStyle("-fx-text-inner-color: red; -fx-text-box-border: red; -fx-focus-color: red;");
                    to.setOpacity(2.0);
                } else {
                    toValidation();

                }
                checkIfAllValid();
            }
        });

    }

    public void toValidation() {
        int toH = Integer.parseInt(to.getText(0,2)), toM = Integer.parseInt((to.getText(3,5)));
        int fromH = Integer.parseInt(from.getText(0,2)), fromM = Integer.parseInt(from.getText(3,5));
        if(fromH > toH || (fromH==toH && fromM>toM)) {
            to.setStyle("-fx-text-inner-color: red; -fx-text-box-border: red; -fx-focus-color: red;");
            to.setOpacity(2.0);
        } else {
            to.setStyle("-fx-text-inner-color: black; -fx-text-box-border: lightgreen; -fx-focus-color: lightgreen;");
            to.setOpacity(1.0);
        }
    }

    @FXML
    public void addUtility(ActionEvent event) {
        // todo
    }

    @FXML
    public void removeUtility(ActionEvent event) {
        // todo
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
        if(name.getOpacity()!=1.0) ret = false;
        if(from.getOpacity()!=1.0) ret = false;
        if(to.getOpacity()!=1.0) ret = false;

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

    @FXML
    public void cancelCreateRoom(ActionEvent event) {
        Main.closeStage();
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


    public boolean checkCapacity(TextField capacity) {
        int roomsize = Integer.parseInt(this.capacity.getText());
        if (roomsize > 0) {

        }
        return false;
    }

}
