package client.forgottenPassword;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    @FXML private Label errorText;

    private String userNameReg = "^[a-zA-Z0-9_.]*$";


    @FXML private Button sendPassword, cancel;

    @FXML
    void initialize() {
        errorText.setText("");
        domain.setItems(FXCollections.observableArrayList("stud.ntnu.no"));
        domain.setValue("stud.ntnu.no");
    }

    public boolean valid(String s, String match, int max) {
        if (s.matches(match) && s.length() <= max) {
            return true;
        }
        return false;
    }

    @FXML public void sendNewPassword(ActionEvent event) {
        if (valid(username.getText(), userNameReg, 30)) {
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
        } else {
            errorText.setText("Brukernavn inneholder ulovlige tegn");
            System.out.println("Brukernavnet inneholder ulovlige tegn");

        }

    }

    @FXML public void cancelNewPass(ActionEvent event) {
        //go back to login
        client.login.Main.show(Main.stage);
    }
}
