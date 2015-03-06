package client.newGroup;

//import calendar.Appointment;
import calendar.UserModel;
import client.newAppointment.FxUtil;
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
import java.util.ResourceBundle;

/**
 * Created by Rae on 06.03.15.
 */
public class Controller implements Initializable {

    @FXML
    private TextField name, description;

    @FXML
    private Button cancel, create, add, remove;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox usersComboBox;

    @FXML
    private ListView attendeeList;

    private ArrayList<UserModel> allUsers;
    private ObservableList<String> userInfo;
    private ObservableList<String> attendees;
    UserModel user = new UserModel();

    @FXML
    @Override
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

        create.setDisable(true);
        attendees = FXCollections.observableArrayList();
        attendeeList.setItems(attendees);
        allUsers = calendar.UserModel.getUsersFromDB();
        userInfo = displayUserInfo(allUsers);
        usersComboBox.setItems(userInfo);
        FxUtil.autoCompleteComboBox(usersComboBox, FxUtil.AutoCompleteMode.STARTS_WITH);

        usersComboBox.setItems(FXCollections.observableArrayList(userInfo));
        FxUtil.autoCompleteComboBox(usersComboBox, FxUtil.AutoCompleteMode.STARTS_WITH);

    }



    public ObservableList<String> displayUserInfo(ArrayList<UserModel> users) {
        ObservableList<String> userInfo = FXCollections.observableArrayList();
        for (UserModel user : users) {
            userInfo.add(user.displayInfo());
        }
        return userInfo;
    }

    @FXML
    public void addUser(ActionEvent event) {
        String usr = (String) FxUtil.getComboBoxValue(usersComboBox);
        if (userInfo.contains(usr)){
            attendees.add(usr);
        }
        usersComboBox.getEditor().setText("");
        System.out.println(attendees);
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

  /*  @FXML
    public void createGroup(ActionEvent event) {
        Group group = new Group();
    }*/
}
