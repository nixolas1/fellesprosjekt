package server;

import calendar.UserModel;
import network.Query;


/**
 * Created by Sondre on 09.03.15.
 */
public class GroupCalendar {

    public static Query getLastGroupIdUsed() {
        int lastId = server.database.Logic.getLastGroupIdUsed();
        return new Query("getAllUsers", lastId);
    }
}
