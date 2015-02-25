package server;

import java.sql.SQLException;

/**
 * Created by nixo on 2/24/15.
 */
public class Main {

    public static void main(String[] args) {
        ConnectDB connectDB = new ConnectDB();
        connectDB.connect();
        connectDB.getName("nicolaat");
        connectDB.inDatabase("nicolaat", "User", "username");

    }

}
