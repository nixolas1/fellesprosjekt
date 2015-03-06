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
import calendar.Group;

import javax.swing.*;
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
    private ObservableList<String> groupmembers;
    private ArrayList<UserModel> groupmembersUserModel;
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
        groupmembers = FXCollections.observableArrayList();
        attendeeList.setItems(groupmembers);
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
        create.setDisable(false);
        String usr = (String) FxUtil.getComboBoxValue(usersComboBox);
        if (userInfo.contains(usr)){
            groupmembers.add(usr);
        }
        usersComboBox.getEditor().setText("");
        System.out.println(groupmembers);
    }


    @FXML
    public void removeUser(ActionEvent event) {
        String usr = attendeeList.getSelectionModel().getSelectedItem().toString();
        System.out.println(usr);
        if (groupmembers.contains(usr)) {
            groupmembers.remove(usr);
            userInfo.add(usr);
        }
    }

    @FXML
    public void createGroup(ActionEvent event) {
        String groupname = name.getText();
        for (String user : groupmembers) {
            String email = user.split(",")[1].trim();
            UserModel usr = getUserModel(email);
            groupmembersUserModel.add(usr);
        }
        System.out.println(groupmembersUserModel);
        Group group = new Group(groupname, groupmembersUserModel);
        //toDo - må addes i database, sondreeeee
    }
    public UserModel getUserModel(String email) {
        for (UserModel user : allUsers) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }



        /*ArrayList<UserModel> groupMembers = new ArrayList<>(users.size());
        for (UserModel user : users){
            groupMembers.add(user);
        }*/

        //Group group = new Group();


    /*public isValid() {
        if
    }*/
}
