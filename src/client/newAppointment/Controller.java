package client.newAppointment;

//import client.newAppointment.Main;
import java.lang.reflect.Array;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import calendar.UserModel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
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
    private TextField room, from, to, purpose, repeat;

    @FXML
    private DatePicker date, stoprepeat;

    @FXML
    private ComboBox usersComboBox;

    @FXML
    private Label stoplabel;

    @FXML
    private Button create, add;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


    private Stage myParent;
    private Stage newAppointmentStage;
    UserModel user = new UserModel();

    public void showNewAppointment(Stage parentStage, UserModel loggedUser) {
        this.myParent = parentStage;
        user=loggedUser;
        try {
            newAppointmentStage = new Stage();
            GridPane pane = (GridPane) FXMLLoader.load(Controller.class.getResource("view.fxml"));
            Scene scene = new Scene(pane);
            newAppointmentStage.setScene(scene);
            newAppointmentStage.setTitle("Ny avtale");
            newAppointmentStage.initOwner(this.myParent);
            newAppointmentStage.initModality(Modality.WINDOW_MODAL);
            newAppointmentStage.show();
        } catch (Exception ex) {
            System.out.println("Exception found in newAppointment");
            ex.printStackTrace();
        }
    }

    private ArrayList<UserModel> allUsers;
    private ArrayList<UserModel> addedUsers;
    private ArrayList<String> userInfo;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        addedUsers = new ArrayList<>();

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

        allUsers = getUsersFromDB();

        userInfo = displayUserInfo(allUsers);

        usersComboBox.setItems(FXCollections.observableArrayList(userInfo));
        FxUtil.autoCompleteComboBox(usersComboBox, FxUtil.AutoCompleteMode.STARTS_WITH);

    }

    public ArrayList<UserModel> getUsersFromDB() {
        // Testing
        ArrayList<UserModel> users = new ArrayList<>();
        UserModel user1 = new UserModel("User1","passord123","stud.ntnu.no","Fornavn1","Etternavn1","12345678");
        UserModel user2 = new UserModel("User2","passord321","stud.ntnu.no","Fornavn2","Etternavn2","87654321");
        UserModel user3 = new UserModel("User3","passord111","stud.ntnu.no","Fornavn3","Etternavn3","91919191");
        users.add(user1);
        users.add(user2);
        users.add(user3);
        return users;
    }


    @FXML
    public void addUser(ActionEvent event) {
        String usr = (String) FxUtil.getComboBoxValue(usersComboBox);
        // validate
        if (userInfo.contains(usr)) {
            String email = usr.split(",")[1].trim();
            UserModel user = getUserModel(email);
            if (!(addedUsers.contains(user))) {
                addedUsers.add(user);
            }
        }
        FxUtil.resetSelection(usersComboBox);
        System.out.println(addedUsers);
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

    public ArrayList<String> displayUserInfo(ArrayList<UserModel> users) {
        ArrayList<String> userInfo = new ArrayList<>();
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
        if("".equals(field.getText())) {
            stoprepeat.setVisible(false);
            stoplabel.setVisible(false);
        }else{
            stoprepeat.setVisible(true);
            stoplabel.setVisible(true);
        }
    }

}
