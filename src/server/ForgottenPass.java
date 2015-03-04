package server;

import calendar.UserModel;
import com.sun.deploy.net.URLEncoder;
import network.Email;
import network.Query;
import security.Crypto;

import java.util.Hashtable;
import java.util.Random;
import java.net.*;
import java.io.*;



/**
 * Created by nixo on 2/26/15.
 */
public class ForgottenPass {
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
