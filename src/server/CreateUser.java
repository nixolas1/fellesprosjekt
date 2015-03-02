package server;

import calendar.UserModel;
import network.Query;
import server.database.*;
import server.database.Logic;

import java.util.Hashtable;

/**
 * Created by nixo on 2/26/15.
 */
public class CreateUser {

    public CreateUser(){
        System.out.println("CreateUser()");
    }

    public static Query createUser(Hashtable<String, String> data){
        try {
            UserModel user = new UserModel(data.get("username"),
                                            data.get("pass"),
                                            data.get("domain"),
                                            data.get("firstName"),
                                            data.get("lastName"),
                                            data.get("phone"));


            Boolean createdUser = server.database.Logic.createUser(user);
            if(createdUser){
                return new Query("create", true);
            }


        }
        catch(Exception e){
            System.out.println("Invalid login-data given.");
        }
        return new Query("create", false);
    }
}
