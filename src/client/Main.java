package client;

import network.Query;
import network.ThreadClient;

import java.util.Hashtable;

/**
 * Created by nixo on 2/26/15.
 */
public class Main {


    public static void main(String[] args) {
        //create socket to server
        ThreadClient socket = new ThreadClient();

        Hashtable<String, String> data = new Hashtable<String, String>(){{
           // put("username","nicolaat");
            put("pass", "hello");
            put("domain", "stud.ntnu.no");
        }};

        Query reply = socket.send(new Query("login", data));
        Hashtable<String, Boolean> response = reply.data;

        if(response.get("reply")){
            System.out.println("success");
        }
        else{
            try {
                if (response.get("data")) {
                    //wrong username or domain
                    System.out.println("wrong user");
                } else {
                    System.out.println("wrong pass");
                }
            }
            catch (NullPointerException e){
                System.out.println("Missing data");
            }
        }
    }
}
