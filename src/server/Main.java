package server;

import calendar.UserModel;
import network.ThreadServer;
import security.Crypto;
import server.database.*;
import server.database.Logic;

/**
 * Created by Sondre on 2/24/15.
 */
public class Main {

    public static void main(String[] args){
        ConnectDB connectDB = new ConnectDB();
        server.database.Logic.setConn(connectDB.connect());

        /*server.database.Logic.getRow("User", "username", "sondrejw");        //UserModel(username, password, domain, firstName, lastName, phone){
        UserModel user = new UserModel("morratilnixo", "passordhashgårinnher", "stud.ntnu.no", "nixo", "stygg", "12345678");
        System.out.println(Logic.createUser(user));
        UserModel sondre = Logic.getUser("sondrejw@stud.ntnu.no");
        System.out.println(sondre.getEmail());

        // SÅNN HER BRUKER OPPDATERER MAN BRUKERINFO
        UserModel sondre = server.database.Logic.getUser("sondrejw@stud.ntnu.no");
        sondre.setFirstName("Sondre");
        sondre.setPassword(Crypto.hash("passord"));
        server.database.Logic.updateUser(sondre);
        Logic.getUser("sondrejw@stud.ntnu.no");
        */

        ThreadServer socket = new ThreadServer(7777);
        socket.startServer();


    }

}
