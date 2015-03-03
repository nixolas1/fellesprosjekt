package server;

import calendar.UserModel;
import network.Query;

import java.util.Hashtable;

/**
 * Created by nixo on 2/26/15.
 */
public class ForgottenPass {
    public static Query resetPassword(Hashtable<String, String> data){
        try {
            UserModel user = new UserModel();
            user.setUsername(data.get("username"));
            user.setDomain(data.get("domain"));
           // client.user.setEmail(client.user.username+"@"+client.user.domain);

            return new Query("reset", true);

        }
        catch(Exception e){
            System.out.println("Invalid data given.");
        }
        return new Query("reset", false);
    }
}
