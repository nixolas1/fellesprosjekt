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
        //server.database.Logic.getRow("User", "username", "sondrejw");
        System.out.println("THE END");
    }

    public static Query createUser(Hashtable<String, String> data){
        try {
            UserModel user = new UserModel(data.get("email"),
                                            data.get("pass"),
                                            data.get("username"),
                                            data.get("domain"),
                                            data.get("firstName"),
                                            data.get("lastName"),
                                            data.get("phone"));
            /*user.setFirstName(data.get("firstName"));
            user.setLastName(data.get("lastName"));
            user.setPhone(data.get("phone"));*/

            server.database.Logic.createUser(user);



            /*if(Database.createUser(user)){
                return new Query("create", true);
            }


            */



        }
        catch(Exception e){
            System.out.println("Invalid login-data given.");
        }
        return new Query("create", false);
    }
}
