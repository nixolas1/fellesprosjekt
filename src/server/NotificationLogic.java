package server;

import calendar.Notification;
import calendar.Room;
import calendar.UserModel;
import network.Query;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class NotificationLogic {

    public static Query getNotifications(Hashtable<String, String> data){
        try {
            String email = data.get("reply");
            ArrayList<Notification> notifs = getModelsFromDBOutput(
                    server.database.Logic.getAllRowsWhere("Notification", "User_email = '"+email+"'"));
            return new Query("getNotifications", notifs);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Query("getNotifications", false);
    }

    public static boolean newNotifications(Notification notif, ArrayList<UserModel> users){
        for(UserModel u : users) {
            notif.user = u;
            server.database.Logic.storeNotification(notif);
        }
        return true;
    }

    public static ArrayList<Notification> getModelsFromDBOutput(ArrayList<List<String>> list){
        ArrayList<Notification> notiList = new ArrayList<>();
        for (List<String>a : list){
            System.out.println(a.toString());
            notiList.add(new Notification(a.get(0), a.get(1), a.get(2), a.get(3), a.get(4)));
        }

        return notiList;
    }
}
