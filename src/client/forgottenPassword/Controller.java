package client.forgottenPassword;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;


/**
 * Created by jonaslochsen on 24.02.15.
 */
public class Controller {

    @FXML private TextField username;

    @FXML private ChoiceBox domain;

    @FXML private Button sendPassword, cancel;


    @FXML public void sendNewPassword(ActionEvent event) {
        //Sender nytt passord på mail og går tilbake til LoginView
    }

    @FXML public void cancelNewPass(ActionEvent event) {
        //Går tilbake til LoginView
    }
}
