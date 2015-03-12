package client.notifications;

import calendar.Notification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nixo on 2/23/15.
 */
public class Notifications {
    ArrayList<Notification> notifications = new ArrayList<>();
    Text label = null;
    ComboBox list = null;
    int every = 15;

    public Notifications(String email, Text unreadCountLabel, ComboBox notificationList){
        notifications = Notification.getUserNotifications(email, client.Main.socket);
        label = unreadCountLabel;
        list = notificationList;

        Timer timer = new Timer();
        timer.schedule( new TimerTask() {
            public void run() {
                notifications = Notification.getUserNotifications(email, client.Main.socket);
                updateList();
            }
        }, 0, every*1000);
    }

    public int getNumberOfUnreadNotifications(){
        int count = 0;
        for(Notification notif : notifications){
            if(!notif.seen)count++;
        }
        return count;
    }

    public void updateList(){
        label.setText(""+getNumberOfUnreadNotifications());

        list.getItems().clear();
        ObservableList<String> notiInfo = FXCollections.observableArrayList();
        for(Notification n : notifications){
            notiInfo.add(n.text);           //TODO change to cellfactory
        }
        list.setItems(notiInfo);
    }


}
