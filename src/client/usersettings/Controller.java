package client.userSettings;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import network.Query;
import java.util.Hashtable;
import javafx.scene.paint.Color;


public class Controller{

    @FXML private TextField username;
    @FXML private ChoiceBox domain;
    @FXML private Text loginErrorText;
    @FXML private PasswordField password;

    @FXML private Button login, newUser, forgottenPassword;

    private String userNameReg = "^[a-zA-Z0-9_.]*$";
    private String passwordReg = "[\\S ]{4,50}$";


    @FXML
    void initialize() {

        System.out.println("asd");
    }


    public boolean valid(String s, String match, int max) {
        if (s.matches(match) && s.length() <= max) {
            return true;
        }
        return false;
    }


    public void cancelChanges(){
        System.out.println("do stuff");
    }

    public void saveChanges(){
        System.out.println("something");
    }






}
