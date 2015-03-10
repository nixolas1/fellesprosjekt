package calendar;

import javafx.scene.paint.Color;
import network.Query;
import network.ThreadClient;

import javax.jws.soap.SOAPBinding;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

/**
 * Created by nixo on 3/3/15.
 */
public class Calendar implements Serializable {
    String name = "";
    String description = "";
    int id;
    public ArrayList<Appointment> appointments = new ArrayList<Appointment>();
    ArrayList<UserModel> members = new ArrayList<UserModel>();

    public Calendar(String name, ArrayList<UserModel> members){
        this.name = name;
        this.members = members;
    }

    public Calendar(String name){
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

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
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
}
