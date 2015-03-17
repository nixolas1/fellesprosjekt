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
    Room room;
    UserModel owner;
    Boolean isVisible=true;
    Boolean isPrivate=false;
    Boolean allDay=false; //if work is chosen
    ArrayList<Calendar> cals = new ArrayList<>();
    public ArrayList<Attendee> attendees = new ArrayList<Attendee>();

    public Appointment() {
    }

    public Appointment(int id) {
        this.id = id;
    }

    public Appointment(String id, String title, String description, String location, String startDate,
                       String endDate,
                       String isVisible, String isAllDay, String isPrivate, String roomID){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        this.title = title;
        this.id = Integer.parseInt(id);
        this.purpose = description;
        this.startDate = LocalDateTime.parse(startDate, format);
        this.endDate = LocalDateTime.parse(endDate, format);
        this.isPrivate = Boolean.parseBoolean(isPrivate);
        this.allDay = Boolean.parseBoolean(isAllDay);
        this.isVisible = Boolean.parseBoolean(isVisible);
        this.cals = Calendar.getAllCalendarsInAppointment(this.id);
        this.attendees = Attendee.getAllAttendeesForAppointment(this.id);
        if(roomID != null) {
            String[] roomRow = server.database.Logic.getRow("Room", "roomid", roomID);
            String name = roomRow[1];
            int capacity = Integer.parseInt(roomRow[2]);
            int opensAt = Integer.parseInt(roomRow[3]);
            int closesAt = Integer.parseInt(roomRow[4]);
            this.room = new Room(Integer.parseInt(roomID), name, capacity, opensAt, closesAt, new ArrayList<Utility>());
        }
        if(location != null) this.location = location;
    }

    public Appointment(int id, String title, String purpose, LocalDateTime startDate, LocalDateTime endDate, Room room, UserModel owner, Calendar cal, String location){
        this.title=title;
        this.id=id;
        this.purpose=purpose;
        this.startDate=startDate;
        this.endDate=endDate;
        this.room=room;
        this.owner=owner;
        this.cals = Calendar.getAllCalendarsInAppointmentClientside(this.id, client.Main.socket);
        this.attendees = Attendee.getAllAttendeesForAppointmentClientside(this.id, client.Main.socket);
        this.location = location;
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
        return new Appointment(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8], a[11]);
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
    public ArrayList<Calendar> getCals() {
        return cals;
    }
    public ArrayList<Attendee> getAttendees() {
        return attendees;
    }
    public ArrayList<UserModel> getUsers() {
        ArrayList<UserModel> ret = new ArrayList<>();
        for(Attendee a : attendees)
            ret.add(a.getUser());
        return ret;
    }
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

    public void setAttendees(ArrayList<Attendee> attendees) {
        this.attendees = attendees;
    }
    public void addCalender(Calendar cal){cals.add(cal);}

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
    public void setCals(ArrayList<Calendar> cals) {
        this.cals = cals;
    }
    public Boolean getIsVisible() {return isVisible;}
    public void setIsVisible(Boolean isVisible) {this.isVisible = isVisible;}
    public Boolean getIsPrivate() {return isPrivate;}
    public void setIsPrivate(Boolean isPrivate) {this.isPrivate = isPrivate;}
    public Boolean getAllDay() {return allDay;}
    public void setAllDay(Boolean allDay) {this.allDay = allDay;}

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
        // todo id, title, purpose, location, startDate, endDate, calID, roomID
        return "Appointment ["+id+"] Title: " +title+ "\nPurpose: " +purpose+ "\nLocation: " +location+ "\nStart: " +startDate.toString()+
                "\nEnd: " +endDate.toString()+ "\nRoom: " +room+ "\nOwner: " +owner.displayInfo()+ "";

    }
    */
    public void setAppType() {}

    public static boolean checkIfAppointmentsCollide(Appointment app1, Appointment app2){
        // Skal sjekke om to appointments kolliderer
        LocalDateTime start1 = app1.getStartDate();
        LocalDateTime end1 = app1.getEndDate();
        LocalDateTime start2 = app2.getStartDate();
        LocalDateTime end2 = app2.getEndDate();
        if (start1.isBefore(start2) && end1.isAfter(start2)) return true;
        if (start1.isAfter(start2) && start1.isBefore(end2)) return true;
        if (start2.isBefore(start1) && end2.isAfter(start1)) return true;
        if (start2.isAfter(start1) && start2.isBefore(end1)) return true;

        return false;
    }


}
