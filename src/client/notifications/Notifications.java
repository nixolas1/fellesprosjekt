package client.notifications;

import calendar.Appointment;
import calendar.Notification;
import client.calendar.Controller.*;
import client.calendar.Main;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Callback;
import network.ThreadClient;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nixo on 2/23/15.
 */
public class Notifications {
    public ArrayList<Notification> notifications = new ArrayList<>();
    Text label = null;
    ComboBox<Notification> list = null;
    public int every = 2;
    public int unreadCount = 0;
    String email;
    ThreadClient socket;

    public Notifications(String email, Text unreadCountLabel, ComboBox<Notification> notificationList){
        this.socket = new ThreadClient();
        notifications = Notification.getUserNotifications(email, socket);
        label = unreadCountLabel;
        list = notificationList;
        this.email=email;
    }

    public void refresh(){
        notifications = Notification.getUserNotifications(email, socket);
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
            list.getItems().addAll(notifications);
            list.setCellFactory(new Callback<ListView<Notification>, ListCell<Notification>>() {
                @Override
                public ListCell<Notification> call(ListView<Notification> p) {
                    return new ListCell<Notification>() {
                        @Override
                        protected void updateItem(Notification item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item != null) {
                                setText(item.text);
                                if (!item.seen) {
                                    setTextFill(Color.RED);
                                }
                            }
                            else {
                                setText(null);
                            }

                        }
                    };
                }
            });
        });
    }


}
