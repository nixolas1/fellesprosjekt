package client.newUser;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class Controller {

    @FXML private TextField username, firstName, lastName, phone;
    @FXML private ChoiceBox domain;

    @FXML private PasswordField password, password2;

    @FXML private Button create;

    private String nameReg = "";
    private String userNameReg = "^[a-zA-Z0-9_.]*$";
    private String passwordReg = "";
    private String phoneReg = "";

    UserModel model = new UserModel();


    @FXML
    void initialize() {
        createValidationListener(username, userNameReg, 30);
        createValidationListener(firstName, nameReg, 30);
        createValidationListener(lastName, nameReg, 30);
    }




    public boolean valid(String s, String match, int max) {
        if (s.matches(match) && s.length() <= max) {
            return true;
        }
        return false;
    }

    public void updateModel() {
        model.setFirstName(firstName.getText());
        model.setLastName(lastName.getText());
        model.setUsername(username.getText());
        model.setPassword(password.getText());
        model.setPhone(phone.getText());
        model.setDomain(domain.getValue().toString());
    }

    @FXML
    public void createUser(ActionEvent event) {
        if (validAll()) {
            // insert into DB from model
        }
    }

    public boolean validAll() {
        if( valid(model.getFirstName(),nameReg,30) && valid(model.getLastName(),nameReg,30) && valid(model.getUsername(),userNameReg,30) && valid(model.getPassword(),passwordReg,30) && valid(model.getPhone(),phoneReg,8)){
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
