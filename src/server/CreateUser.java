package server;

import calendar.Notification;
import calendar.UserModel;
import network.Email;
import network.Query;
import security.Crypto;
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
            String pass = Crypto.generatePass(7);
            System.out.println(data.toString());
            UserModel user = new UserModel(data.get("username"),
                                            Crypto.hash(pass),
                                            data.get("domain"),
                                            data.get("firstName"),
                                            data.get("lastName"),
                                            data.get("phone"));

            System.out.println(user.toString());
            Boolean createdUser = server.database.Logic.createUser(user);
            if(createdUser){
                System.out.println("Created user "+user.getEmail()+" with pass "+pass);
                String subject = "New TimeTo user registration for "+user.getUsername();
                String message = "Your password is "+ pass+" \nPlease change it in your settings after logging in.";
                Email.sendEmail(user.getEmail(), subject, message);
                server.database.Logic.storeNotification(
                        new Notification("Velkommen, "+user.getFirstName()+"!", user, false));
                return new Query("create", true);
            }


        }
        catch(Exception e){
            System.out.print("Invalid create-user data given: ");
            e.printStackTrace();
        }
        return new Query("create", false);
    }
}
