package security;

import network.Query;

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

            //if username in inTable("nixolas1", "User", "username");

            if(username!="" && pass_hash!="" && domain!="")
                return new Query("login", true);

        }
        catch(Exception e){
            System.out.println("Invalid login-data given.");
        }
        return new Query("login", false);
    }
}
