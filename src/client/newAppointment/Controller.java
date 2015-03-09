package client.newAppointment;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import calendar.*;
import calendar.Group;
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
    private TextField title, from, to, description, repeat, locationDescription;

    @FXML
    private DatePicker date, endDate, stoprepeat;

    @FXML
    private ComboBox usersComboBox, room, groupComboBox;

    @FXML
    private Label stoplabel, roomOrLocation, timeLabel, toLabel;

    @FXML private CheckBox allDay;

    @FXML private RadioButton work, personal;

    @FXML
    private Button create, add, remove, addGroup, removeGroup;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML private ListView attendeeList, groupList;


    private ArrayList<UserModel> allUsers;
    private ObservableList<String> userInfo;
    private ObservableList<String> attendees;
    private ArrayList<Group> allGroups;
    private ObservableList<String> groupInfo;
    private ObservableList<String> addedGroups;
    private ArrayList<Room> allRooms;
    UserModel user = new UserModel(); // todo loggedUser?



    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        add.setDisable(true);
        remove.setDisable(true);
        locationDescription.setVisible(false);

        ToggleGroup tg = new ToggleGroup();
        work.setToggleGroup(tg);
        personal.setToggleGroup(tg);

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

        allGroups = getGroupsFromDB();
        addedGroups = FXCollections.observableArrayList();
        groupList.setItems(addedGroups);
        groupInfo = displayGroupInfo(allGroups);
        groupComboBox.setItems(groupInfo);

        FxUtil.autoCompleteComboBox(usersComboBox, FxUtil.AutoCompleteMode.STARTS_WITH); // AutoCompleteMode ON
        FxUtil.autoCompleteComboBox(groupComboBox, FxUtil.AutoCompleteMode.STARTS_WITH);



    }

    public static ArrayList<UserModel> getUsersFromDB() {
        return calendar.UserModel.getAllUsers();
    }

    public static ArrayList<Group> getGroupsFromDB() {
        return calendar.Group.getAllGroups();
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
    public void addGroup(ActionEvent event) {
        String grp = (String) FxUtil.getComboBoxValue(groupComboBox);
        if(groupInfo.contains(grp)) {
            addedGroups.add(grp);
            groupInfo.remove(grp);
        }
        groupComboBox.getEditor().setText("");
        System.out.println(addedGroups);
    }

    @FXML
    public void removeGroup(ActionEvent event) {
        String grp = groupList.getSelectionModel().getSelectedItem().toString();
        System.out.println(grp);
        if (addedGroups.contains(grp)) {
            addedGroups.remove(grp);
            groupInfo.add(grp);
            System.out.println("Group " + grp + " removed.");
        }
    }

    // Get group from ID
    public Group getGroup(int id) {
        for (Group grp : allGroups) {
            if(grp.getId() == id) return grp;
        }
        return null;
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
            Calendar cal = new Calendar("test", 1); // TEST CAL! TODO get from DB
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

    public ObservableList<String> displayGroupInfo(ArrayList<Group> groups) {
        ObservableList<String> groupInfo = FXCollections.observableArrayList();
        for (Group grp : groups) {
            groupInfo.add(grp.displayInfo());
        }
        return groupInfo;
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
            if (date.getValue()!=null && endDate.getValue()!=null) {
                if (date.getValue().isBefore(endDate.getValue())) return true;
            }
            int h = parseInt(from.getText(0, 2)), m = parseInt(from.getText(3, 5));
            int hh = parseInt(text.substring(0, 2)), mm = parseInt(text.substring(3, 5));
            if (hh <= h && mm <= m) {
                return false;
            }
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
                    updateTimeValid();
                    checkIfAllValid();
                }
                    else {
                    field.setStyle("-fx-text-inner-color: green;");
                    field.setOpacity(2.0);
                    updateTimeValid();
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

    public void updateTimeValid() {
        if(date.getValue()==null || endDate.getValue()==null) {
            System.out.println("date / endDate == NULL");
            return;
        }
        if(date.getValue().isBefore(endDate.getValue()) && !(date.getValue().equals(endDate.getValue()))) {
            System.out.println("den andre jævla ifen i updateTimeValid line 396");
            if(validTime()){
                System.out.println("Valid L399");
                to.setOpacity(2.0);
                to.setStyle("fx-text-inner-color: black; -fx-text-box-border: lightgreen; -fx-focus-color: lightgreen;");
            } else {
                System.out.println("Invalid L403");
                to.setOpacity(3.0);
                to.setStyle("-fx-text-inner-color: red; -fx-text-box-border: red; -fx-focus-color: red;");
            }
        } else {
            return;
        }
    }

    public boolean validTime() {
        if(from.getText().length()!=5 || to.getText().length()!=5) return false;
        int h = Integer.parseInt(from.getText(0, 2)), m = Integer.parseInt(from.getText(3, 5));
        int hh = Integer.parseInt(to.getText(0, 2)), mm = Integer.parseInt(to.getText(3, 5));
        if(h>=24 || hh>=24 || m>=60 || mm>=60) return false;
        return true;
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