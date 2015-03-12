package calendar;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class Attendee implements Serializable {

    private UserModel user;
    private Appointment appointment;
    private Calendar cal;
    private LocalDateTime invited, answered;
    private Boolean attending, isOwner;
    public Attendee(UserModel user, Appointment appointment, Calendar cal, LocalDateTime invited, LocalDateTime answered, Boolean attending, Boolean isOwner){
        this.user=user;
        this.appointment=appointment;
        this.cal=cal;
        this.invited=invited;
        this.answered=answered;
        this.attending=attending;
        this.isOwner=isOwner;
    }

    public Attendee(UserModel user, Appointment app, Calendar cal, LocalDateTime invited, Boolean isOwner) {
        this.user=user;
        this.appointment=app;
        this.cal=cal;
        this.invited=invited;
        this.answered=null;
        this.attending=null;
        this.isOwner=isOwner;
    }
}
