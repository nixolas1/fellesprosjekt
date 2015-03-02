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
        client.login.Main.show(Main.stage, "Nytt passord er sendt på mail");
    }

    @FXML public void cancelNewPass(ActionEvent event) {
        //go back to login
        client.login.Main.show(Main.stage);
    }
}
