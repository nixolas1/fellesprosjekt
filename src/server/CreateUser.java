package server;

import calendar.UserModel;
import network.Query;

import java.util.Hashtable;

/**
 * Created by nixo on 2/26/15.
 */
public class CreateUser {


    public static Query createUser(Hashtable<String, String> data){
        try {
            UserModel user = new UserModel(data.get("email"), data.get("pass"));
            user.setFirstName(data.get("firstName"));
            user.setLastName(data.get("lastName"));
            user.setPhone(data.get("phone"));



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
