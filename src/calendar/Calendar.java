package calendar;

import client.*;
import javafx.scene.paint.Color;
import network.ClientDB;
import network.Query;
import network.ThreadClient;
import server.database.*;

import javax.jws.soap.SOAPBinding;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

/**
 * Created by nixo on 3/3/15.
 */
public class Calendar implements Serializable {
    String name = "";
    String description = "";
    int id=-1;
    public ArrayList<Appointment> appointments = new ArrayList<Appointment>();
    ArrayList<UserModel> members = new ArrayList<UserModel>();

    public Calendar(String name, ArrayList<UserModel> members){
        this.name = name;
        this.members = members;
    }

    public Calendar(int id){
        this.id = id;
    }

    public Calendar(String name){
        this.name = name;
    }

    public static ArrayList<Calendar> getAllCalendarsInAppointment(int id){
        ArrayList<List<String>> rows= server.database.Logic.getAllRowsWhere("Calendar_has_Appointment", "Appointment_appointmentid = " + id);
        ArrayList<Calendar> ret = new ArrayList<>();
        for(List<String> c : rows){
            ret.add(new Calendar(c.get(1))); //TODO check that it is correct index
        }
        return ret;
    }

    public static ArrayList<Calendar> getAllCalendarsInAppointmentClientside(int id, ThreadClient socket){
        ArrayList<List<String>> rows= ClientDB.getAllTableRowsWhere("Calendar_has_Appointment", "Appointment_appointmentid = " + id, socket);
        ArrayList<Calendar> ret = new ArrayList<>();
        for(List<String> c : rows){
            ret.add(new Calendar(c.get(1))); //TODO check that it is correct index
        }
        return ret;
    }

    public Calendar(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getColor(){
        return getColor(1.0);
    }

    public String getColor(double opacity){
        Random random = new Random(this.id*13379001);
        final int hue = random.nextInt(360);
        final double saturation = 0.7;
        final double luminance = 0.9;
        Color c =  Color.hsb(hue, saturation, luminance, opacity);
        return "rgba("+ c.getRed()*255+", "+c.getGreen()*255+", "+c.getBlue()*255+", "+c.getOpacity()+");";
    }

    public void addApointment(Appointment appoint){
        this.appointments.add(appoint);
    }

    public ArrayList<Appointment> getAppointments(){return appointments;}

    public ArrayList<UserModel> getMembers(){
        return this.members;
    }

    public void addAppointment(Appointment appointment){
        appointments.add(appointment);
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

  /*  public ArrayList<Calendar> getAllCalendarsFromDB() {
        try {
            Query reply = client.Main.socket.send(new Query("getAllCalendars", new ArrayList<Calendar>()));
            Hashtable<ArrayList<Calendar>, Boolean> response = reply.data;
            return response.get("reply");
        } catch(Exception e) {
            System.err.println("Unable to receive calendars from server:");
            e.printStackTrace();
            return null;
        }
    }*/

    public static ArrayList<Calendar> getAllCalendarsFromDB() {
        ArrayList<Calendar> cals = new ArrayList<>();
        ArrayList<List<String>> calendars = network.ClientDB.getAllTableRows("Calendar",client.Main.socket);
        for(List<String> li : calendars) {
            if(Boolean.parseBoolean(li.get(4))) cals.add(new Calendar(Integer.parseInt(li.get(0)), li.get(1)));
        }
        return cals;
    }

    public static int createGroupInDatabase(Calendar groupCalendar, ThreadClient socket){
        try {
            Query reply = socket.send(new Query("createGroup", groupCalendar));
            Hashtable<String, String> response = reply.data;
            return Integer.parseInt(response.get("reply"));
        }catch(Exception e){
            System.err.println("Unable to send or recieve appointments from server:");
            e.printStackTrace();
            return -1;
        }
    }

    public String displayInfo() {
        return name + ", " + id;
    }
}
