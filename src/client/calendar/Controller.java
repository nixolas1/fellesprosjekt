package client.calendar;

import calendar.Appointment;
import calendar.Calendar;
import calendar.Notification;
import calendar.UserModel;
import client.newAppointment.FxUtil;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import client.notifications.Notifications;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.ClientDB;
import network.ThreadClient;
import server.User;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.time.DayOfWeek;
import java.util.*;

/**
 * Created by jonaslochsen on 02.03.15.
 */
public class Controller {


    //@FXML private ComboBox chooseCalendar;
    @FXML private Text name, year, month, weekNum, monDate, tueDate, wedDate, thuDate, friDate, satDate, sunDate, notifCount;
    @FXML private Button logOff, userSettings, yourCalendar, today;
    @FXML private GridPane calendarGrid;
    @FXML public ComboBox<Notification> notifCombo;
    @FXML public ComboBox findUserCalendar, myCals;
    @FXML private ListView shownCals;

    protected static Stage primaryStage;

    private LocalDate calDate = LocalDate.now();
    private ThreadClient socket = client.Main.socket;
    private int privCal = Main.user.getPrivateCalendar();
    //private Integer[] cals = new Integer[]{privCal};
    private ArrayList<Integer> cals = new ArrayList<Integer>(Arrays.asList(privCal));
    private Notifications notifs;
    private Hashtable<Integer, ArrayList<Appointment>> appointments = new Hashtable<>();
    private ArrayList<UserModel> allUsersUM;
    private ArrayList<calendar.Calendar> myCalendars;
    private ObservableList<String> displayedCals = FXCollections.observableArrayList();
    private ArrayList<Integer> calendarsAtisplay = new ArrayList<Integer>();
    Timer timer = new Timer();
    Integer numUnread = 0;

