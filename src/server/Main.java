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
        //UserModel(username, password, domain, firstName, lastName, phone){
        UserModel user = new UserModel("morratilnixo", "passordhashgårinnher", "stud.ntnu.no", "nixo", "stygg", "12345678");

        //server.database.Logic.inDatabase("username", "User", "sondrej");

        System.out.println(Logic.createUser(user));
        //UserModel sondre = Logic.getUser("sondrejw@stud.ntnu.no");
        //System.out.println(sondre.getEmail());

        //ThreadServer socket = new ThreadServer(7777);
        //socket.startServer();



    }

}
