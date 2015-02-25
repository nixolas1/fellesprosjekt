package server;

import network.Query;

import java.util.Hashtable;

import static security.Login.validLogin;


public class Logic {

    public static Query process(String request, Hashtable data){
        switch (request){
            case "end": return null;
            case "login": return validLogin(data);
            case "create": return new Query("create", true); //createUser(data);
        }
        return new Query("Error: No function could process that query.");
    }




}
