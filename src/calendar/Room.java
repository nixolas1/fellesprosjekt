package calendar;

import network.Query;
import network.ThreadClient;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by nixo on 3/3/15.
 */
public class Room implements Serializable {
    String name;
    int capacity;
    int opensAt; //minutes since midnight
    int closesAt;
    int id;

    ArrayList<Utility> utilities;

    public Room(int id, String name, int capacity, int opensAt, int closesAt, ArrayList<Utility> utilities) {
        this.name = name;
        this.id = id;
        this.capacity = capacity;
        this.opensAt = opensAt;
        this.closesAt = closesAt;
        this.utilities = utilities;
    }

    public Room(int id, int capacity){
        this.id = id;
        this.capacity = capacity;
    }

    public Room(int id) { //TODO fix. gives null.
        String[] roomRow = server.database.Logic.getRow("Room", "roomid", Integer.toString(id));

        String name = roomRow[1];
        int capacity = Integer.parseInt(roomRow[2]);
        int opensAt = Integer.parseInt(roomRow[3]);
        int closesAt = Integer.parseInt(roomRow[4]);
        new Room(id, name, capacity, opensAt, closesAt, new ArrayList<Utility>());
    }

    public Room(String name) {
        this.name = name;
    }


    public Room(){
        // New room object
    }

    public Room(int id, String name, int capacity, int opensAt, int closesAt) {
        new Room(id, name, capacity, opensAt, closesAt, null);
    }

    public static ArrayList<Room> getAllRooms(ThreadClient socket) {
        System.out.println("getAllRooms()");
        try {
            Query reply = socket.send(new Query("getRooms"));
            Hashtable<String, ArrayList<Room>> response = reply.data;
            return response.get("reply");
        } catch (Exception e) {
            System.err.println("Unable to send or recieve rooms from server:");
            e.printStackTrace();
            return new ArrayList<Room>();
        }
    }

    public int getOpensAt() {
        return opensAt;
    }
    public int getClosesAt() {
        return closesAt;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Utility> getUtilities() {
        return utilities;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setOpensAt(int opensAt) {
        this.opensAt = opensAt;
    }

    public void setClosesAt(int closesAt) {
        this.closesAt = closesAt;
    }

    public void setUtilities(ArrayList<Utility> utilities) {
        this.utilities = utilities;
    }

    public String toString() {
        return this.name;
    }

    public int getCapacity() {
        return capacity;
    }

}
