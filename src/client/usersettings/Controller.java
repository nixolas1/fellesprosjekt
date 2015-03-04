package client.userSettings;

import calendar.UserModel;
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
import network.Query;
import java.util.Hashtable;
import javafx.scene.paint.Color;
import security.Crypto;


public class Controller{

    @FXML private TextField username, domain, firstName, lastName, phone;
    @FXML private PasswordField password1, password11;
    @FXML private Button cancel, save;

    private String nameReg = "[A-Za-z,æ,ø,å,Æ,Ø,Å,-, ]+";
    private String userNameReg = "^[a-zA-Z0-9_.]*$";
    private String passwordReg = "[\\S ]{6,50}$";
    private String phoneReg = "[0-9]{8}";

    private UserModel model;



    @FXML
    void initialize() {
        //model = client.userSettings.Main.user;
        // TODO username, password, domain, firstName, lastName, phone
        model = new UserModel("sondreheiho", Crypto.hash("morra"), "stud.ntnu.no", "Sondre", "Den Beste", "12345678");
        /*
        username = new TextField();
        domain = new TextField();
        firstName = new TextField();
        lastName = new TextField();
        phone = new TextField();
        */
        createValidationListener(password1, passwordReg, 50);
        createPasswordValidationListener(password11, passwordReg, 50);
        createValidationListener(firstName, nameReg, 30);
        createValidationListener(lastName, nameReg, 30);
        createValidationListener(phone, phoneReg, 8);
        initializeFields();
    }

    public void initializeFields(){
        username.setText(model.getUsername());
        username.setDisable(true);
        domain.setText(model.getDomain());
        domain.setDisable(true);
        firstName.setText(model.getFirstName());
        lastName.setText(model.getLastName());
        phone.setText(model.getPhone());

    }

    public boolean valid(String s, String match, int max) {
        if (s.matches(match) && s.length() <= max) {
            return true;
        }
        return false;
    }


    public void cancelChanges(){
        System.out.println("cancelChanges()");
    }

    public void saveChanges(){
        System.out.println("saveChanges()");
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

    public void createPasswordValidationListener(final TextField field, final String match, final int max) {
        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String oldValue, String newValue) {
                if (valid(newValue, match, max) && field.getText().equals(password1.getText())) {
                    System.out.println("VALID: " + newValue);
                    updateModel();
                    field.setStyle("-fx-text-inner-color: black; -fx-text-box-border: lightgreen; -fx-focus-color: lightgreen;");
                } else {
                    field.setStyle("-fx-text-inner-color: red; -fx-text-box-border: red; -fx-focus-color: red;");
                    System.out.println("Invalid:" + newValue);
                }
            }
        });
    }

    public void updateModel() {
        if(txtFieldCheck(firstName)) {
            model.setFirstName(firstName.getText());
        }
        if(txtFieldCheck(lastName)) {
            model.setLastName(lastName.getText());
        }
        if(txtFieldCheck(phone)) {
            model.setPhone(phone.getText());
        }
    }

    public boolean txtFieldCheck(TextField field) {
        if (field.getText() != null && !(field.getText().equals(""))) {
            return true;
        }
        return false;
    }






}
