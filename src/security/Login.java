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


            if(username!=null && pass_hash!=null && domain!=null) {
                return new Query("login", true);    //accept all

               /* if (inDatabase(username+"@"+domain, "User", "username")){
                    User user = new User(username);
                    if(user.hash == pass_hash){
                        return new Query("login", true);    //correct login
                    }else{
                        return new Query("login", false, false); //false, false = wrong password
                    }
                }else{
                    return new Query("login", false, true); //false, true = wrong user/domain
                }
            */}

        }
        catch(Exception e){
            System.out.println("Invalid login-data given.");
        }

        return new Query("login", false);
    }
}
