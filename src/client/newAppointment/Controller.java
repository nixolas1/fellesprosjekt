package client.newAppointment;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import calendar.*;
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
import javafx.scene.control.*;
import network.Query;

public class Controller implements Initializable{


    @FXML
    private TextField title, from, to, description, repeat, locationDescription;

    @FXML
    private DatePicker date, endDate, stoprepeat;

    @FXML
    private ComboBox usersComboBox, room, groupComboBox;

    @FXML
    private Label stoplabel, roomOrLocation, timeLabel, toLabel;

    @FXML private CheckBox allDay, otherLocation;

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
    private ArrayList<Room> rooms;
    UserModel user = new UserModel(); // todo loggedUser?
    private String timeRegex = "[\\d]{2}:[\\d]{2}";



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
                otherLocation.setSelected(false);
                if (tg.getSelectedToggle() != null) {
                    if (work.isSelected()) {
                        otherLocation.setVisible(true);
                        locationDescription.setVisible(false);
                        room.setVisible(true);
                        roomOrLocation.setText("Rom");
                    }
                    if (personal.isSelected()) {
                        locationDescription.setVisible(true);
                        otherLocation.setVisible(false);
                        room.setVisible(false);
                        roomOrLocation.setText("Sted");
                        locationDescription.setText("");
                    }
                }
            }
        });

        room.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                checkIfAllValid();
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
                checkIfAllValid();
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

        otherLocation.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                checkIfAllValid();
                if(otherLocation.isSelected()) {
                    room.setVisible(false);
                    locationDescription.setVisible(true);
                    roomOrLocation.setText("Sted");
                } else {
                    room.setVisible(true);
                    locationDescription.setVisible(false);
                    roomOrLocation.setText("Rom");
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
        rooms = getRooms();
        room.setItems(FXCollections.observableArrayList(rooms));
        userInfo = displayUserInfo(allUsers); // ComboBox items
        usersComboBox.setItems(userInfo);

        //allGroups = getGroupsFromDB();
        allGroups = new ArrayList<>();
        allGroups.add(new Group(1,"new",new ArrayList<UserModel>()));
        allGroups.add(new Group(2,"TestGroup",new ArrayList<UserModel>()));
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
            int hrStart = 00;
            int minStart = 00;
            int hrEnd = 23;
            int minEnd = 59;
            if(!allDay.isSelected()) {
                hrStart = Integer.parseInt(from.getText().split(":")[0]);
                minStart = Integer.parseInt((from.getText().split(":")[1]));
                hrEnd = Integer.parseInt((to.getText().split(":")[0]));
                minEnd = Integer.parseInt(to.getText().split(":")[1]);
            }
            LocalDateTime startDate = this.date.getValue().atTime(hrStart, minStart);
            LocalDateTime endDate = this.endDate.getValue().atTime(hrEnd, minEnd);
            Room room = null;
            String location = null;
            int repeat = Integer.parseInt(this.repeat.getText());
            if((work.isSelected() && otherLocation.isSelected()) || personal.isSelected()) {
                location = locationDescription.getText();
            } else {
                room = new Room(1, "test", 1, 0, 23, new ArrayList<Utility>()); // TEST ROOM! TODO get from DB
            }
            UserModel owner = new UserModel(); // todo FIX
            calendar.Calendar cal = new calendar.Calendar("test"); // TEST CAL! TODO get from DB
            Appointment app = new Appointment(getAppointmentId(),title,description,startDate,endDate,room,owner,cal,repeat,stoprepeat.getValue(),location);
            System.out.println(app.displayInfo());
            Hashtable<String, Boolean> response = client.Main.socket.send(new Query("newAppointment", app)).data;
            if(response.get("reply"))
                System.out.println("Appointment created\n"+app.displayInfo());
            else
                System.out.println("Server could not create the appointment.");

        } else {
            System.out.println("One or more fields are INVALID. Data not sent to server.");
        }

    }

    public ArrayList<Room> getRooms() {
        // todo: Get all rooms from server
        return new ArrayList<Room>(Arrays.asList(new Room(1,"Rom1",3,1,12), new Room(2,"Room321",5,9,15), new Room(3,"R1",500,8,20)));
    }
    public int getAppointmentId() {
        // todo: Get ID from server
        return -1;
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

        if(title.getText()==null || title.getText().equals("")) ret = false;
        if(description.getText()==null || description.getText()=="") ret = false;
        if(locationDescription.getText()==null || locationDescription.getText()=="") ret = false;
        if(date.getValue()==null || date.getValue().toString().equals("")) ret=false;
        if(endDate.getValue()==null || endDate.getValue().toString().equals("")) {
            endDate.setValue(date.getValue());
        }
        if(dateIsAfter(endDate, date) || dateIsAfter(stoprepeat, date)) {
            ret = false;
            System.out.println("date shit in checkIfAllValid()");
        }
        if(work.isSelected()) {
            if (otherLocation.isSelected() && locationDescription.getOpacity() != 2.0) {
                ret = false;
                System.out.println("Room problem [WORK only]");
            }
          /*  if(!otherLocation.isSelected() && (room.getValue.equals(null) || room.getValue().equals(""))) {
                ret = false;
            }*/

        }
        if(personal.isSelected() && (locationDescription.getText().equals("") || locationDescription.getText().equals(null))) {
            ret = false;
            System.out.println("Location problem [PERSONAL only]");
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
        // todo room
        if (description.getText().equals("")) ret = false;
        /*if(work.isSelected() && !otherLocation.isSelected()) {
            if(room.getValue().equals(null) || room.getValue().equals("")) {
                ret = false;
            }
        }*/
        if((work.isSelected() && otherLocation.isSelected()) || personal.isSelected()) {
            if (locationDescription.getText().equals("")) {
                ret = false;
            }
        }

        create.setDisable(!ret);
        System.out.println("checkIfAllValid() ret = " +ret);
        return ret;
    }

    public boolean valid(String text, String match, int max, int extra) {
        if(extra==2 && valid(text, match, max,0) && valid(from.getText(), match, max,0)) {
            return updateTimeValid();
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
                if(!(to.getText()==null || to.getText().equals(""))) {
                    if (!updateTimeValid()) {
                        to.setStyle("-fx-text-inner-color: red; -fx-text-box-border: red; -fx-focus-color: red;");
                    } else {
                        to.setStyle("-fx-text-inner-color: black; -fx-text-box-border: lightgreen; -fx-focus-color: lightgreen;");
                    }
                }
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

    public boolean updateTimeValid() {
        return validTime(timeRegex);
    }

    public boolean validTime(String match) {
        if(from.getText().length()!=5 || to.getText().length()!=5) return false;
        if(!(from.getText().matches(match)) || !(to.getText().matches(match))) return false;
        int h = Integer.parseInt(from.getText(0, 2)), m = Integer.parseInt(from.getText(3, 5));
        int hh = Integer.parseInt(to.getText(0, 2)), mm = Integer.parseInt(to.getText(3, 5));
        if(h>=24 || hh>=24 || m>=60 || mm>=60) return false;
        if(date.getValue().isBefore(endDate.getValue())) {
            return true;
        }
        if (date.getValue().equals(endDate.getValue())) {
            if(h > hh) return false;
            if(h == hh) {
                if(m > mm) return false;
            }
        }
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