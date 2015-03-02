package client.newUser;

import calendar.UserModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import network.ThreadClient;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Hashtable;


public class Controller {

    @FXML private TextField username, firstName, lastName, phone;
    @FXML private ChoiceBox domain;
    @FXML private Label errorTxt;
    @FXML private PasswordField password1, password11;

    @FXML private Button create, cancel;

    private String nameReg = "[A-Za-z,æ,ø,å,Æ,Ø,Å,-, ]+";
    private String userNameReg = "^[a-zA-Z0-9_.]*$";
    private String passwordReg = "[\\S ]{6,50}$";
    private String phoneReg = "[0-9]{8}";

    UserModel model = new UserModel();

    @FXML
    void initialize() {
        domain.setItems(FXCollections.observableArrayList("@stud.ntnu.no"));
        errorTxt.setText("");
        createValidationListener(username, userNameReg, 30);
        createValidationListener(firstName, nameReg, 30);
        createValidationListener(lastName, nameReg, 30);
        createValidationListener(phone, phoneReg, 8);
        createValidationListener(password1, passwordReg, 50);
        createPassListener(password11);

    }


    public boolean valid(String s, String match, int max) {
        if (s.matches(match) && s.length() <= max) {
            return true;
        }
        return false;
    }

    public boolean validPassword(String password) {
        if (valid(password, passwordReg, 30)) {
            if (password1.getText().equals(password11.getText())) {
                return true;
            }
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
        if(txtFieldCheck(password1) && password1.getText().equals(password11.getText())) {
            model.setPassword(password1.getText());
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
            Boolean response = Main.createUser(model.getUsername(), model.getPassword(), model.getDomain(), model.getFirstName(), model.getLastName(), model.getPhone());

            if(response) {
                System.out.println("User " + model.getUsername() + " created");
                errorTxt.setTextFill(Color.GREEN);
                errorTxt.setText("Bruker " + model.getUsername() + " opprettet.");
                client.login.Main.show(Main.stage, "Bruker ble opprettet");



            } else {
                System.out.println("User NOT created");
                errorTxt.setTextFill(Color.RED);
                errorTxt.setText("En feil oppstod");
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

    // validating model
    public boolean validAllModel() {
        if( valid(model.getFirstName(),nameReg,30) && valid(model.getLastName(),nameReg,30) && valid(model.getUsername(),userNameReg,30)
                && valid(model.getPassword(),passwordReg,30) && valid(model.getPhone(),phoneReg,8)){
            System.out.println("All fields in model are valid");
            return true;
        }
        return false;
    }

    public boolean validAllFields() {
        if ( valid(firstName.getText(),nameReg,30) && valid(lastName.getText(),nameReg,30) && valid(username.getText(),nameReg,30)
                && valid(password1.getText(),passwordReg,50) && valid(password11.getText(),passwordReg,50) && valid(phone.getText(),phoneReg,8)) {
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

    public void createPassListener(final PasswordField field) {
        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!validPassword(field.getText())) {
                    field.setStyle("-fx-text-inner-color: red; -fx-text-box-border: red; -fx-focus-color: red;");
                    System.out.println("invalid password");
                } else {
                    System.out.println("valid passwords");
                    updateModel();
                    field.setStyle("-fx-text-inner-color: black; -fx-text-box-border: lightgreen; -fx-focus-color: lightgreen;");
                }
            }
        });
    }

}
