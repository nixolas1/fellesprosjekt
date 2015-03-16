package client.calendar;

import calendar.Appointment;
import calendar.UserModel;
import client.newAppointment.FxUtil;
import com.sun.org.apache.xpath.internal.operations.Bool;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Locale;

/**
 * Created by jonaslochsen on 02.03.15.
 */
public class Controller {


    //@FXML private ComboBox chooseCalendar;
    @FXML private Text name;
    @FXML private Button logOff;
    @FXML private Button userSettings;
    @FXML private Button yourCalendar;
    @FXML private Button today;
    @FXML private Text year;
    @FXML private Text month;
    @FXML private Text weekNum;
    @FXML private Text monDate;
    @FXML private Text tueDate;
    @FXML private Text wedDate;
    @FXML private Text thuDate;
    @FXML private Text friDate;
    @FXML private Text satDate;
    @FXML private Text sunDate;
    @FXML private GridPane calendarGrid;
    @FXML public ComboBox notifCombo, findUserCalendar, myCals;
    @FXML public Text notifCount;


    protected static Stage primaryStage;

    private LocalDate calDate = LocalDate.now();
    private ThreadClient socket = client.Main.socket;
    private Integer[] cals = new Integer[]{1,2,3,4};
    private Notifications notifs;
    private Hashtable<Integer, ArrayList<Appointment>> appointments = new Hashtable<>();
    private ArrayList<UserModel> allUsersUM;

    @FXML
    void initialize() {
        //chooseCalendar.setItems(FXCollections.observableArrayList("Gunnar Greve"));
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
        notifs = new Notifications(Main.user.getEmail(), notifCount, notifCombo);

    }

    public ArrayList<UserModel> getAllUserModels() {
        return UserModel.getAllUsers();
    }

    public ArrayList<String> userModelsToString(ArrayList<UserModel> users) {
        return UserModel.convertUserModelsToStringArrayList(users);
    }

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
        Calendar c = Calendar.getInstance(Locale.ENGLISH);
        c.setTime(Date.valueOf(d));
        int day = c.get(Calendar.DAY_OF_WEEK);
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

    public void populateCalendars(Integer[] id){
        ArrayList<Appointment> apps = new ArrayList<>();
        for(int i : id){
            apps.addAll(appointments.get(i));
        }
        populateCalendar(apps);
    }

    public Hashtable<Integer, ArrayList<Appointment>> getAppointments(Integer[] id){
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

                AnchorPane pane = generateAppointmentPane(app, apps);
                insertPane(pane, start, app.getEndDate());
            }
        }
    }

    private AnchorPane generateAppointmentPane(Appointment app, ArrayList<Appointment> apps){

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
         //todo add padding to collided items
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

    private void insertPane(AnchorPane pane, LocalDateTime startDate, LocalDateTime endDate) {
        int col = startDate.getDayOfYear()-calDate.getDayOfYear();
        int row = startDate.getHour();
        int rowspan = endDate.getHour()-startDate.getHour();
        insertPane(pane, col, row, 1, rowspan);
    }

    private void insertPane(AnchorPane pane, int col, int row, int colspan, int rowspan) {
        calendarGrid.add(pane, col, row, colspan, rowspan);
    }

    public double getColWidth(){
        return calendarGrid.getColumnConstraints().get(1).getPrefWidth();
    }

}

/*for(int day = 0; day<days; day++){
            for(int hour = 0; hour<hours; hour++){
                insertPane(, hour, day);
            }
        }*/