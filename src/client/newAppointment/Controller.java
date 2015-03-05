package client.newAppointment;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import calendar.*;
import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.*;
import javafx.scene.layout.GridPane;
import javafx.stage.*;
import javafx.fxml.*;
import javafx.scene.control.*;



import static java.lang.Integer.parseInt;

public class Controller implements Initializable{


    @FXML
    private TextField title, room, from, to, purpose, repeat;

    @FXML
    private DatePicker date, endDate, stoprepeat;

    @FXML
    private ComboBox usersComboBox;

    @FXML
    private Label stoplabel;

    @FXML
    private Button create, add, remove;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML private ListView attendeeList;


    private ArrayList<UserModel> allUsers;
    private ObservableList<String> userInfo;
    private ObservableList<String> attendees;
    UserModel user = new UserModel();



    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        add.setDisable(true);
        remove.setDisable(true);

        usersComboBox.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (usersComboBox.isFocused()) {
                    add.setDisable(false);
                }
            }
        });

        attendeeList.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (attendeeList.isFocused()) {
                    remove.setDisable(false);
                }
            }
        });


        createValidationListener(room, 0, "[\\w- ]+ [\\d]+", 50);
        createValidationListener(from, 0,   "[\\d]{2}:[\\d]{2}", 5);
        createValidationListener(to,   2,   "[\\d]{2}:[\\d]{2}", 5);
        createValidationListener(purpose, 1, ".*", 50);
        createValidationListener(repeat,  3, "[0-9]*", 3);

        dateValidation(date);
        dateValidation(endDate);
        dateValidation(stoprepeat);

        stoprepeat.setVisible(false);
        stoplabel.setVisible(false);

       /* create.setOnAction(new EventHandler<ActionEvent>() {
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
        });*/

        create.setDisable(true);
        attendees = FXCollections.observableArrayList(); // Listview items
        attendeeList.setItems(attendees); // Adding items to ListView
        allUsers = getUsersFromDB();
        userInfo = displayUserInfo(allUsers); // ComboBox items
        usersComboBox.setItems(userInfo);
        FxUtil.autoCompleteComboBox(usersComboBox, FxUtil.AutoCompleteMode.STARTS_WITH); // AutoCompleteMode ON


        usersComboBox.setItems(FXCollections.observableArrayList(userInfo));
        FxUtil.autoCompleteComboBox(usersComboBox, FxUtil.AutoCompleteMode.STARTS_WITH);

    }

    public static ArrayList<UserModel> getUsersFromDB() {
        return calendar.UserModel.getAllUsers();
    }



    @FXML
    public void addUser(ActionEvent event) {
        String usr = (String) FxUtil.getComboBoxValue(usersComboBox);
        // validating
        if (userInfo.contains(usr)) {
           // String email = usr.split(",")[1].trim();
           // UserModel user = getUserModel(email);
            attendees.add(usr);
            userInfo.remove(usr);
            }
        //FxUtil.resetSelection(usersComboBox);
        usersComboBox.getEditor().setText("");
        System.out.println(attendees);
    }

    // Get UserModel from email
    public UserModel getUserModel(String email) {
        for (UserModel user : allUsers) {
            if(user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    @FXML
    public void removeUser(ActionEvent event) {
        String usr = attendeeList.getSelectionModel().getSelectedItem().toString();
        System.out.println(usr);
        if (attendees.contains(usr)) {
            attendees.remove(usr);
            userInfo.add(usr);
        }
    }

    @FXML
    public void createAppointment(ActionEvent event) {
        if(checkIfAllValid()) {
            // title, purpose, startDate, endDate, room, owner, cal
            String title = this.title.getText();
            String purpose = this.purpose.getText();
            int hrStart = Integer.parseInt(from.getText().split(":")[0]);
            int minStart = Integer.parseInt((from.getText().split(":")[1]));
            int hrEnd = Integer.parseInt((to.getText().split(":")[0]));
            int minEnd = Integer.parseInt(to.getText().split(":")[1]);
            LocalDateTime startDate = this.date.getValue().atTime(hrStart, minStart);
            LocalDateTime endDate = this.endDate.getValue().atTime(hrEnd, minEnd);
            Room room = new Room(1, "test", 1, 0, 24, new ArrayList<Utility>()); // TEST ROOM! TODO get from DB
            UserModel owner = new UserModel(); // todo FIX
            Calendar cal = new Calendar("test", 1); // TEST CAL! TODO get from DB
            Appointment app = new Appointment(title, purpose, startDate, endDate, room, owner, cal);

            //TODO send appointment to server, insert into db
        } else {
            System.out.println("One or more fields INVALID. Data not sent to server.");
            // TODO Error text field?
        }

    }

    public ObservableList<String> displayUserInfo(ArrayList<UserModel> users) {
        ObservableList<String> userInfo = FXCollections.observableArrayList();
        for (UserModel user : users) {
            userInfo.add(user.displayInfo());
        }
        return userInfo;
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
        if("".equals(field.getText()) || field.getText().equals("0")) {
            stoprepeat.setVisible(false);
            stoplabel.setVisible(false);
        }else{
            stoprepeat.setVisible(true);
            stoplabel.setVisible(true);
        }
    }

}