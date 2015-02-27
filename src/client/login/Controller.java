package client.login;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import network.Query;
import network.ThreadClient;
import security.Crypto;

import java.io.Serializable;
import java.util.Hashtable;


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
        loginErrorText.setText("");
        domain.setItems(FXCollections.observableArrayList("@stud.ntnu.no"));
    }


    public boolean valid(String s, String match, int max) {
        if (s.matches(match) && s.length() <= max) {
            return true;
        }
        return false;
    }


    @FXML
    public void login(ActionEvent event) {
        final String user = username.getText();
        final String pass = password.getText();
        final String domainText = domain.getValue().toString();
        if (!(valid(user, userNameReg, 30))) {
            // brukernavn ulovlig tegn
            loginErrorText.setText("Brukernavn inneholder ulovlige tegn");
            password.setText("");
            return;
        }
        if (!(valid(pass,passwordReg,50))) {
            password.setText("");
            if(pass.length() < 4) {
                loginErrorText.setText("Passordet er for kort");
            } else {
                loginErrorText.setText("Feil passord");
            }
            return;
        }

        if(Main.checkLogin(user, pass, domainText)){
            //riktig!
            System.out.println("Successful login!");
        }
        else{
            System.out.println("Wrong username or pass.");
        }

    }

    @FXML
    public void newUser(ActionEvent event) {
        // go to newUser stage


    }

    @FXML
    public void forgottenPass(ActionEvent event) {
        // go to forgottenPassword stage
    }



    

}