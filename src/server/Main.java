package server;

import calendar.*;

import network.ThreadClient;
import network.ThreadServer;
import security.Crypto;
import server.database.*;
import server.database.Logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import static server.AppointmentLogic.getCalendarAppointments;

/**
 * Created by Nixo on 2/24/15.
 */
public class Main {

    public static void main(String[] args){
        //Start database
        ConnectDB connectDB = new ConnectDB();
        server.database.Logic.setConn(connectDB.connect());

        //test stuff
        /*Hashtable<String, ArrayList<Appointment>> hore= AppointmentLogic.getCalendarAppointments(new Hashtable<String, String>() {{put("id", "1");}}).data;
        ArrayList<Appointment> appList = hore.get("reply");
        Appointment app = appList.get(0);
        System.out.println(app.toString());
        System.out.println("yes?");
        server.database.Logic.createAppointment(app);*/

        //Start sockets
        ThreadServer socket = new ThreadServer(7777);
        socket.startServer();




    }

}
