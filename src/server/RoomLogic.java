package server;

import calendar.*;
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

    public static Query initiateRoomLogic(Hashtable<String, Appointment> data){
        ArrayList<String> attendeeList = new ArrayList<>();
        int numberOfConflicts = 0;                           // Teller for hvor mange overlapp man vil få når flere folk er med i flere grupper på samme arrangement
        int numberOfDistinctAttendees;                      // Det endelig antallet personer som deltar, uten overlapp
        Appointment appointment = data.get("data");        // Henter data'en fra Hashtable

        // Løper gjennom alle inviterte til en arrangement
        for (Attendee attendee : appointment.getAttendees()){
            String email = attendee.getUser().getEmail();
            if (! attendeeList.contains(email)){
                attendeeList.add(email);
            } else {
                numberOfConflicts += 1;
            }
        }

        // Løper gjennom alle medlemmene i alle gruppekalenderene i appointment objektet
        for (Calendar groupCalendar : appointment.getCals()){
            ArrayList<UserModel> memberList= groupCalendar.getMembers();
            for (UserModel member : memberList){
                if (! attendeeList.contains(member.getEmail())){
                    attendeeList.add(member.getEmail());
                } else {
                    numberOfConflicts += 1;
                }
            }

        }

        numberOfDistinctAttendees = attendeeList.size();

        ArrayList<List<String>> foo = server.database.Logic.getAllRows("Room");

        ArrayList<Room> allCapableRooms = new ArrayList<>();            // List over alle rom med nok kapasitet til dette møtet

        // Løper gjennom alle rommene
        for (List<String> rooms : foo){
            Room room = new Room();
            int count = 0;
            for (String roomAttribute : rooms){
                for (int i = 0; i <rooms.size(); i++){
                    // Legger kun til rommene med bra nok kapasitet
                    if (numberOfDistinctAttendees <= Integer.parseInt(rooms.get(2))) {
                        switch (i) {
                            case 0:
                                room.setId(Integer.valueOf(rooms.get(0)));
                            case 1:
                                room.setName(rooms.get(1));
                            case 2:
                                room.setCapacity(Integer.valueOf(rooms.get(2)));
                            case 3:
                                room.setOpensAt(Integer.valueOf(rooms.get(3)));
                            case 4:
                                room.setClosesAt(Integer.valueOf(rooms.get(4)));
                        }
                    }

                }
                allCapableRooms.add(room);
            }
        }

        // Henter alle avtaler




        return new Query("roomLogic", roomList)
    }

}
