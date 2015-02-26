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


public class Controller {

    @FXML private TextField username;
    @FXML private ChoiceBox domain;
    @FXML private Text loginErrorText;
    @FXML private PasswordField password;

    @FXML private Button login, newUser, forgottenPassword;

    private String userNameReg = "^[a-zA-Z0-9_.]*$";
    private String passwordReg = "[\\S ]{6,50}$";


    @FXML
    void initialize() {
        loginErrorText.setText("");
        domain.setItems(FXCollections.observableArrayList("@stud.ntnu.no"));
       /* createValidationListener(username, userNameReg, 30);
        createValidationListener(firstName, nameReg, 30);
        createValidationListener(lastName, nameReg, 30);
        createValidationListener(phone, phoneReg, 8);
        createValidationListener(password1, passwordReg, 50);
        createPassListener(password11);
    */
    }


    public boolean valid(String s, String match, int max) {
        if (s.matches(match) && s.length() <= max) {
            return true;
        }
        return false;
    }



    public boolean txtFieldCheck(TextField field) {
        if (field.getText() != null && !(field.getText().equals(""))) {
            return true;
        }
        return false;
    }

    @FXML
    public void login(ActionEvent event) {
        String user = username.getText();
        String pass = password.getText();
        if (!(valid(user, userNameReg, 30))) {
            // brukernavn ulovlig tegn
            loginErrorText.setText("Brukernavn inneholder ulovlige tegn");
            password.setText("");
            return;
        }
        if (!(valid(pass,passwordReg,50))) {
            password.setText("");
            if(pass.length() < 6) {
                loginErrorText.setText("Passordet er for kort");
            } else {
                loginErrorText.setText("Feil passord");
            }
            return;
        }
        // sjekk database
        System.out.println("Sjekk databasen");

    }



    

}
