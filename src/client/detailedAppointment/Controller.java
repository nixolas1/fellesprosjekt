package client.detailedAppointment;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import calendar.*;

import client.*;
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
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import server.AppointmentLogic;


import static java.lang.Integer.parseInt;

public class Controller implements Initializable{


    @FXML
    private TextField title, from, to, description, repeat, locationDescription;

    @FXML
    private DatePicker date, endDate, stoprepeat;

    @FXML
    private ComboBox usersComboBox, room;

    @FXML
    private Label stoplabel, roomOrLocation, timeLabel, toLabel;

    @FXML private CheckBox allDay;

    @FXML private RadioButton work, personal;

    @FXML
    private Button create, add, remove;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML private ListView attendeeList;

    @FXML private Text headTitle;


    private ArrayList<UserModel> allUsers;
    private ObservableList<String> userInfo;
    private ObservableList<String> attendees;
    private ArrayList<Room> allRooms;
    UserModel user = new UserModel(); // todo loggedUser?
    private Appointment app;



    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        add.setDisable(true);
        remove.setDisable(true);
        locationDescription.setVisible(false);

        ToggleGroup tg = new ToggleGroup();
        work.setToggleGroup(tg);
        personal.setToggleGroup(tg);

        app = Main.appointment;
        initializeFields();


