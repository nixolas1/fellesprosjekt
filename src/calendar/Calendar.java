package calendar;

import java.util.ArrayList;

/**
 * Created by nixo on 3/3/15.
 */
public class Calendar {
    String name;
    int id;
    public String color = "0xCCCCCC";
    public ArrayList<Appointment> appointments = new ArrayList<Appointment>();

    public Calendar(String name, int id){
        this.name = name;
        this.id = id;
    }

    public void addApointment(Appointment appoint){
        this.appointments.add(appoint);
    }

    public ArrayList<Appointment> getAppointments(){return appointments;}
}
