package calendar;

import network.Query;
import network.ThreadClient;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Hashtable;

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
    LocalDate endRepeatDate;
    int repeatEvery=0;
    Room room;
    UserModel owner;
    Calendar cal;
    Boolean appType;
    Boolean otherLocation; //if work is chosen

    public ArrayList<Attendee> attendees = new ArrayList<Attendee>();
    //                                                              2015-03-06 12:00:00.0

    public Appointment() {
    }

    public Appointment(String id, String title, String purpose, String location, String startDate, String endDate, String endRepeatDate, String repeatEveryXDays, String calID, String roomID){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        this.title = title;
        this.id = Integer.parseInt(id);
        this.purpose = purpose;
        this.startDate = LocalDateTime.parse(startDate, format);
        this.endDate = LocalDateTime.parse(endDate, format);
        this.cal = new Calendar(Integer.parseInt(calID));
        //can be null
        if(roomID != null) {
            String[] roomRow = server.database.Logic.getRow("Room", "roomid", roomID);
            String name = roomRow[1];
            int capacity = Integer.parseInt(roomRow[2]);
            int opensAt = Integer.parseInt(roomRow[3]);
            int closesAt = Integer.parseInt(roomRow[4]);
            this.room = new Room(Integer.parseInt(roomID), name, capacity, opensAt, closesAt, new ArrayList<Utility>());
        }

        if(location != null) this.location = location;
        if(repeatEveryXDays != null) this.repeatEvery = Integer.parseInt(repeatEveryXDays);
        if(endRepeatDate != null) this.endRepeatDate = LocalDate.parse(endRepeatDate, dateFormat);
    }

    public Appointment(int id, String title, String purpose, LocalDateTime startDate, LocalDateTime endDate, Room room, UserModel owner, Calendar cal, int repeatEvery, LocalDate endRepeatDate, String location){
        this.title=title;
        this.id=id;
        this.purpose=purpose;
        this.startDate=startDate;
        this.endDate=endDate;
        this.room=room;
        this.owner=owner;
        this.cal = cal;
        this.location = location;
        this.repeatEvery = repeatEvery;
        this.endRepeatDate=endRepeatDate;
    }

    public static ArrayList<Appointment> getAppointmentsInCalendar(int calID, ThreadClient socket){
        Hashtable<String, String> data = new Hashtable<String, String>(){{
            put("id",Integer.toString(calID));
        }};

        try {
            Query reply = socket.send(new Query("getAppointments", data));
            Hashtable<String, ArrayList<Appointment>> response = reply.data;
            return response.get("reply");
        }catch(Exception e){
            System.err.println("Unable to send or recieve appointments from server:");
            e.printStackTrace();
            return new ArrayList<Appointment>();
        }
    }

    public static Appointment getAppointmentFromDB(String id){ //Server side only!
        String[] a = server.database.Logic.getRow("Appointment", "appointmentid", id);
        return new Appointment(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8], a[9]);
    }

    public ArrayList<Appointment> getCollisions(ArrayList<Appointment> appointments){
        ArrayList<Appointment> colls = new ArrayList<>();
        for(Appointment app : appointments){
            if(this.getStartDate().isBefore(app.getEndDate()) && app.getStartDate().isBefore(this.getEndDate())){
                colls.add(app);
            }
        }
        return colls;
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
    public int getRepeatEvery(){return repeatEvery;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getEndRepeatDate() {
        return endRepeatDate;
    }

    public void setEndRepeatDate(LocalDate endRepeatDate) {
        this.endRepeatDate = endRepeatDate;
    }

    public void setRepeatEvery(int repeatEvery) {
        this.repeatEvery = repeatEvery;
    }

    public void setAttendees(ArrayList<Attendee> attendees) {
        this.attendees = attendees;
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

public String displayInfo() {
    return "Appointment ["+id+"]\nTitle: " +title+ "\nPurpose: " +purpose+ "\nLocation: " +location+ "\nStart: " +startDate.toString()+
            "\nEnd: " +endDate.toString()+ "\nRoom: " +room+ "\nOwner: " +owner.displayInfo();
}




    /*
    public String toString() {
        return "Appointment ["+id+"] Title: " +title+ "\nPurpose: " +purpose+ "\nLocation: " +location+ "\nStart: " +startDate.toString()+
                "\nEnd: " +endDate.toString()+ "\nRoom: " +room+ "\nOwner: " +owner.displayInfo()+ "";
    }

    public String ohString(){
        // todo id, title, purpose, location, startDate, endDate, endRepeatDate, repeatEveryXDays, calID, roomID
        return "Appointment ["+id+"] Title: " +title+ "\nPurpose: " +purpose+ "\nLocation: " +location+ "\nStart: " +startDate.toString()+
                "\nEnd: " +endDate.toString()+ "\nRoom: " +room+ "\nOwner: " +owner.displayInfo()+ "";

    }
    */
    public void setAppType() {}

}
