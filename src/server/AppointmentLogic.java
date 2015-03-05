package server;

import calendar.Appointment;
import calendar.UserModel;
import network.Query;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by nixo on 3/4/15.
 */
public class AppointmentLogic {
    public static Query getUserAppointments(Hashtable<String, String> data){
        try {
            UserModel user = new UserModel();
            user.setUsername(data.get("username"));
            user.setDomain(data.get("domain"));
            if(server.database.Logic.inDatabase("User", "email", user.getEmail())){
                UserModel serverUser = server.database.Logic.getUser(user.getEmail());
                //TODO: get appointments from server
                ArrayList<Appointment> apps = new ArrayList<Appointment>();
                return new Query("userAppointments", "apps");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Query("userAppointments", false);
    }

}
