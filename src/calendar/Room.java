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
    public Room(int id, String name, int capacity, int opensAt, int closesAt, ArrayList<Utility> utilities){
        this.name=name;
        this.id = id;
        this.capacity=capacity;
        this.opensAt=opensAt;
        this.closesAt=closesAt;
        this.utilities = utilities;
    }

    public Room(int id){
        String[] roomRow = server.database.Logic.getRow("Room", "roomid", Integer.toString(id));
        System.out.println(roomRow);
        new Room(id, "", 1, 420, 1300, new ArrayList<Utility>());
    }
    public Room(String name){
    this.name=name;
    }

    public Room(int id, String name, int capacity, int opensAt, int closesAt){
        new Room(id, name, capacity, opensAt, closesAt, null);
    }

    public static ArrayList<Room> getAllRooms(ThreadClient socket){
        System.out.println("getAllRooms()");
        try {
            Query reply = socket.send(new Query("getRooms"));
            Hashtable<String, ArrayList<Room>> response = reply.data;
            return response.get("reply");
        }catch(Exception e){
            System.err.println("Unable to send or recieve rooms from server:");
            e.printStackTrace();
            return new ArrayList<Room>();
        }
    }

    public int getCapacity() {
        return capacity;
    }

}
