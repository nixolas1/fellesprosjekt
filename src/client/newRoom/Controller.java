package client.newRoom;

import calendar.Room;
import calendar.Utility;
import client.*;
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
import java.util.ArrayList;
import java.util.List;
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
        utilities = getUtilitiesFromDB();
        addedUtilities = FXCollections.observableArrayList();
        utilityComboBox.setItems(utilities);
        utilityList.setItems(addedUtilities);


        name.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String s = name.getText();
                if(s.length()>1 && s.length()<50) {
                    name.setStyle("-fx-text-inner-color: black; -fx-text-box-border: lightgreen; -fx-focus-color: lightgreen;");
                    name.setOpacity(2.0);
                } else {
                    name.setStyle("-fx-text-inner-color: red; -fx-text-box-border: red; -fx-focus-color: red;");
                    name.setOpacity(3.0);
                }
                checkIfAllValid();
            }
        });

        capacity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if((!capacity.getText().matches("\\d+")) || Integer.parseInt(capacity.getText())<=0 ) {
                    capacity.setStyle("-fx-text-inner-color: red; -fx-text-box-border: red; -fx-focus-color: red;");
                    capacity.setOpacity(3.0);
                } else {
                    capacity.setStyle("-fx-text-inner-color: black; -fx-text-box-border: lightgreen; -fx-focus-color: lightgreen;");
                    capacity.setOpacity(2.0);
                }
                checkIfAllValid();
            }
        });

        from.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!from.getText().matches(timeRegex)) {
                    from.setStyle("-fx-text-inner-color: red; -fx-text-box-border: red; -fx-focus-color: red;");
                    from.setOpacity(3.0);
                } else {
                    from.setStyle("-fx-text-inner-color: black; -fx-text-box-border: lightgreen; -fx-focus-color: lightgreen;");
                    from.setOpacity(2.0);
                }
                checkIfAllValid();
            }
        });

        to.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if((!to.getText().matches(timeRegex))) {
                    to.setStyle("-fx-text-inner-color: red; -fx-text-box-border: red; -fx-focus-color: red;");
                    to.setOpacity(3.0);
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
            to.setOpacity(3.0);
        } else {
            to.setStyle("-fx-text-inner-color: black; -fx-text-box-border: lightgreen; -fx-focus-color: lightgreen;");
            to.setOpacity(2.0);
        }
    }

    @FXML
    public void addUtility(ActionEvent event) {
        String utility = (String) utilityComboBox.getValue();
        System.out.println(utility);
        if (!(addedUtilities.contains(utility))) {
            addedUtilities.add(utility);
            utilities.remove(utilityComboBox.getValue());
        }
    }

    @FXML
    public void removeUtility(ActionEvent event) {
        String utility = utilityList.getSelectionModel().getSelectedItem().toString();
        addedUtilities.remove(utility);
        utilities.add(utility);
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
        updateTimeValid();
        if(name.getOpacity()!=2.0) ret = false;
        if(from.getOpacity()!=2.0) ret = false;
        if(to.getOpacity()!=2.0) ret = false;
        if(capacity.getOpacity()!=2.0) ret = false;
        System.out.println("Name:"+name.getOpacity());
        System.out.println("from:"+from.getOpacity());
        System.out.println("to:"+to.getOpacity());
        System.out.println("capacity:"+capacity.getOpacity());
        create.setDisable(!ret);
        System.out.println("checkIfAllValid() ret = " +ret);
        return ret;
    }

    @FXML
    public void createRoom(ActionEvent event) {
        if (checkIfAllValid()) {
            String name = this.name.getText();
            int opensAt = (Integer.parseInt(from.getText(0,2))*60)+Integer.parseInt(from.getText(3,5));
            int closesAt = (Integer.parseInt(to.getText(0,2))*60)+Integer.parseInt(to.getText(3,5));
            int capacity = Integer.parseInt(this.capacity.getText());
            //todo make utility objects from Utilities list
            ArrayList<Utility> utilityObjects = makeUtilityObjects();
            Room room = new Room(-1,name,capacity,opensAt,closesAt,utilityObjects);
            System.out.println("Room " + name + " created");
            Boolean reply = Main.createRoom(room).get("reply");
            if(reply) {
                System.out.println("success");
            } else {
                System.out.println("an error occured");
            }
        }
        Main.close();
    }

    public ArrayList<Utility> makeUtilityObjects() {
        if (addedUtilities.size()<=0) return null;
        ArrayList<Utility> uts = new ArrayList<>();
        for (String u : addedUtilities) {
            String name = u.split(",")[0];
            int id = Integer.parseInt(u.split(",")[1].trim());
            uts.add(new Utility(id, name));
        }
        return uts;
    }

    public ObservableList<String> getUtilitiesFromDB() {
        //Fra DB:
        // ID, NAVN
        ArrayList<List<String>> utsFromDB = network.ClientDB.getAllTableRows("Utility",client.Main.socket);
        ObservableList<String> uts = FXCollections.observableArrayList();
        for(List<String> li : utsFromDB) {
            String u = li.get(1) + ", " + li.get(0);
            uts.add(u);
        }
        return uts;
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

    @FXML
    public void cancelCreateRoom(ActionEvent event) {
        client.newRoom.Main.close();
    }

}
