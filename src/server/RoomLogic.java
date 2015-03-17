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

    public static void initiateRoomLogicObject(Appointment appointment) {
        //System.out.println("initiateRoomLogicObject()");
        Hashtable<String, Appointment> hashTable = new Hashtable<String, Appointment>() {{
            put("data", appointment);
        }};
        initiateRoomLogic(hashTable);
    }

    public static Query initiateRoomLogic(Hashtable<String, Appointment> data){
        //System.out.println("initiateRoomLogic()");
        ArrayList<String> attendeeList = new ArrayList<>();
        int numberOfConflicts = 0;                           // Teller for hvor mange overlapp man vil få når flere folk er med i flere grupper på samme arrangement
        int numberOfDistinctAttendees;                      // Det endelig antallet personer som deltar, uten overlapp
        Appointment appointment = data.get("reply");        // Henter data'en fra Hashtable

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
                allRoomIds.add(Integer.valueOf(rooms.get(0)));
                allCapableRooms.add(room);
            }

        }


        ArrayList<List<String>> allRowsFromAppointment = server.database.Logic.getAllRows("Appointment");
        ArrayList<Appointment> collidingAppointments = new ArrayList<>();
        ArrayList<Integer> allRoomIdsWithConflict = new ArrayList<>();

        // Henter alle avtaler som er koblet på en rom-id
        for (List<String> appointments : allRowsFromAppointment){
            Appointment app = new Appointment();
            int count = 0;
            try{
                if (appointments.get(11) != null || ! appointments.get(11).equalsIgnoreCase("null") || appointments.get(11).length() < 1) {
                    Appointment newAppointment = new Appointment(appointments.get(0), appointments.get(1),
                            appointments.get(2), appointments.get(3), appointments.get(4), appointments.get(5),
                            appointments.get(6), appointments.get(7), appointments.get(8), appointments.get(11));
                    if (checkIfAppointmentsCollide(appointment, newAppointment)){
                        collidingAppointments.add(newAppointment);
                        allRoomIdsWithConflict.add(newAppointment.getId());
                        System.out.println("Conflict with appointment '" + newAppointment.getTitle() + "' [ID=" + newAppointment.getId() + "]");
                    }
                }
            } catch (NullPointerException e){
                System.out.println("NullPointerException triggered in initiateRoomLogic(). Appointment [ID=" + appointments.get(0) + "] has no roomid");
            }
        }

        for (Room room : allCapableRooms){
            if (allRoomIdsWithConflict.contains(room.getId())){
                allCapableRooms.remove(room);
            }
        }

        allCapableRooms.sort(new RoomComparator());

        System.out.println("\nRoom selection done. Available rooms are: ");
        for (Room room : allCapableRooms){
            System.out.println("[CAP=" + room.getCapacity() + "] [ID=" + room.getId() + "] " + room.getName());
        }

        /*System.out.println("\nColliding appointments: ");
        for (Appointment app: collidingAppointments){
            System.out.println(app.displayInfo());
        }*/

        return new Query("roomLogic", allCapableRooms);
    }

    public static boolean checkIfAppointmentsCollide(Appointment app1, Appointment app2){
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
