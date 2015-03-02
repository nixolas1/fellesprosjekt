package client.forgottenPassword;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;

import java.util.Hashtable;


/**
 * Created by jonaslochsen on 24.02.15.
 */
public class Controller {

    @FXML private TextField username;

    @FXML private ChoiceBox domain;

    @FXML private Button sendPassword, cancel;

    @FXML
    void initialize() {
        domain.setItems(FXCollections.observableArrayList("stud.ntnu.no"));
    }


    @FXML public void sendNewPassword(ActionEvent event) {
        String domainText = domain.getValue().toString();
        String user = username.getText();
        Hashtable<String, Boolean> response = Main.resetPass(user, domainText);

        if(response.get("reply")){
            client.login.Main.show(Main.stage, "Nytt passord er sendt på mail");
        }
        else{
            //error tekst her
            System.out.println("Errorxzz");
        }

    }

    @FXML public void cancelNewPass(ActionEvent event) {
        //go back to login
        client.login.Main.show(Main.stage);
    }
}
