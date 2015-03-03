package server;

import calendar.UserModel;
import com.sun.deploy.net.URLEncoder;
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
           // user.setEmail(user.username+"@"+user.domain);
            System.out.println(user.getEmail());
            if(server.database.Logic.inDatabase("email", user.getEmail(),  "User")){
                //reset pass logic here
                String pass = generateString(7);

                //set new pass in db
                String hash = Crypto.hash(pass);
                //TODO store in server

                //send pass to email
                String subject = URLEncoder.encode("TimeTo password reset", "ASCII");
                String msg = URLEncoder.encode("Your new password is "+pass, "ASCII");
                String url="http://nixomod.webege.com/webtek/send.php?to="+user.getEmail()+"&subject="+subject+"&message="+msg+"&from=felles";

                openURL(url);
                System.out.println("Reset pass for " + user.getEmail()+" successful with pass "+pass);
                return new Query("reset", true);
            }


        }
        catch(Exception e){
            System.out.println("Invalid data given: "+e);

        }
        return new Query("reset", false);
    }

    public static String generateString( int length)
    {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!-_+=)";
        Random rng = new Random();
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

    public static void openURL(String url) throws Exception {
        URL uri = new URL(url);
        URLConnection yc = uri.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        yc.getInputStream()));
        String inputLine;

        System.out.print("Email server replied: ");
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);

        in.close();
    }


}
