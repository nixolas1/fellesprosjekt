package client.forgottenPassword;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.awt.event.ActionEvent;


/**
 * Created by jonaslochsen on 24.02.15.
 */
public class Controller {

    @FXML
    private TextField username;

    @FXML
    private ChoiceBox domain;

    @FXML
    private Button sendPassword, cancel;


    public void sendNewPassword(ActionEvent event) {

    }

    public void cancel(ActionEvent event) {

    }

}
