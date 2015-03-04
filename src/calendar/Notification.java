package calendar;

import java.util.Date;

/**
 * Created by nixo on 3/4/15.
 */
public class Notification {
    Appointment app;
    Calendar cal;
    UserModel user;
    String text;
    boolean seen;
    Date sent;

    public Notification(Appointment app, Calendar cal, UserModel user, String text, boolean seen, Date sent){
        this.app=app;
        this.cal=cal;
        this.user=user;
        this.text=text;
        this.seen=seen;
        this.sent=sent;
    }
}
