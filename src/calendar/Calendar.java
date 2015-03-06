package calendar;

import javafx.scene.paint.Color;

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

    public Calendar(String name, int id){
        this.name = name;
        this.id = id;
    }
    public Calendar(int id){
        this.id = id;
    }

    public String getColor(){
        Random random = new Random(this.id*13379001);
        final float hue = random.nextInt(360);
        final float saturation = (random.nextInt(2000) + 1000) / 10000f;
        final float luminance = 0.9f;
        Color color =  Color.hsb(hue, saturation, luminance);
        return "#"+color.toString().substring(4);
    }

    public int getID(){return this.id;}
    public void addApointment(Appointment appoint){
        this.appointments.add(appoint);
    }

    public ArrayList<Appointment> getAppointments(){return appointments;}
}
