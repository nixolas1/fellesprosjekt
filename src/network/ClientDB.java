package network;

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

    public static Boolean updateAttendingStatus(String email, int appid, int going, ThreadClient socket) {
        Hashtable<String, String> data = new Hashtable<String, String>() {{
            put("email", email);
            put("appid", String.valueOf(appid));
            put("going", String.valueOf(going));
        }};

        try {
            Query reply = socket.send(new Query("updateAttending", data));
            Hashtable<String, Boolean> response = reply.data;
            return response.get("reply");
        } catch (Exception e) {
            System.err.println("Could not update Attending:");
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean updateRow(String table, String where, String set, ThreadClient socket) {
        Hashtable<String, String> data = new Hashtable<String, String>() {{
            put("table", table);
            put("where", where);
            put("set", set);
        }};

        try {
            Query reply = socket.send(new Query("updateRow", data));
            Hashtable<String, Boolean> response = reply.data;
            return response.get("reply");
        } catch (Exception e) {
            System.err.println("Could not update row:");
            e.printStackTrace();
            return false;
        }
    }
}
