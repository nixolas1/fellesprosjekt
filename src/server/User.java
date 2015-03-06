package server;

import calendar.UserModel;
import network.Query;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by andersindrebo on 04/03/15.
 */
public class User {

    public static Query getAllUsers() {
        ArrayList<UserModel> allUsers = server.database.Logic.getAllUsers();
        return new Query("getAllUsers", allUsers);
    }

    public static Query getUser(Hashtable<String, String> data) {

        UserModel user = new UserModel();
        user.setUsername(data.get("username"));
        user.setDomain(data.get("domain"));
        System.out.println("Get user "+user.getEmail());
        if(server.database.Logic.inDatabase("User", "email", user.getEmail())) {
            UserModel serverUser = server.database.Logic.getUser(user.getEmail());
            return new Query("getUser", serverUser);
        }
        return new Query("error", "getUser");
    }
}
