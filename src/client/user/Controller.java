package client.user;

import calendar.UserModel;
import client.login.*;
import client.login.Main;
import client.newUser.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import network.Query;
import network.ThreadClient;
import security.Crypto;

import java.util.Hashtable;

/**
 * Created by Rae on 02.03.15.
 */


public class Controller {

    @FXML private TextField username, firstName, lastName, phone, domain;
    //@FXML private ChoiceBox domain;

    @FXML private PasswordField password1, password11;

    @FXML private Button create, cancel;

    private String nameReg = "[A-Za-z,æ,ø,å,Æ,Ø,Å,-, ]+";
    private String userNameReg = "^[a-zA-Z0-9_.]*$";
    private String passwordReg = "[\\S ]{6,50}$";
    private String phoneReg = "[0-9]{8}";
    private String email;

    UserModel model;

    public Controller(String email){
        this.email = email;
    }


    @FXML
    void initialize(){
        initializeFields();
    }

    public void initializeFields() {
        /*UserModel user = server.database.Logic.getUser("sondrejw@stud.ntnu.no");
        username.setText(user.getUsername());
        firstName.setText(user.getFirstName());
        lastName.setText(user.getDomain());
        password1.setText("");
        password11.setText("");*/
        //client.user.setEmail();
        //client.user.setFirstName();
        model = server.database.Logic.getUser(email);

        username.setText(model.getUsername());
        domain.setText(model.getDomain());
        firstName.setText(model.getFirstName());
        lastName.setText((model.getLastName()));
        phone.setText((model.getPhone()));
        password1.setText(Crypto.hash(model.getPassword()));
        password11.setText(Crypto.hash(model.getPassword()));
    }

    public boolean validAllFields() {
        if ( valid(firstName.getText(),nameReg,30) && valid(lastName.getText(),nameReg,30) && valid(username.getText(),nameReg,30)
                && valid(password1.getText(),passwordReg,50) && valid(password11.getText(),passwordReg,50) && valid(phone.getText(), phoneReg, 8) && validPassword()) {
            System.out.println("All fields are valid");
            return true;
        }
        return false;
    }

    public boolean valid(String s, String match, int max) {
        if (s.matches(match) && s.length() <= max) {
            return true;
        }
        return false;
    }

    public void setAllFields(){
        model.setPassword(Crypto.hash(password1.getText()));
        model.setFirstName(firstName.getText());
        model.setLastName(lastName.getText());
        model.setPhone(phone.getText());

    }

    public boolean validPassword() {
        if (valid(password1.getText(), passwordReg, 50)) {
            if (password1.getText().equals(password11.getText())) {
                return true;
            }
            return false;
        }
        return false;
    }

    @FXML
    public void saveChanges() {
        if (validAllFields()) {
            setAllFields();
            Hashtable<String, String> data = new Hashtable<String, String>(){{
                put("user", model.getUsername());
                put("domain", model.getDomain());
                put("pass",model.getPassword());
                put("firstName",model.getFirstName());
                put("lastName", model.getLastName());
                put("phone", model.getPhone());
            }};
            ThreadClient socket = new ThreadClient();
            //model.setUsername(username.getText());
            Query reply = socket.send(new Query("update",data));
            response = (Hashtable<String, Boolean>)reply.data.get("reply");
            System.out.println("data insterted from model to database");
        }
    }

    @FXML
    public void cancelChanges() {
        //go back to calendar
    }

}
