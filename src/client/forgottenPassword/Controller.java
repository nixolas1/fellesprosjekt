package client.forgottenPassword;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;

public class Controller {

    @FXML private TextField username;
    @FXML private ChoiceBox domain;
    @FXML private Label errorText;

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
            errorText.setText("Nytt passord sendt på epost");
            errorText.setTextFill(Color.GREEN);
        } else {
            errorText.setText("Brukernavn inneholder ulovlige tegn");
            System.out.println("Brukernavnet inneholder ulovlige tegn");

        }

    }

    @FXML
    public void cancel(ActionEvent event) {
        // go back to login stage
        System.out.println("Canceling");
    }






}
