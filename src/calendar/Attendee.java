package calendar;

import java.io.Serializable;
import java.util.Date;

public class Attendee implements Serializable {

    UserModel user;
    Appointment appointment;
    Calendar cal;
    Date invited, answered;
    Boolean attending, isOwner;
    public Attendee(UserModel user, Appointment appointment, Calendar cal, Date invited, Date answered, Boolean attending, Boolean isOwner){
        this.user=user;
        this.appointment=appointment;
        this.cal=cal;
        this.invited=invited;
        this.answered=answered;
        this.attending=attending;
        this.isOwner=isOwner;
    }
}
