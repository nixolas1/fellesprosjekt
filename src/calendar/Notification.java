package calendar;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by nixo on 3/4/15.
 */
public class Notification implements Serializable {
    public Appointment app;
    public Calendar cal;
    public UserModel user;
    public String text;
    public boolean seen;
    public Date sent;
    public Date dateSeen;

    public Notification(String text, UserModel user, boolean seen){
        this.text=text;
        this.seen=seen;
        this.user=user;
    }

    public Notification(Appointment app, Calendar cal, UserModel user, String text, boolean seen, Date sent, Date dateSeen){
        this.app=app;
        this.cal=cal;
        this.user=user;
        this.text=text;
        this.seen=seen;
        this.sent=sent;
        this.dateSeen=dateSeen;
    }

}
