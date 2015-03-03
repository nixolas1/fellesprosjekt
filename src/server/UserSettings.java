package server;

import calendar.UserModel;
import network.Query;

import java.util.Hashtable;

/**
 * Created by nixo on 2/26/15.
 */
public class UserSettings {

    public static Query setSettings(Hashtable<String, String> data){
        try {
            UserModel user = new UserModel();
            //client.user.setUsername(data.get("username"));
            return new Query("settings", true);

        }
        catch(Exception e){
            System.out.println("Invalid data given.");
        }
        return new Query("settings", false);
    }

    public static Query getSettings(Hashtable<String, String> data){
        try {
            UserModel user = new UserModel();
            user.setUsername(data.get("username"));
            return new Query("settings", true);

        }
        catch(Exception e){
            System.out.println("Invalid data given.");
        }
        return new Query("settings", false);
    }


}
