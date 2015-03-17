package calendar;

import network.ClientDB;
import network.ThreadClient;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Attendee implements Serializable {

    private UserModel user;
    private int appointmentid;
    private LocalDateTime invited, answered, alarm;
    private Boolean attending, isOwner;


    public Attendee(UserModel user, int appointment, LocalDateTime invited, LocalDateTime answered, Boolean attending, Boolean isOwner){
        this.user=user;
        this.appointmentid=appointment;
        this.invited=invited;
        this.answered=answered;
        this.attending=attending;
        this.isOwner=isOwner;
    }

    public Attendee(UserModel user, int app, LocalDateTime invited, Boolean isOwner) {
        this.user=user;
        this.appointmentid=app;
        this.invited=invited;
        this.answered=null;
        this.attending=null;
        this.isOwner=isOwner;
    }

    public Attendee(UserModel user, LocalDateTime invited, Boolean isOwner) {
        this.user=user;
        this.invited=invited;
        this.answered=null;
        this.attending=null;
        this.isOwner=isOwner;
    }

    public Attendee(String email, String appid, String timeInvited, String timeAnswered, String willAttend, String isOwner, String alarm) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println(email);
        this.user=UserModel.getUserFromServer(email);
        this.appointmentid=Integer.parseInt(appid);

        if(timeInvited != null)
            this.invited=LocalDateTime.parse(timeInvited, format);
        if(timeAnswered != null)
            this.answered=LocalDateTime.parse(timeAnswered, format);
        if(willAttend != null)
            this.attending=Boolean.parseBoolean(willAttend);
        if(isOwner != null)
            this.isOwner=Boolean.parseBoolean(isOwner);
        if(alarm != null)
            this.alarm=LocalDateTime.parse(alarm, format);
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public int getAppointmentid() {
        return appointmentid;
    }

    public void setAppointmentid(int appointmentid) {
        this.appointmentid = appointmentid;
    }

    public LocalDateTime getInvited() {
        return invited;
    }

    public void setInvited(LocalDateTime invited) {
        this.invited = invited;
    }

    public LocalDateTime getAnswered() {
        return answered;
    }

    public void setAnswered(LocalDateTime answered) {
        this.answered = answered;
    }

    public LocalDateTime getAlarm() {
        return alarm;
    }

    public void setAlarm(LocalDateTime alarm) {
        this.alarm = alarm;
    }

    public Boolean getAttending() {
        return attending;
    }

    public void setAttending(Boolean attending) {
        this.attending = attending;
    }

    public Boolean getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(Boolean isOwner) {
        this.isOwner = isOwner;
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