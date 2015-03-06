package calendar;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nixo on 3/3/15.
 */
public class Calendar implements Serializable {
    String name;
    int id;
    public String color = "0xCCCCCC";
    public ArrayList<Appointment> appointments = new ArrayList<Appointment>();
    Group group;

    public Calendar(String name, int id, Group group){
        this.group = group;
        this.name = name;
        this.id = id;
    }

    public Calendar(String name, int id){
        this.name = name;
        this.id = id;
    }
    public Calendar(int id){
        this.id = id;
    }

    public void addApointment(Appointment appoint){
        this.appointments.add(appoint);
    }

    public ArrayList<Appointment> getAppointments(){return appointments;}
}
