package calendar;

import server.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by nixo on 2/23/15.
 */
public class Appointment implements Serializable {

    int id;
    String title;
    String purpose;
    String location;
    LocalDateTime startDate;
    LocalDateTime endDate;
    LocalDateTime endRepeatDate;
    int repeatEvery;
    Room room;
    UserModel owner;
    Calendar cal;
    public ArrayList<Attendee> attendees = new ArrayList<Attendee>();

    public Appointment() {
    }

    public Appointment(String id, String title, String purpose, String location, String startDate, String endDate, String endRepeatDate, String repeatEveryXDays, String calID, String roomID){
        this.title = title;
        this.id = Integer.parseInt(id);
        this.purpose = purpose;
        this.startDate = LocalDateTime.parse(startDate);
        this.endDate = LocalDateTime.parse(endDate);
        this.cal = new Calendar(Integer.parseInt(calID));

        //can be null
        if(roomID != null) this.room = new Room(Integer.parseInt(roomID));
        if(location != null) this.location = location;
        if(repeatEveryXDays != null) this.repeatEvery = Integer.parseInt(repeatEveryXDays);
        if(endRepeatDate != null) this.endRepeatDate = LocalDateTime.parse(endRepeatDate);
    }

    public Appointment(int id, String title, String purpose, LocalDateTime startDate, LocalDateTime endDate, Room room, UserModel owner, Calendar cal){
        this.title=title;
        this.id=id;
        this.purpose=purpose;
        this.startDate=startDate;
        this.endDate=endDate;
        this.room=room;
        this.owner=owner;
        this.cal = cal;
    }

    public String getTitle() {
        return title;
    }
    public String getPurpose() {
        return purpose;
    }
    public LocalDateTime getStartDate() {
        return startDate;
    }
    public LocalDateTime getEndDate() {
        return endDate;
    }
    public Room getRoom() {
        return room;
    }
    public UserModel getOwner() {
        return owner;
    }
    public Calendar getCal() {
        return cal;
    }
    public ArrayList<Attendee> getAttendees() {
        return attendees;
    }
    public void addAttendee(Attendee attendee) {
        attendees.add(attendee);
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    public void setRoom(Room room) {
        this.room = room;
    }
    public void setOwner(UserModel owner) {
        this.owner = owner;
    }
    public void setCal(Calendar cal) {
        this.cal = cal;
    }

}
