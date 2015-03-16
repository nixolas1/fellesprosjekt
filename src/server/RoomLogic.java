package server;

import calendar.*;
import network.Query;

import java.time.LocalDateTime;
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

    public static void initiateRoomLogicObject(Appointment appointment) {
        System.out.println("initiateRoomLogicObject()");
        Hashtable<String, Appointment> hashTable = new Hashtable<String, Appointment>() {{
            put("data", appointment);
        }};
        initiateRoomLogic(hashTable);
    }

    public static Query initiateRoomLogic(Hashtable<String, Appointment> data){
        System.out.println("initiateRoomLogic()");
        ArrayList<String> attendeeList = new ArrayList<>();
        int numberOfConflicts = 0;                           // Teller for hvor mange overlapp man vil få når flere folk er med i flere grupper på samme arrangement
        int numberOfDistinctAttendees;                      // Det endelig antallet personer som deltar, uten overlapp
        Appointment appointment = data.get("data");        // Henter data'en fra Hashtable

        // Løper gjennom alle inviterte til en arrangement
        for (Attendee attendee : appointment.getAttendees()){
            String email = attendee.getUser().getEmail();
            if (! attendeeList.contains(email)){
                attendeeList.add(email);
                //System.out.println("Added user '" + email + "'");
            } else {
                numberOfConflicts += 1;
                System.out.println("numberOfConflicts: " + numberOfConflicts);
            }
        }

        // Løper gjennom alle medlemmene i alle gruppekalenderene i appointment objektet
        for (Calendar groupCalendar : appointment.getCals()){
            ArrayList<UserModel> memberList= groupCalendar.getMembers();
            for (UserModel member : memberList){
                if (! attendeeList.contains(member.getEmail())){
                    attendeeList.add(member.getEmail());
                    //System.out.println("Added user '" + email + "'");
                } else {
                    numberOfConflicts += 1;
                    System.out.println("numberOfConflicts: " + numberOfConflicts);
                }
            }

        }

        numberOfDistinctAttendees = attendeeList.size();
        System.out.println("numberOfDistinctAttendees: " + numberOfDistinctAttendees);

        ArrayList<List<String>> allRowsFromRoom = server.database.Logic.getAllRows("Room");
        ArrayList<Integer> allRoomIds = new ArrayList<>();
        ArrayList<Room> allCapableRooms = new ArrayList<>();            // List over alle rom med nok kapasitet til dette møtet

        // Løper gjennom alle rommene
        for (List<String> rooms : allRowsFromRoom){
            int count = 0;
            if (numberOfDistinctAttendees <= Integer.parseInt(rooms.get(2))) {
                Room room = new Room();
                room.setId(Integer.valueOf(rooms.get(0)));
                room.setName(rooms.get(1));
                room.setCapacity(Integer.valueOf(rooms.get(2)));
                room.setOpensAt(Integer.valueOf(rooms.get(3)));
                room.setClosesAt(Integer.valueOf(rooms.get(4)));
                new Room(Integer.valueOf(rooms.get(0)), rooms.get(1), Integer.valueOf(rooms.get(2)), Integer.valueOf(rooms.get(3)), Integer.valueOf(rooms.get(4)));
                allRoomIds.add(Integer.valueOf(rooms.get(0)));
                allCapableRooms.add(room);
            }
            /*for (String roomAttribute : rooms){
                /*for (int e = 0; e < rooms.size(); e++) {
                    if (numberOfDistinctAttendees <= Integer.parseInt(rooms.get(2)))
                }

                for (int i = 0; i <rooms.size(); i++){
                    // Legger kun til rommene med bra nok kapasitet
                    if (numberOfDistinctAttendees <= Integer.parseInt(rooms.get(2))) {
                        switch (i) {
                            case 0:
                                room.setId(Integer.valueOf(rooms.get(0)));
                                allRoomIds.add(Integer.valueOf(rooms.get(0)));
                                break;
                            case 1:
                                room.setName(rooms.get(1));
                                break;
                            case 2:
                                room.setCapacity(Integer.valueOf(rooms.get(2)));
                                break;
                            case 3:
                                room.setOpensAt(Integer.valueOf(rooms.get(3)));
                                break;
                            case 4:
                                room.setClosesAt(Integer.valueOf(rooms.get(4)));
                                break;
                        }
                    }

                }*/

        }


        // Henter alle avtaler som er koblet på en rom-id
        int[] allCalendarIds;
        ArrayList<List<String>> allRowsFromAppointment = server.database.Logic.getAllRows("Appointment");
        ArrayList<Appointment> collidingAppointments = new ArrayList<>();
        ArrayList<Integer> allRoomIdsWithConflict = new ArrayList<>();

        for (List<String> appointments : allRowsFromAppointment){
            Appointment app = new Appointment();
            int count = 0;
            /*for (String appointmentAttribute : appointments){
                for (int i = 0; i < appointments.size(); i++){*/
                    // Legger kun til rommene med bra nok kapasitet
            if (! appointments.get(11).equalsIgnoreCase("null") || appointments.get(11).length() < 1) {
                Appointment newAppointment = new Appointment(appointments.get(0), appointments.get(1),
                        appointments.get(2), appointments.get(3), appointments.get(4), appointments.get(5),
                        appointments.get(6), appointments.get(7), appointments.get(8), appointments.get(9),
                        appointments.get(10), appointments.get(11));
                if (checkIfAppointmentsCollide(appointment, newAppointment)){
                    collidingAppointments.add(newAppointment);
                    allRoomIdsWithConflict.add(newAppointment.getId());
                    System.out.println("Conflict with appointment '" + newAppointment.getTitle() + "' [ID=" + newAppointment.getId() + "]");
                }
            }
                //}
            //}
        }

        for (Room room : allCapableRooms){
            if (allRoomIdsWithConflict.contains(room.getId())){
                allCapableRooms.remove(room);
            }
        }

        System.out.println("Room selection done. Available rooms are: ");
        for (Room room : allCapableRooms){
            System.out.println("[ID=" + room.getId() + "] " + room.getName());
        }



        return new Query("roomLogic", allCapableRooms);
    }

    public static boolean checkIfAppointmentsCollide(Appointment app1, Appointment app2){
        // Skal sjekke om to appointments kolliderer
        LocalDateTime start1 = app1.getStartDate();
        LocalDateTime end1 = app1.getEndDate();
        LocalDateTime start2 = app2.getStartDate();
        LocalDateTime end2 = app2.getEndDate();
        if (start1.isBefore(start2) && end1.isAfter(start2)) return true;
        if (start1.isAfter(start2) && start1.isBefore(end2)) return true;
        if (start2.isBefore(start1) && end2.isAfter(start1)) return true;
        if (start2.isAfter(start1) && start2.isBefore(end1)) return true;

        return false;
    }

}
