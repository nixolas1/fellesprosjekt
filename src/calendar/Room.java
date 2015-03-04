package calendar;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by nixo on 3/3/15.
 */
public class Room {
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

    public Room(int id, String name, int capacity, int opensAt, int closesAt){
        new Room(id, name, capacity, opensAt, closesAt, new ArrayList<Utility>());
    }

}
