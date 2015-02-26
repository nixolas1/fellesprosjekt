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
            UserModel user = new UserModel();
            user.setUsername(data.get("username"));
            user.setPassword(data.get("pass"));
            user.setDomain(data.get("domain"));
            user.setFirstName(data.get("firstName"));
            user.setLastName(data.get("lastName"));
            user.setPhone(data.get("phone"));
            user.setEmail(user.username+"@"+user.domain);
            return new Query("create", true);

        }
        catch(Exception e){
            System.out.println("Invalid login-data given.");
        }
        return new Query("create", false);
    }
}
