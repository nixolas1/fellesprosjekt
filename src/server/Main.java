package server;

import calendar.UserModel;
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
        //server.database.Logic.getRow("User", "username", "sondrejw");

        UserModel user = new UserModel("testesen@stud.ntnu.no", "asdasdasdasd", "testesen", "stud.ntnu.no", "Test", "Testesen", "12345678");

        //Logic.createUser(user);
        UserModel sondre = Logic.getUser("sondrejw@stud.ntnu.no");
        //System.out.println(sondre.getEmail());


        ThreadServer socket = new ThreadServer(7777);
        socket.startServer();


    }

}