        tg.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (tg.getSelectedToggle() != null) {
                    if (work.isSelected()) {
                        locationDescription.setVisible(false);
                        room.setVisible(true);
                        roomOrLocation.setText("Rom");
                    }
                    if (personal.isSelected()) {
                        locationDescription.setVisible(true);
                        room.setVisible(false);
                        roomOrLocation.setText("Sted");
                    }
                }
            }
        });

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

        allDay.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (allDay.isSelected()) {
                    to.setDisable(true);
                    from.setDisable(true);
                    timeLabel.setDisable(true);
                    toLabel.setDisable(true);
                    from.setText("");
                    to.setText("");
                    from.setStyle("-fx-text-inner-color: black;");
                    to.setStyle("-fx-text-inner-color: black;");

                } else {
                    to.setDisable(false);
                    from.setDisable(false);
                    timeLabel.setDisable(false);
                    toLabel.setDisable(false);
                }
            }
        });

        stoprepeat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(dateIsAfter(date,stoprepeat) && dateIsAfter(endDate,stoprepeat)) {
                    stoprepeat.setStyle("-fx-text-inner-color: green;");
                    stoprepeat.setOpacity(2.0);
                } else {
                    stoprepeat.setStyle("-fx-text-inner-color: red;");
                    stoprepeat.setOpacity(3.0);
                }
            }
        });



        //createValidationListener(room, 0, "[\\w- ]+ [\\d]+", 50);
        createValidationListener(from, 0, "[\\d]{2}:[\\d]{2}", 5);
        createValidationListener(to,   2,   "[\\d]{2}:[\\d]{2}", 5);
        createValidationListener(description, 1, ".*", 50);
        createValidationListener(repeat,  3, "[0-9]*", 3);
        createValidationListener(title, 0, ".{0,50}", 50);
        createValidationListener(locationDescription, 0, ".{0,50}",50);

        dateValidation(date);
        dateValidation(endDate);
        // dateValidation(stoprepeat);

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
        allRooms = getAllRooms();
        userInfo = displayUserInfo(allUsers); // ComboBox items
        usersComboBox.setItems(userInfo);
        FxUtil.autoCompleteComboBox(usersComboBox, FxUtil.AutoCompleteMode.STARTS_WITH); // AutoCompleteMode ON


    }

    private boolean isOwner() {
        return true;
    }

    public void initializeFields() {
        ObservableList<Attendee> attendeeObservableList = FXCollections.observableArrayList(app.attendees);
        headTitle.setText(app.getTitle());
        //work or private
        title.setText(app.getTitle());
        date.setValue(app.getStartDate().toLocalDate());
        //allday
        from.setText(app.getStartDate().getHour() + ":" + app.getStartDate().getMinute());
        to.setText(app.getEndDate().getHour() + ":" + app.getEndDate().getMinute());
        endDate.setValue(app.getEndDate().toLocalDate());
        description.setText(app.getPurpose());
        repeat.setText(app.getRepeatEvery() + "");
        //if repeat > 0 stoprepeat.visible
        stoprepeat.setValue(app.getEndRepeatDate());
        attendeeList.setItems(attendeeObservableList);
        //roomOrLocation.setText(app.);
        locationDescription.setText(app.getLocation());
        room.setValue(app.getRoom());

    }

    public static ArrayList<UserModel> getUsersFromDB() {
        return calendar.UserModel.getAllUsers();
    }



    @FXML
    public void addUser(ActionEvent event) {
        String usr = (String) FxUtil.getComboBoxValue(usersComboBox);
        if (userInfo.contains(usr)) {
            // String email = usr.split(",")[1].trim();
            // UserModel user = getUserModel(email);
            attendees.add(usr);
            userInfo.remove(usr);
            //usersComboBox.setItems(userInfo);
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
            String title = this.title.getText();
            String description = this.description.getText();
            int hrStart = Integer.parseInt(from.getText().split(":")[0]);
            int minStart = Integer.parseInt((from.getText().split(":")[1]));
            int hrEnd = Integer.parseInt((to.getText().split(":")[0]));
            int minEnd = Integer.parseInt(to.getText().split(":")[1]);
            LocalDateTime startDate = this.date.getValue().atTime(hrStart, minStart);
            LocalDateTime endDate = this.endDate.getValue().atTime(hrEnd, minEnd);
            Room room = new Room(1, "test", 1, 0, 24, new ArrayList<Utility>()); // TEST ROOM! TODO get from DB
            UserModel owner = new UserModel(); // todo FIX
            Calendar cal = new Calendar("test"); // TEST CAL! TODO get from DB
            Appointment app = new Appointment(getAppointmentId(), title, description, startDate, endDate, room, owner, cal, 0, null, "abc");
            //TODO send appointment to server, insert into db
        } else {
            System.out.println("One or more fields INVALID. Data not sent to server.");
        }

    }

    public ArrayList<Room> getAllRooms() {
        // todo: Get all rooms from server
        return new ArrayList<Room>();
    }
    public int getAppointmentId() {
        // todo: Get ID from server
        return 0;
    }
    public void updateRoomList() {
        // todo: Oppdaterer romlisten som inneholder aktuelle rom, dvs rom med riktig antall plasser ift. deltakere

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
        if(title.getText()==null || title.getText()=="") ret = false;
        if(dateIsAfter(endDate, date) || dateIsAfter(stoprepeat, date)) {
            ret = false;
            System.out.println("date shit in checkIfAllValid()");
        }
        if(work.isSelected() && room.getOpacity()!=2.0) {
            ret = false;
            System.out.println("Room problem [WORK only]");
        }
        if(personal.isSelected() && (locationDescription.getText()=="" || locationDescription.getText()==null)) {
            ret = false;
            System.out.println("Sted problem [PERSONAL only]");
        }
        if(!allDay.isSelected()) {
            if (from.getOpacity() != 2.0) {
                ret = false;
                System.out.println("From problem");
            }
            if (to.getOpacity() != 2.0) {
                ret = false;
                System.out.println("To problem");
            }
        }
        if(date.getOpacity()!=2.0) {
            ret = false;
            System.out.println("Date problem");
        }
        if(stoprepeat.getOpacity()==3.0) {
            ret = false;
            System.out.println("stoprepeat problem");
        }

        create.setDisable(!ret);
        System.out.println("checkIfAllValid() ret = " +ret);
        return ret;
    }

    public boolean valid(String text, String match, int max, int extra) {
        if(extra==2 && valid(text, match, max,0) && valid(from.getText(), match, max,0)){
            if (allDay.isSelected()) return true;
            int h = parseInt(from.getText(0, 2)), m = parseInt(from.getText(3, 5));
            int hh = parseInt(text.substring(0, 2)), mm = parseInt(text.substring(3, 5));
            if (date.getValue()!=null && endDate!=null) {
                if (date.getValue().isBefore(endDate.getValue()) && date.getValue()!=endDate.getValue()) {
                    return true;
                } else {
                    return false;
                }
            }
            if (hh <= h && mm <= m) return false;
            if(hh >= 24 || h >= 24 || m >= 60 || mm >= 60) return false;
        }
        return text.length() <= max && text.matches(match);
    }

    public boolean dateIsAfter(DatePicker date, DatePicker after) {
        if (date.getValue() == null || after.getValue() == null) return false;
        if (after.getValue().isAfter(date.getValue())) return true;
        return false;
    }

    public void dateValidation(final DatePicker field) {
        field.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                LocalDate d = field.getValue();
                if (d == null || d.isBefore(LocalDate.now()) || dateIsAfter(endDate,date)) {
                    field.setStyle("-fx-text-inner-color: red;");
                    field.setOpacity(3.0);
                    checkIfAllValid();
                }
                else {
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