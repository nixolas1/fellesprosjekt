package server;

import network.ThreadServer;
import server.database.*;
import server.database.Logic;

/**
 * Created by nixo on 2/24/15.
 */
public class Main {

    public static void main(String[] args){
        ConnectDB connectDB = new ConnectDB();
        server.database.Logic.setConn(connectDB.connect());
        server.database.Logic.getRow("User", "username", "sondrejw");

        ThreadServer socket = new ThreadServer(7777);
        socket.startServer();


    }

}
