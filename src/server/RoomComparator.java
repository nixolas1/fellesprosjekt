package server;

import calendar.Room;

import java.util.Comparator;

/**
 * Created by Sondre on 17.03.15.
 */
public class RoomComparator  implements Comparator<Room> {

    @Override
    public int compare(Room room1, Room room2) {
        return room1.getCapacity() - room2.getCapacity();
    }
}
