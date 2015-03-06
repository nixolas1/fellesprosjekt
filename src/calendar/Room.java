package calendar;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

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
        new Room(id, name, capacity, opensAt, closesAt, new ArrayList<Utility>());
    }

}
