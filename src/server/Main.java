package server;

import network.ThreadServer;
import server.database.ConnectDB;

/**
 * Created by nixo on 2/24/15.
 */
public class Main {

    public static void main(String[] args){
        ConnectDB connectDB = new ConnectDB();
        connectDB.connect();

        ThreadServer socket = new ThreadServer(7777);
        socket.startServer();


    }

}
