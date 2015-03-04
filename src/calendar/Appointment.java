package calendar;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by nixo on 2/23/15.
 */
public class Appointment {

    String title;
    String purpose;
    Date startDate;
    Date endDate;
    Room room;
    UserModel owner;
    Calendar cal;
    public ArrayList<Attendee> attendees = new ArrayList<Attendee>();

    public Appointment(String title, String purpose, Date startDate, Date endDate, Room room, UserModel owner, Calendar cal){
        this.title=title;
        this.purpose=purpose;
        this.startDate=startDate;
        this.endDate=endDate;
        this.room=room;
        this.owner=owner;
        this.cal = cal;
    }

}