    @FXML
    void initialize() {
        //chooseCalendar.setItems(FXCollections.observableArrayList("Gunnar Greve"));
        name.setText(Main.getLoggedUser().getFirstName() + " " + Main.getLoggedUser().getLastName());
        myCalendars = getMyCalsFromDB();
        myCals.setItems(FXCollections.observableArrayList(calendarsToString(myCalendars)));
        allUsersUM = getAllUserModels();
        findUserCalendar.setItems(FXCollections.observableArrayList(userModelsToString(allUsersUM)));
        FxUtil.autoCompleteComboBox(findUserCalendar, FxUtil.AutoCompleteMode.CONTAINING); // AutoComplete ON
        FxUtil.autoCompleteComboBox(myCals, FxUtil.AutoCompleteMode.CONTAINING);
        calDate = getLastMonday(calDate);
        updateYear();
        updateMonth();
        updateWeekNum();
        updateDate();
        appointments = getAppointments(cals);
        populateCalendars(cals);
        importFont();

        //Notification stuff
        notifCombo.setOnAction((event) -> {
            Notification cell = notifCombo.getSelectionModel().getSelectedItem();
            if(cell != null) {
                System.out.println("ComboBox Action (selected: " + cell.text + ")");
                // Set at seen
                ClientDB.updateRow("Notification",
                        "User_email = '" + Main.user.getEmail() + "' AND Appointment_appointmentid = " + cell.app.getId(),
                        "seen = 1",
                        client.Main.socket
                );
                if (cell.app.getId() != 0) {
                    client.detailedAppointment.Main detApp = new client.detailedAppointment.Main();
                    try {
                        detApp.showDetAppointment(primaryStage, Main.user, cell.app);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        notifs = new Notifications(Main.user.getEmail(), notifCount, notifCombo);
        timer.schedule( new TimerTask() {
            public void run() {
                notifs.refresh();
                if(notifs.unreadCount>numUnread){
                    numUnread=notifs.unreadCount;
                    Platform.runLater(() -> {
                        clearAppointments();
                        appointments = getAppointments(cals);
                        populateCalendars(cals);
                    });
                }
            }
        }, 0, notifs.every*1000);



        myCals.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                //System.out.println("Calendar cal = new Calendar(Integer.parseInt(myCals.getValue().toString().split(,)[1].trim()), myCals.getValue().toString().split',)[0].trim())");
                //System.out.println(myCals.getValue().toString().split(",")[1].trim() + "        " + myCals.getValue().toString().split(",")[0].trim());
                findUserCalendar.getEditor().setText("");
                Calendar cal = new Calendar(Integer.parseInt(myCals.getValue().toString().split(",")[1].trim()), myCals.getValue().toString().split(",")[0].trim());
                System.out.println("Viewing calendar " + cal.getName());
                clearAppointments();
                //System.out.println("CALS: length = " + cals.length);
                /*for (Integer in : cals){
                    System.out.println(in);
                }*/
                //displayedCals.add(cal.getName());
                //shownCals.setItems(displayedCals);
                //cals.add(cal.getId());
                cals = new ArrayList<Integer>(Arrays.asList(cal.getId()));
                //cals = new Integer[]{cal.getId()};
                appointments = getAppointments(cals);
                populateCalendars(cals);
            }
        });

        findUserCalendar.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                myCals.getEditor().setText("");
                UserModel user = getUserModelFromEmail(findUserCalendar.getValue().toString().split(",")[1].trim());
                System.out.println("Viewing " + user.getFirstName() + "'s calendar");
                clearAppointments(); //todo
                ArrayList<Calendar> userCal = calendar.Calendar.getMyCalendarsFromDB(user);
                cals = new ArrayList<Integer>(userCal.size());
                //cals = new Integer[userCal.size()];
                for (int i = 0; i < userCal.size(); i++) {
                    cals.add(userCal.get(i).getId());
                }
                appointments = getAppointments(cals);
                populateCalendars(cals);
            }
        });

    }

    public void getMainCalendar(){
        ArrayList<Calendar> asd = Calendar.getAllCalendarsFromDB();

    }

    public ArrayList<UserModel> getAllUserModels() {
        return UserModel.getAllUsers();
    }

    public ArrayList<String> userModelsToString(ArrayList<UserModel> users) {
        return UserModel.convertUserModelsToStringArrayList(users);
    }

    public ArrayList<String> calendarsToString(ArrayList<calendar.Calendar> cals) {
        return calendar.Calendar.convertCalendarsToStringArrayList(cals);
    }

    public static ArrayList<calendar.Calendar> getMyCalsFromDB() { return calendar.Calendar.getMyCalendarsFromDB(Main.getLoggedUser()); }

    public void importFont() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("")));
        } catch (IOException|FontFormatException e) {
            //Handle exception
        }
    }

    public static String monthName(int month){
        String[] monthNames = {"januar", "februar", "mars", "april", "mai", "juni", "juli", "august", "september", "oktober", "november", "desember"};
        return monthNames[month - 1];
    }

    public void updateYear() {
        year.setText(calDate.getYear() + "");
    }

    public void updateMonth() {
        month.setText(monthName(calDate.getMonthValue()) + "");
    }

    public void updateWeekNum() {
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int weekNumber = calDate.get(woy);
        weekNum.setText(weekNumber + "");
    }

    private LocalDate getLastMonday(LocalDate d) {
        java.util.Calendar c = java.util.Calendar.getInstance(Locale.ENGLISH);
        c.setTime(Date.valueOf(d));
        int day = c.get(java.util.Calendar.DAY_OF_WEEK);
        return d.minusDays(day-2);
    }

    public void updateDate() {
        monDate.setText(calDate.getDayOfMonth() + "");
        tueDate.setText(calDate.plusDays(1).getDayOfMonth() + "");
        wedDate.setText(calDate.plusDays(2).getDayOfMonth() + "");
        thuDate.setText(calDate.plusDays(3).getDayOfMonth() + "");
        friDate.setText(calDate.plusDays(4).getDayOfMonth() + "");
        satDate.setText(calDate.plusDays(5).getDayOfMonth() + "");
        sunDate.setText(calDate.plusDays(6).getDayOfMonth() + "");
    }

    public void showNextWeek(ActionEvent event) {
        calDate=calDate.plusWeeks(1);
        updateView();
    }

    public void showLastWeek(ActionEvent event) {
        calDate = calDate.minusWeeks(1);
        updateView();
    }

    public void showToday(ActionEvent event) {
        calDate = getLastMonday(LocalDate.now());
        updateView();
    }

    public void updateView(){
        updateDate();
        updateWeekNum();
        updateMonth();
        updateYear();

        clearAppointments();
        populateCalendars(cals);
    }


    public void onBtnShowNewAppointment(ActionEvent event) {
        client.newAppointment.Main newApp = new client.newAppointment.Main();
        try {
            newApp.showNewAppointment(primaryStage, Main.user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBtnShowNewRoom(ActionEvent event) {
        client.newRoom.Main newRoom = new client.newRoom.Main();
        try {
            newRoom.showNewRoom(primaryStage, Main.user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logOff(ActionEvent event) {
        client.login.Main.show(Main.stage);
    }

    public void showUserSettings(ActionEvent event) {
        client.usersettings.Main.show(Main.stage);
    }


    public void onBtnShowNewGroup(ActionEvent event) {
     client.newGroupCalendar.Main newGroup = new client.newGroupCalendar.Main();
       try {
            newGroup.showNewGroup(primaryStage, Main.user);
       } catch (Exception e) {
            e.printStackTrace();
       }
    }

    public void clearAppointments(){
        calendarGrid.getChildren().removeIf(AnchorPane.class::isInstance);
    }

    public void populateCalendars(ArrayList<Integer> id){
        ArrayList<Appointment> apps = new ArrayList<>();
        for(int i : id){
            apps.addAll(appointments.get(i));
        }
        populateCalendar(apps);
    }

    public Hashtable<Integer, ArrayList<Appointment>> getAppointments(ArrayList<Integer> id){
        Hashtable<Integer, ArrayList<Appointment>> apps = new Hashtable<>();
        for(Integer i : id){
            apps.put(i, Appointment.getAppointmentsInCalendar(i, socket));
        }
        return apps;
    }

    public void populateCalendar(int calID){
        ArrayList<Appointment> apps = Appointment.getAppointmentsInCalendar(calID, socket);
        populateCalendar(apps);
    }

    public void populateCalendar(ArrayList<Appointment> apps){
        for(Appointment app : apps){

            Boolean isRepeat=false, isThisWeek=false;
            LocalDateTime start = app.getStartDate();


            //display is its a repeating event and repeats this week
            /*if(app.getRepeatEvery()>0 && app.getEndRepeatDate().isAfter(calDate)){
                LocalDateTime nextRepeat = start, now=calDate.atStartOfDay(), tempEnd=app.getEndDate();
                while(nextRepeat.isBefore(now)){
                    nextRepeat = nextRepeat.plusDays(app.getRepeatEvery());
                    tempEnd = tempEnd.plusDays((app.getRepeatEvery()));
                    System.out.println(nextRepeat);
                }
                if(nextRepeat.isBefore(now.plusDays(7))) {
                    isRepeat = true;
                    app.setStartDate(nextRepeat);
                    app.setEndDate(tempEnd);
                }
            }*/

            //only display appointments this week
            if(start.toLocalDate().isBefore(calDate.plusDays(7))
                    && start.toLocalDate().isAfter(calDate.minusDays(1))) {
                isThisWeek = true;
            }


            if(isThisWeek || isRepeat){
/*                System.out.println(app.getTitle() +
                                ": den " + start+
                                " i kalender "+app.getCals()+
                                " i rom "+app.getRoom().getName()
                );*/

                Node pane = generateAppointmentPane(app, apps);
                insertPane(pane, start, app.getEndDate());
            }
        }
    }

    private Node generateAppointmentPane(Appointment app, ArrayList<Appointment> apps){

        ArrayList<Appointment> collisions = app.getCollisions(apps);
        int numCollisions = collisions.size();
        double paneWidth = getColWidth()/numCollisions;
        int duration = app.getEndDate().getHour()-app.getStartDate().getHour();

        String startText = app.getStartDate().format(DateTimeFormatter.ofPattern("HH:mm"));
        String endText = app.getEndDate().format(DateTimeFormatter.ofPattern("HH:mm"));

        final Label title = paneLabel(app.getTitle(), paneWidth);
        final Label time = paneLabel(startText + " - " + endText, paneWidth);
        final Label location = paneLabel("", paneWidth);//TODO find solution for location display. app.getRoom().getName());

        AnchorPane pane = new AnchorPane(title, time, location);
        setAnchor(time, "top", 0);
        setAnchor(title, "top", 20);
        setAnchor(location, "bottom", 0);

        if(duration<2)
            location.setText("");

        if(paneWidth<70){
            time.setText("");
            setAnchor(title, "top", 0);
        }
        if(paneWidth<50) {
            title.setText("");
        }

        //style
        System.out.println(app.toString());
        String color = "-fx-background-color: "+app.getCals().get(0).getColor(0.6)+" "; //TODO make correct calID
        double padding = paneWidth*collisions.indexOf(app);
        //System.out.println(color);
        pane.setStyle(color);
        pane.setCursor(Cursor.HAND);
        pane.setMaxWidth(paneWidth);
        //pane.setPadding(new Insets(0,0,0,padding));

        //interaction
        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Pressed "+app.getTitle());
                client.detailedAppointment.Main detApp = new client.detailedAppointment.Main();
                try {
                    detApp.showDetAppointment(primaryStage, Main.user, app);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                event.consume();
            }
        });

        if(numCollisions>0) {
            /*GridPane grid = new GridPane();
            grid.add(pane, collisions.indexOf(app), 0);
            grid.setMaxWidth(paneWidth);
            return grid;*/
        }

        return pane;
    }

    private Label paneLabel(String text, double width){
        final Label label = new Label(text);
        label.setMaxWidth(width - 5);
        label.setWrapText(true);
        return label;
    }

    private void setAnchor(Label label, String place, double pos){
        double padding = 5.0;
        AnchorPane.setLeftAnchor(label, padding);
        if(place.equals("top"))
            AnchorPane.setTopAnchor(label, padding+pos);
        else if(place.equals("bottom"))
            AnchorPane.setBottomAnchor(label, padding + pos);
    }

    private void insertPane(Node pane, LocalDateTime startDate, LocalDateTime endDate) {
        int col = startDate.getDayOfYear()-calDate.getDayOfYear();
        int row = startDate.getHour();
        int rowspan = endDate.getHour()-startDate.getHour();
        if(rowspan == 0)rowspan = 1;
        insertPane(pane, col, row, 1, rowspan);
    }

    private void insertPane(Node pane, int col, int row, int colspan, int rowspan) {
        calendarGrid.add(pane, col, row, colspan, rowspan);
    }

    public double getColWidth(){
        return calendarGrid.getColumnConstraints().get(1).getPrefWidth();
    }

    public UserModel getUserModelFromEmail(String email) {
        for (UserModel user : allUsersUM) {
            if(user.getEmail().equals(email)) return user;
        }
        return null;
    }

}

/*for(int day = 0; day<days; day++){
            for(int hour = 0; hour<hours; hour++){
                insertPane(, hour, day);
            }
        }*/