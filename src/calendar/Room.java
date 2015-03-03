package calendar;

/**
 * Created by nixo on 3/3/15.
 */
public class Room {
    String name;
    int capacity;
    int opensAt; //minutes since midnight
    int closesAt;
    public Room(String name, int capacity, int opensAt, int closesAt){
        this.name=name;
        this.capacity=capacity;
        this.opensAt=opensAt;
        this.closesAt=closesAt;
    }
}
