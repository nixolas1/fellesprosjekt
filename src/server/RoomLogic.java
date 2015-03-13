package server;

import calendar.Appointment;
import calendar.Room;
import network.Query;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Sondre on 06.03.15.
 */
public class RoomLogic {

    public static Query getRooms(){
        try {
            ArrayList<Room> rooms = getModelsFromDBOutput(
                    server.database.Logic.getAllRows("Room"));
            System.out.println(rooms.toString());
            return new Query("getRooms", rooms);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Query("getRooms", false);
    }

    public static ArrayList<Room> getModelsFromDBOutput(ArrayList<List<String>> list){
        ArrayList<Room> roomList = new ArrayList<>();
        for (List<String>a : list){
            //System.out.println(a.toString());
            roomList.add(new Room(Integer.parseInt(a.get(0)), a.get(1), Integer.parseInt(a.get(2)), Integer.parseInt(a.get(3)), Integer.parseInt(a.get(4))));
        }

        return roomList;
    }

    public static ArrayList<Room> availableRooms(){
        //Hashtable<String, ArrayList<Appointment>> hore= AppointmentLogic.getCalendarAppointments(new Hashtable<String, String>() {{put("id", "1");}}).data;
        Hashtable<String, ArrayList<Room>> foo = getRooms().data;
        ArrayList<Room> appList = foo.get("reply");
        System.out.println(appList.toString());

        /*Query query = getRooms();
        Appointment app = appList.get(0);
        Hashtable<String, ArrayList<Room>> allRooms = query.data;
        ArrayList*/
        return appList;
    }

    public static ArrayList<Room> initiateRoomLogic(Hashtable<String, Appointment> data){
        Appointment appointment = data.get("data");


    }
}
