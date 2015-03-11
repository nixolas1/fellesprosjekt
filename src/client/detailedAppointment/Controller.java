/*package client.detailedAppointment;

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


    @FXML private Text loginErrorText;
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
        model = client.detailedAppointment.Main.user;
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

    }


    public void toCalendar(ActionEvent event) {
        client.calendar.Main.show(Main.stage);
    }
}
*/