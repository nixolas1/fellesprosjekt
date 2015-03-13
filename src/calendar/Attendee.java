package calendar;

import network.ClientDB;
import network.ThreadClient;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Attendee implements Serializable {

    private UserModel user;
    private int appointmentid;
    private Calendar cal;
    private LocalDateTime invited, answered;
    private Boolean attending, isOwner;


    public Attendee(UserModel user, int appointment, Calendar cal, LocalDateTime invited, LocalDateTime answered, Boolean attending, Boolean isOwner){
        this.user=user;
        this.appointmentid=appointment;
        this.cal=cal;
        this.invited=invited;
        this.answered=answered;
        this.attending=attending;
        this.isOwner=isOwner;
    }

    public Attendee(UserModel user, int app, Calendar cal, LocalDateTime invited, Boolean isOwner) {
        this.user=user;
        this.appointmentid=app;
        this.cal=cal;
        this.invited=invited;
        this.answered=null;
        this.attending=null;
        this.isOwner=isOwner;
    }

    public Attendee(UserModel user, Calendar cal, LocalDateTime invited, Boolean isOwner) {
        this.user=user;
        this.cal=cal;
        this.invited=invited;
        this.answered=null;
        this.attending=null;
        this.isOwner=isOwner;
    }

    public Attendee(String calid, String appid, String email, String timeInvited, String timeAnswered, String willAttend, String isOwner) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.user=UserModel.getUserFromServer(email);
        this.appointmentid=Integer.parseInt(appid);
        this.cal=new Calendar(Integer.parseInt(calid));
        if(timeInvited != null)
            this.invited=LocalDateTime.parse(timeInvited, format);
        if(timeAnswered != null)
            this.answered=LocalDateTime.parse(timeAnswered, format);
        if(willAttend != null)
            this.attending=Boolean.parseBoolean(willAttend);
        if(isOwner != null)
            this.isOwner=Boolean.parseBoolean(isOwner);
    }

    public static ArrayList<Attendee> getAllAttendeesForAppointment(int id) {
        ArrayList<List<String>> rows = server.database.Logic.getAllRowsWhere("Attendee", "Appointment_appointmentid = " + id);
        return getAppAttFromRows(rows);
    }

    public static ArrayList<Attendee> getAllAttendeesForAppointmentClientside(int id, ThreadClient socket) {
        ArrayList<List<String>> rows = ClientDB.getAllTableRowsWhere("Attendee", "Appointment_appointmentid = " + id, socket);
        return getAppAttFromRows(rows);
    }

    public static ArrayList<Attendee> getAppAttFromRows(ArrayList<List<String>> rows) {
        ArrayList<Attendee> ret = new ArrayList<>();
        for(List<String> c : rows){
            ret.add(new Attendee(c.get(0), c.get(1), c.get(2), c.get(3), c.get(4), c.get(5), c.get(6)));
        }
        return ret;
    }

}