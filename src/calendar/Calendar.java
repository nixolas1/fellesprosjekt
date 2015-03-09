package calendar;

import javafx.scene.paint.Color;

import javax.jws.soap.SOAPBinding;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by nixo on 3/3/15.
 */
public class Calendar implements Serializable {
    String name = "";
    int id;
    public ArrayList<Appointment> appointments = new ArrayList<Appointment>();
    ArrayList<UserModel> members = new ArrayList<UserModel>();

    public Calendar(String name, int id, ArrayList<UserModel> members){
        this.name = name;
        this.members = members;
        this.id =
    }



    public Calendar(String name, int id){
        this.name = name;
        this.id = id;
    }
    public Calendar(int id){
        this.id = id;
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

    public int getID(){return this.id;}
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
}
