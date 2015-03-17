package client.notifications;

import calendar.Notification;
import client.calendar.Controller.*;
import javafx.application.Platform;
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
    public ArrayList<Notification> notifications = new ArrayList<>();
    Text label = null;
    ComboBox list = null;
    public int every = 10;
    public int unreadCount = 0;
    String email;

    public Notifications(String email, Text unreadCountLabel, ComboBox notificationList){
        notifications = Notification.getUserNotifications(email, client.Main.socket);
        label = unreadCountLabel;
        list = notificationList;
        this.email=email;

    }

    public void refresh(){
        notifications = Notification.getUserNotifications(email, client.Main.socket);
        updateList();
    }

    public int getNumberOfUnreadNotifications(){
        int count = 0;
        for(Notification notif : notifications){
            if(!notif.seen)count++;
        }
        return count;
    }



    public void updateList(){
        Platform.runLater(() -> {
            unreadCount = getNumberOfUnreadNotifications();
            label.setText("" + unreadCount);
            list.getItems().clear();
            ObservableList<String> notiInfo = FXCollections.observableArrayList();
            for (Notification n : notifications) {
                notiInfo.add(n.text);           //TODO change to cellfactory
            }
            list.setItems(notiInfo);
        });
    }


}
