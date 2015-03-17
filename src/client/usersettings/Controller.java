package client.usersettings;

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
import security.Crypto;


public class Controller{


    @FXML private Text confirmText;
    @FXML private PasswordField password;
    @FXML private ChoiceBox domain;

    @FXML private Button login, newUser, forgottenPassword;

    private String userNameReg = "^[a-zA-Z0-9_.]*$";
    @FXML private TextField username, firstName, lastName, phone;
    @FXML private PasswordField password1, password11;


    private String nameReg = "[A-Za-z,æ,ø,å,Æ,Ø,Å,-, ]+";
    private String passwordReg = "[\\S ]{6,50}$";
    private String phoneReg = "[0-9]{8}";

    private UserModel model;



    @FXML
    void initialize() {
        model = Main.user;
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
        domain.setItems(FXCollections.observableArrayList("stud.ntnu.no"));
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
        System.out.println("Back to Calendar");
    }


    public boolean hasChangedPassword(){
        if (password1.getText().length() > 0 && password11.getText().length() > 0) {
            System.out.println("hasChangedPassword(): true");
            System.out.println("password1.getText().length():" + password1.getText().length());
            System.out.println("password11.getText().length():" + password11.getText().length());
            return true;
        } else {
            System.out.println("hasChangedPassword(): false");
            return false;
        }
    }

    public boolean validatePasswordFields() {
        if (valid(password1.getText(), passwordReg,50) && valid(password11.getText(),passwordReg,50)) {
            System.out.println("All fields are valid");
            return true;
        } return false;
    }


    @FXML
    public void saveChanges() {
        System.out.println("updateUser()");
        // TODO  -->   username, password, domain, firstName, lastName, phone
        if (hasChangedPassword()){
            if (validAllFields() && validatePasswordFields()){
                Boolean response = client.usersettings.Main.updateUser(
                        model.getUsername(),
                        Crypto.hash(password1.getText()),
                        model.getDomain(),
                        firstName.getText(),
                        lastName.getText(),
                        phone.getText());
                if (response) {
                    System.out.println("User settings updated");
                } else {
                    System.out.println("Something wrong happened in updating user settings");
                    System.out.println();
                }
            } else {
                System.out.println("One og more fields are not correct");
            }
        } else {
            if (validAllFields()){
                Boolean response = client.usersettings.Main.updateUser(
                        model.getUsername(),
                        model.getPassword(),
                        model.getDomain(),
                        firstName.getText(),
                        lastName.getText(),
                        phone.getText());
                if (response) {
                    System.out.println("User settings updated");
                } else {
                    System.out.println("Something wrong happened in updating user settings");
                }
            } else {
                System.out.println("One og more fields are not correct");
            }
        }
        confirmText.setVisible(true);
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

    public void logOff(ActionEvent event) {
        client.login.Main.show(Main.stage);
    }

    public void toCalendar(ActionEvent event) {
        client.calendar.Main.show(Main.stage, Main.user);
    }
}
