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
        ConnectDB connectDB = new ConnectDB();
        server.database.Logic.setConn(connectDB.connect());

        //server.database.Logic.getRow("User", "username", "sondrejw");        //UserModel(username, password, domain, firstName, lastName, phone){
        //UserModel user = new UserModel("sondreerbest", Crypto.hash("morra"), "stud.ntnu.no", "nixso", "stysdgg", "12345678");
        //System.out.println(Logic.createUser(user));
        //UserModel sondre = Logic.getUser("sondrejw@stud.ntnu.no");
        //System.out.println(sondre.getEmail());
        //
        // SÅNN HER BRUKER OPPDATERER MAN BRUKERINFO
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
        UserModel user1 = server.database.Logic.getUser("sondrejw@stud.ntnu.no");
        UserModel user2 = server.database.Logic.getUser("admin@stud.ntnu.no");
        UserModel user3 = server.database.Logic.getUser("fucku@stud.ntnu.no");
        UserModel user4 = server.database.Logic.getUser("slange@stud.ntnu.no");
        user4.setPhone("12345678");
        ArrayList<UserModel> list = new ArrayList<>(Arrays.asList(user1, user2, user3, user4));
        Group group = new Group(Logic.getLastGroupIdUsed() +1, "TESTGRUPPE", list);
        Logic.createGroup(group);
        */

        /*
        Hashtable<String, ArrayList<Appointment>> hore= AppointmentLogic.getCalendarAppointments(new Hashtable<String, String>() {{put("id", "1");}}).data;
        ArrayList<Appointment> appList = hore.get("reply");
        Appointment app1 = appList.get(0);
        */

        Hashtable<String, ArrayList<Room>> foo = RoomLogic.getRooms().data;
        ArrayList<Room> roomList = foo.get("reply");
        Room fuckRoom = roomList.get(3);

        // todo id, title, purpose, location, startDate, endDate, endRepeatDate, repeatEveryXDays, calID, roomID
        LocalDateTime startdate = LocalDateTime.parse("2015-04-04T10:00");
        LocalDateTime enddate = LocalDateTime.parse("2015-04-04T16:00");
        Appointment app = new Appointment("testmøte", "for å teste vel", null, startdate, enddate, fuckRoom, new Calendar(5), 0, null);
        Logic.createAppointment(app);

        //RoomLogic.availableRooms();

        ThreadServer socket = new ThreadServer(7777);
        socket.startServer();



    }

}
