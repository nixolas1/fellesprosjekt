package server;

import network.Query;
import security.Login;

import java.util.Hashtable;

import static security.Login.validLogin;


public class Logic {

    public static Query process(String request, Hashtable data){
        switch (request){
            case "end": return null;
            case "login": return Login.validLogin(data);
            case "create": return CreateUser.createUser(data);
            case "reset": return ForgottenPass.resetPassword(data);
            case "getSettings": return UserSettings.getSettings(data);
            case "setSettings": return UserSettings.setSettings(data);
            case "getAllUsers": return User.getAllUsers();


        }
        return new Query("error");
    }


}
