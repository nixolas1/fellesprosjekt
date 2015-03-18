package calendar;

import network.Query;
import network.ThreadClient;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

/**
 * Created by nixo on 3/4/15.
 */
public class Notification implements Serializable {
    public Appointment app;
    public UserModel user;
    public String text="";
    public boolean seen=false;
    public LocalDateTime created=LocalDateTime.now();
    public LocalDateTime dateSeen=LocalDateTime.now();

    public Notification(String text, UserModel user, boolean seen){
        this.text=text;
        this.seen=seen;
        this.user=user;
    }

    public Notification(Appointment app, Calendar cal, UserModel user, String text, boolean seen, LocalDateTime created, LocalDateTime dateSeen){
        this.app=app;
        this.user=user;
        this.text=text;
        this.seen=seen;
        this.created=created;
        this.dateSeen=dateSeen;
    }

    public Notification(String appid, String email, String text, String seen, String created){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

        this.app=Appointment.getAppointmentFromDB(appid);
        this.user=server.database.Logic.getUser(email);
        this.text=text;
        System.out.println("Notifs: "+seen);
        if(seen != null)
            this.seen=!seen.equals("0");
        System.out.println(this.seen);
        if(created != null)
            this.created=LocalDateTime.parse(created, format);
    }

    public Notification(Appointment app, String text){

        this.app=app;
        this.text=text;

    }

    public Notification(String text){
        this.app=new Appointment(0);
        this.text=text;
    }

    public static ArrayList<Notification> getUserNotifications(String email, ThreadClient socket){
        System.out.println("Trying to get all notifications from user "+email);
        try {
            Query reply = socket.send(new Query("getNotifications", email));
            Hashtable<String, ArrayList<Notification>> response = reply.data;
            System.out.println("Got notifications: "+response.get("reply").toString());
            return response.get("reply");
        }catch(Exception e){
            System.err.println("Unable to send or recieve Notifications from server:");
            e.printStackTrace();
            return new ArrayList<Notification>();
        }
    }

}
