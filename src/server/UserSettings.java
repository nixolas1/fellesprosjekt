package server;

import calendar.UserModel;
import network.Email;
import network.Query;
import security.Crypto;

import java.util.Hashtable;

/**
 * Created by nixo on 2/26/15.
 */
public class UserSettings {

    public static Query setSettings(Hashtable<String, UserModel> data){
        try {
            UserModel user = new UserModel();
            //user.setUsername(data.get("username"));
            return new Query("settings", true);

        } catch(Exception e){
            System.out.println("Invalid data given.");
        }
        return new Query("settings", false);
    }

    public static Query updateSettings(Hashtable<String, String> data){
        System.out.println("updateSettings()");
        try{
            //String a = "username, password, domain, firstName, lastName, phone";
            UserModel user = new UserModel(data.get("username"),
                                            data.get("password"),
                                            data.get("domain"),
                                            data.get("firstName"),
                                            data.get("lastName"),
                                            data.get("phone"));

            if(server.database.Logic.inDatabase("User", "email", user.getEmail() + "@" + user.getDomain())){
                if (server.database.Logic.updateUser(user)){
                    return new Query("settings", true);
                } else {
                    return new Query("settings", false);
                }
            }
        } catch (Exception e){
            System.out.print("Exception triggered in server.userSettings.updateSettings(): ");
            e.printStackTrace();
        } return new Query("settings", false);
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
                return new Query("create", true);
            }


        }
        catch(Exception e){
            System.out.print("Invalid create-user data given: ");
            System.err.println(e);
        }
        return new Query("create", false);
    }

    public static Query resetPassword(Hashtable<String, String> data){
        try {
            UserModel user = new UserModel();
            user.setUsername(data.get("username"));
            user.setDomain(data.get("domain"));
            System.out.println(user.getEmail());
            if(server.database.Logic.inDatabase("User", "email", user.getEmail())){

                String pass = Crypto.generatePass(7);

                //set new pass in db
                String hash = Crypto.hash(pass);

                UserModel serverUser = server.database.Logic.getUser(user.getEmail());
                serverUser.setPassword(hash);
                server.database.Logic.updateUser(serverUser);

                //send pass to email
                String subject = "TimeTo password reset";
                String msg = "Your new password is "+pass;
                Email.sendEmail(user.getEmail(), subject, msg);
                System.out.println("Reset pass for " + user.getEmail()+" successful with pass "+pass);
                return new Query("reset", true);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Query("reset", false);
    }
}
