package server;

import calendar.Appointment;
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
            if(server.database.Logic.createAppointment(app))
                return new Query("newAppointment", true);
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
                ArrayList<Appointment> apps = getModelsFromDBOutput(
                        server.database.Logic.getAllRowsWhere("Appointment", "Calendar_calendarid = " + cal_id));
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
            appList.add(new Appointment(a.get(0), a.get(1), a.get(2), a.get(3), a.get(4), a.get(5), a.get(6), a.get(7), a.get(8), a.get(9)));
        }

        return appList;
    }

}
