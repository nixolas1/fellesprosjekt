package network;

import server.GroupCalendar;
import server.database.Logic;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by nixo on 3/6/15.
 */
public class ClientDB {

    public static ArrayList<List<String>> getAllTableRows(String table, ThreadClient socket) {
        return getAllTableRowsWhere(table, "", socket);
    }

    public static ArrayList<List<String>> getAllTableRowsWhere(String table, String where, ThreadClient socket) {
        Hashtable<String, String> data = new Hashtable<String, String>() {{
            put("table", table);
            put("where", where);
        }};

        try {
            Query reply = socket.send(new Query("getRows", data));
            Hashtable<String, ArrayList> response = reply.data;
            return response.get("reply");
        } catch (Exception e) {
            System.err.println("Could not get rows:");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
