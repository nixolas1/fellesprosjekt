package client.forgottenPassword;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.awt.event.ActionEvent;

public class Controller {

    @FXML private TextField username;
    @FXML private ChoiceBox domain;
    @FXML private Text errorText;

    @FXML private Button sendPassword, cancel;

    private String userNameReg = "^[a-zA-Z0-9_.]*$";


    @FXML
    void initialize() {
        errorText.setText("");
        domain.setItems(FXCollections.observableArrayList("@stud.ntnu.no"));
        domain.setValue("@stud.ntnu.no");
    }


    public boolean valid(String s, String match, int max) {
        if (s.matches(match) && s.length() <= max) {
            return true;
        }
        return false;
    }


    @FXML
    public void sendNewPassword(ActionEvent event) {
        if (valid(username.getText(), userNameReg, 30)) {
            // Sjekk om brukernavnet er i databasen,
            // Generer nytt passord og send
            System.out.println("Send email");
        } else {
            errorText.setText("Brukernavn inneholder ulovlige tegn");

        }

    }

    @FXML
    public void cancel(ActionEvent event) {
        // go back to login
    }






}
