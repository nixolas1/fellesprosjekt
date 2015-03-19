package server;

import calendar.*;
import calendar.Appointment;
import calendar.Notification;
import calendar.UserModel;
import network.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by nixo on 3/4/15.
 */
public class AppointmentLogic {

    public static Query newAppointment(Hashtable<String, Appointment> data){
        try {
            Appointment app = data.get("reply");
            if(server.database.Logic.createAppointment(app)) {
                Notification notif = new Notification(app, "Invitert til nytt møte: "+app.getTitle());
                NotificationLogic.newNotifications(notif, app.getUsers());
                return new Query("newAppointment", true);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return new Query("newAppointment", false);
    }

    public static Query getCalendarAppointments(Hashtable<String, String> data){
        try {
            String cal_id = data.get("id");
            System.out.println("Getting appointments for cal: "+cal_id);
            if(server.database.Logic.inDatabase("Calendar", "calendarid", cal_id)){
                String sqlQuery = "INNER JOIN Calendar_has_Appointment on " +
                        "Appointment.appointmentid = Calendar_has_Appointment.Appointment_appointmentid " +
                        "WHERE Calendar_has_Appointment.Calendar_calendarid=" + cal_id;
                ArrayList<List<String>>rows = server.database.Logic.getAllRowsQuery("Appointment", sqlQuery);
                ArrayList<Appointment> apps = getModelsFromDBOutput(rows);
                System.out.println(apps.toString());
                return new Query("getAppointments", apps);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Query("userAppointments", false);
    }

    public static ArrayList<Appointment> getModelsFromDBOutput(ArrayList<List<String>> list){
        ArrayList<Appointment> appList = new ArrayList<>();
        for (List<String>a : list){
            //System.out.println(a.toString());
            appList.add(new Appointment(a.get(0), a.get(1), a.get(2), a.get(3), a.get(4), a.get(5), a.get(6), a.get(7), a.get(8), a.get(11)));
        }

        return appList;
    }

    public static ArrayList<Appointment> getCalendarAppointments(String calendarId){
        try {
            String cal_id = calendarId;
            System.out.println("Getting appointments for cal: "+cal_id);
            if(server.database.Logic.inDatabase("Calendar", "calendarid", cal_id)){
                String sqlQuery = "INNER JOIN Calendar_has_Appointment on " +
                        "Appointment.appointmentid = Calendar_has_Appointment.Appointment_appointmentid " +
                        "WHERE Calendar_has_Appointment.Calendar_calendarid=" + cal_id;
                ArrayList<List<String>>rows = server.database.Logic.getAllRowsQuery("Appointment", sqlQuery);
                ArrayList<Appointment> apps = getModelsFromDBOutput(rows);
                System.out.println(apps.toString());
                return apps;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<Appointment>();
    }

    public static ArrayList<String> getListOfDistinctAttendees(Appointment appointment) {
        System.out.println("getNumberOfDistinctAttendees()");
        ArrayList<String> attendeeList = new ArrayList<String>();
        int numberOfConflicts = 0;                               // Teller for hvor mange overlapp man vil få når flere folk er med i flere grupper på samme arrangement
        int numberOfDistinctAttendees;                          // Det endelig antallet personer som deltar, uten overlapp
        //Appointment appointment = data.get("reply");           // Henter data'en fra Hashtable

        // Løper gjennom alle inviterte til en arrangement
        for (Attendee attendee : appointment.getAttendees()) {
            String email = attendee.getUser().getEmail();
            System.out.println("Attendee invited: " + email);
            if (!attendeeList.contains(email)) {
                attendeeList.add(email);
                //System.out.println("Added user '" + email + "'");
            } else {
                numberOfConflicts += 1;
                System.out.println("numberOfConflicts: " + numberOfConflicts);
            }
        }

        // Løper gjennom alle medlemmene i alle gruppekalenderene i appointment objektet
        for (Calendar groupCalendar : appointment.getCals()) {
            ArrayList<String> memberList = server.database.Logic.getUsersInGroupCalendar(groupCalendar.getId());
            System.out.println("\n\nGROUPID ADDED: " + groupCalendar.getId() + "\n\n");
            for (String email : memberList) {
                if (!attendeeList.contains(email)) {
                    attendeeList.add(email);
                    System.out.println("Member in group " + groupCalendar.getId() + " added: " + email);
                } else {
                    numberOfConflicts += 1;
                    System.out.println("Member '" + email + "' in group " + groupCalendar.getId() + " is already in attendee-list");
                    //System.out.println("numberOfConflicts: " + numberOfConflicts);
                }
            }
        }

        numberOfDistinctAttendees = attendeeList.size();
        System.out.println("numberOfConflicts: " + numberOfConflicts);
        System.out.println("numberOfDistinctAttendees: " + numberOfDistinctAttendees);

        return attendeeList;
    }

    public static Query updateAttending(Hashtable<String, String> data){
        String email = data.get("email");
        String appid = data.get("appid");
        String going = data.get("going");
        Appointment app = Appointment.getAppointmentFromDB(appid);
        server.database.Logic.updateRowField("Attendee",
                "User_email = '" + email + "' AND Appointment_appointmentid = " + appid,
                "willAttend = "+going
        );
        if(going.equals("0")) {
            Notification notif = new Notification(app, email + " kommer ikke på " + app.getTitle());
            NotificationLogic.newNotificationsFromEmail(notif, getListOfDistinctAttendees(app));
        }
        return new Query("updateAttending", true);
    }

}
