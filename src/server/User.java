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
        return new Query("",data);
    }
}
