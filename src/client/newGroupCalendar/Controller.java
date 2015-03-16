package client.newGroupCalendar;

//import calendar.Appointment;
import calendar.Calendar;
import calendar.UserModel;
import client.newAppointment.FxUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import calendar.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Rae on 06.03.15.
 */
public class Controller implements Initializable {

    @FXML
    private TextField name;

    @FXML private TextArea description;

    @FXML
    private Button cancel, create, add, remove;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox usersComboBox;

    @FXML
    private ListView groupList;

    private ArrayList<UserModel> allUsers;
    private ObservableList<String> userInfo;
    private ObservableList<String> groupmembers;
    private ArrayList<UserModel> groupmembersUserModel;
    UserModel user;
    boolean nameValid = false, descriptionValid = false, groupValid = false;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user = Main.getLoggedUser();

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

        groupList.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (groupList.isFocused()) {
                    remove.setDisable(false);
                }
            }
        });

        name.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String s = name.getText();
                if (s.length() > 0 && s.length() < 50) {
                    name.setStyle("-fx-text-inner-color: black; -fx-text-box-border: lightgreen; -fx-focus-color: lightgreen;");
                    nameValid = true;
                } else {
                    name.setStyle("-fx-text-inner-color: red; -fx-text-box-border: red; -fx-focus-color: red;");
                    nameValid = false;
                }
                checkIfAllValid();
            }
        });

        description.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String s = description.getText();
                if (s.length() > 0 && s.length() < 1000) {
                    description.setStyle("-fx-text-inner-color: black; -fx-text-box-border: lightgreen; -fx-focus-color: lightgreen;");
                    descriptionValid = true;
                } else {
                    description.setStyle("-fx-text-inner-color: red; -fx-text-box-border: red; -fx-focus-color: red;");
                    descriptionValid = false;
                }
                checkIfAllValid();
            }
        });

        create.setDisable(true);
        groupmembers = FXCollections.observableArrayList();
        groupList.setItems(groupmembers);
        groupmembers.add(user.getFirstName() + " " + user.getLastName() + ", " + user.getEmail()); // SELF
        allUsers = calendar.UserModel.getAllUsers();
        userInfo = displayUserInfo(allUsers);
        usersComboBox.setItems(userInfo);
        FxUtil.autoCompleteComboBox(usersComboBox, FxUtil.AutoCompleteMode.STARTS_WITH);

        usersComboBox.setItems(FXCollections.observableArrayList(userInfo));
        FxUtil.autoCompleteComboBox(usersComboBox, FxUtil.AutoCompleteMode.STARTS_WITH);

        groupmembersUserModel = new ArrayList<>();

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
        if (userInfo.contains(usr) && !groupmembers.contains(usr)){
            groupmembers.add(usr);
            userInfo.remove(usr);
        }
        usersComboBox.getEditor().setText("");
        System.out.println(groupmembers);
        if(groupmembers.size()>0) groupValid = true;
        checkIfAllValid();
    }


    @FXML
    public void removeUser(ActionEvent event) {
        String usr = groupList.getSelectionModel().getSelectedItem().toString();
        System.out.println(usr);
        if (groupmembers.contains(usr)) {
            groupmembers.remove(usr);
            userInfo.add(usr);
        }
        if(groupmembers.size()<1) groupValid = false;
        checkIfAllValid();
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
        Calendar cal = new Calendar(groupname, groupmembersUserModel);
        cal.setDescription(description.getText());
        Boolean res = Main.createGroup(cal).get("reply");
        if(res) {
            System.out.println("Group '" + groupname + "' created.");
        } else {
            System.out.println("Error occured. Group not created");
        }
        Main.closeStage();

    }

    @FXML
    public void cancel(ActionEvent event) {
        Main.closeStage();
    }

    public UserModel getUserModel(String email) {
        for (UserModel user : allUsers) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public void checkIfAllValid() {
        boolean ret = false;
        if(nameValid && descriptionValid && groupValid) ret = true;
        create.setDisable(!ret);
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
