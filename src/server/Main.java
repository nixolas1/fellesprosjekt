package server;

import server.database.ConnectDB;

/**
 * Created by nixo on 2/24/15.
 */
public class Main {

    public static void main(String[] args){
        ConnectDB connectDB = new ConnectDB();
        connectDB.connect();
        //connectDB.getUser("nicolaat");
        //connectDB.getRow("User", "username", "nicolaat");
        connectDB.getRow("Room", "roomid", "2");
        //String[] asda = {"asdasd", "asdasd"};
        //connectDB.inDatabase("username", "User", "nikolaat");

    }

}
