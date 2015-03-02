package client.login;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
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

        Hashtable<String, Boolean> response = Main.checkLogin(user, pass, domainText);

        if(response.get("reply")){
            //riktig!
            System.out.println("Successful login!");
            loginErrorText.setText("Riktig! Du blir nå sendt til kalenderen din.");
        }
        else{
            try {
                if (response.get("data")) {
                    System.out.println("Wrong username or domain.");
                    loginErrorText.setText("Feil brukernavn eller domene");
                } else {
                    System.out.println("Wrong password");
                    loginErrorText.setText("Feil passord");
                }
            }
            catch(NullPointerException e){
                System.out.println("Invalid data");
                loginErrorText.setText("ERROR");
            }
            password.setText("");
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
