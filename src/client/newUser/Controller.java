package client.newUser;

import calendar.UserModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import network.Query;
import network.ThreadClient;

import java.util.Hashtable;


public class Controller {

    @FXML private TextField username, firstName, lastName, phone;
    @FXML private ChoiceBox domain;
    @FXML private Label errorTxt;

    @FXML private Button create, cancel;

    private String nameReg = "[A-Za-z,æ,ø,å,Æ,Ø,Å,-, ]+";
    private String userNameReg = "^[a-zA-Z0-9_.]*$";
    //private String passwordReg = "[\\S ]{6,50}$";
    private String phoneReg = "[0-9]{8}";

    UserModel model = new UserModel();

    @FXML
    void initialize() {
        domain.setItems(FXCollections.observableArrayList("stud.ntnu.no"));
        errorTxt.setText("");
        createValidationListener(username, userNameReg, 30);
        createValidationListener(firstName, nameReg, 30);
        createValidationListener(lastName, nameReg, 30);
        createValidationListener(phone, phoneReg, 8);

    }


    public boolean valid(String s, String match, int max) {
        if (s.matches(match) && s.length() <= max) {
            return true;
        }
        return false;
    }


    public void updateModel() {
        if(txtFieldCheck(firstName)) {
            model.setFirstName(firstName.getText());
        }
        if(txtFieldCheck(lastName)) {
            model.setLastName(lastName.getText());
        }
        if(txtFieldCheck(username)) {
            model.setUsername(username.getText());
        }
        if(txtFieldCheck(phone)) {
            model.setPhone(phone.getText());
        }
        if(domain.getValue() != null) {
            model.setDomain(domain.getValue().toString());
        }
    }

    public boolean txtFieldCheck(TextField field) {
        if (field.getText() != null && !(field.getText().equals(""))) {
            return true;
        }
        return false;
    }


    @FXML
    public void createUser(ActionEvent event) {
        if (validAllFields()) {
            // insert into DB from model
            Boolean response = Main.createUser(model.getUsername(), model.getDomain(), model.getFirstName(), model.getLastName(), model.getPhone());

            if(response) {
                System.out.println("User " + model.getUsername() + " created");
                errorTxt.setTextFill(Color.GREEN);
                errorTxt.setText("Bruker " + model.getUsername() + " opprettet.");
                client.login.Main.show(Main.stage, "Bruker ble opprettet. Passord er sendt på epost.");



            } else {
                System.out.println("User NOT created");
                errorTxt.setTextFill(Color.RED);
                errorTxt.setText("En feil oppstod. Brukeren finnes allerede i databasen.");
            }
        } else {
            errorTxt.setTextFill(Color.RED);
            errorTxt.setText("Du må fylle ut alle feltene");
        }
    }

    @FXML
    public void cancelNewUser(ActionEvent event) {
        //go back to login
        client.login.Main.show(Main.stage);
    }

    public boolean validAllFields() {
        if ( valid(firstName.getText(),nameReg,30) && valid(lastName.getText(),nameReg,30) && valid(username.getText(),nameReg,30)
                && valid(phone.getText(),phoneReg,8)) {
            System.out.println("All fields are valid");
            return true;
        }
        return false;
    }


    public void createValidationListener(final TextField field, final String match, final int max){
        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String oldValue, String newValue) {
                if (!valid(newValue, match, max)) {
                    field.setStyle("-fx-text-inner-color: red; -fx-text-box-border: red; -fx-focus-color: red;");
                    System.out.println("Invalid:" + newValue);
                }
                else{
                    System.out.println("VALID: " + newValue);
                    updateModel();
                    field.setStyle("-fx-text-inner-color: black; -fx-text-box-border: lightgreen; -fx-focus-color: lightgreen;");
                }
            }
        });
    }

}
