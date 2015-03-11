package server;

import calendar.Calendar;
import calendar.Group;
import calendar.UserModel;
import network.ThreadClient;
import network.ThreadServer;
import security.Crypto;
import server.database.*;
import server.database.Logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import static server.AppointmentLogic.getCalendarAppointments;

/**
 * Created by Nixo on 2/24/15.
 */
public class Main {

    public static void main(String[] args){
        ConnectDB connectDB = new ConnectDB();
        server.database.Logic.setConn(connectDB.connect());

        //server.database.Logic.getRow("User", "username", "sondrejw");        //UserModel(username, password, domain, firstName, lastName, phone){
        //UserModel user = new UserModel("sondreerbest", Crypto.hash("morra"), "stud.ntnu.no", "nixso", "stysdgg", "12345678");
        //System.out.println(Logic.createUser(user));
        //UserModel sondre = Logic.getUser("sondrejw@stud.ntnu.no");
        //System.out.println(sondre.getEmail());

        //UserModel sondre = server.database.Logic.getUser("sondrejw@stud.ntnu.no");
        //sondre.setFirstName("Sondre");
        //sondre.setPassword(Crypto.hash("passord"));
        //server.database.Logic.updateUser(sondre);
        //Logic.getUser("sondrejw@stud.ntnu.no");
        //System.out.println(Logic.createUser(user));
        //server.database.Logic.getNumberOfRows("User");
        //server.database.Logic.getAllRows("User");
        //ArrayList<UserModel> hore = server.database.Logic.getAllUsers();
        //getCalendarAppointments(new Hashtable<String, String>() {{put("id", "2");}});

        /*
        UserModel user1 = server.database.Logic.getUser("nicolaat@stud.ntnu.no");
        UserModel user2 = server.database.Logic.getUser("admin@stud.ntnu.no");
        UserModel user3 = server.database.Logic.getUser("fucku@stud.ntnu.no");
        UserModel user4 = server.database.Logic.getUser("uyendn@stud.ntnu.no");
        ArrayList<UserModel> list = new ArrayList<>(Arrays.asList(user1, user2, user3, user4));
        Calendar group = new Calendar("Bæsjegruppen", list);

        Hashtable<String, Calendar> foo = new Hashtable<String, Calendar>(){{put("data",group);}};
        System.out.println("hashtable: " + foo.toString());
        Logic.createGroup(foo);
        */



        //int stuff = Logic.getLastGroupIdUsed();
        //System.out.println("LAST ID: " + stuff);
        ThreadServer socket = new ThreadServer(7777);
        socket.startServer();



    }

}
