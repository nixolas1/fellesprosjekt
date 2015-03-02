package security;

import calendar.UserModel;
import network.Query;
import server.database.ConnectDB;

import java.util.Hashtable;

/**
 * Created by nixo on 2/23/15.
 */
public class Login {

    public static Query validLogin(Hashtable<String, String> data){
        try {
            String username = data.get("username");
            String pass_hash = data.get("pass");
            String domain = data.get("domain");
            String email = username+domain;

            if(username!=null && pass_hash!=null && domain!=null) {

                //if (ConnectDB.inDatabase(username+"@"+domain, "User", "username")){
                //get user from db as UserModel
                //server.database.Logic.getUser(email);
                System.out.println("Getting user: "+email+ " with pass: "+pass_hash);
                UserModel user = server.database.Logic.getUser(email);//new UserModel(email, "n4bQgYhMfWWaL+qgxVrQFaO/TxsrC4Is0V1sFbDwCgg=");
                System.out.println(user.getEmail()+", "+user.getPassword());
                if(email.equals(user.getEmail())){
                    if(pass_hash.equals(user.getPassword())){
                        //handle new login here
                        //meh
                        return new Query("login", true);    //correct login
                    }else{
                        return new Query("login", false, false); //false, false = wrong password
                    }
                }else{
                    return new Query("login", false, true); //false, true = wrong user/domain
                }
            }

        }
        catch(Exception e){
            System.out.println("Invalid login-data given.");
            System.err.println(e);
        }

        return new Query("login", false, true);
    }
}
