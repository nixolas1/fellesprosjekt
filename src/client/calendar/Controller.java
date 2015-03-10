package client.calendar;

import calendar.Appointment;
import javafx.collections.FXCollections;
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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
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



    protected static Stage primaryStage;

    private LocalDate displayDate = LocalDate.parse("2015-03-02");
    private LocalDate tempDate = LocalDate.now();
    private ThreadClient socket = new ThreadClient(); //TODO: REMOVE IN MASTER BRANCH


    @FXML
    void initialize() {
        //chooseCalendar.setItems(FXCollections.observableArrayList("Gunnar Greve"));
        updateYear();
        updateMonth();
        updateWeekNum();
        updateDate();
        populateCalendars(new Integer[]{1, 2, 3, 4});
    }

    public static String monthName(int month){
        String[] monthNames = {"januar", "februar", "mars", "april", "mai", "juni", "juli", "august", "september", "oktober", "november", "desember"};
        return monthNames[month - 1];
    }

    public void updateYear() {
        year.setText(tempDate.getYear() + "");
    }

    public void updateMonth() {
        month.setText(monthName(tempDate.getMonthValue()) + "");
    }

    public void updateWeekNum() {
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int weekNumber = tempDate.get(woy);
        weekNum.setText(weekNumber + "");
    }

    private LocalDate getLastMonday(LocalDate d) {
        return d;
    }

    public void updateDate() {
        monDate.setText(tempDate.getDayOfMonth() + "");
        tueDate.setText(tempDate.plusDays(1).getDayOfMonth() + "");
        wedDate.setText(tempDate.plusDays(2).getDayOfMonth() + "");
        thuDate.setText(tempDate.plusDays(3).getDayOfMonth() + "");
        friDate.setText(tempDate.plusDays(4).getDayOfMonth() + "");
        satDate.setText(tempDate.plusDays(5).getDayOfMonth() + "");
        sunDate.setText(tempDate.plusDays(6).getDayOfMonth() + "");
    }

    public void showNextWeek(ActionEvent event) {
        tempDate=tempDate.plusWeeks(1);
        updateWeekNum();
        updateDate();
        updateMonth();
        updateYear();

    }

    public void showLastWeek(ActionEvent event) {
        tempDate = tempDate.minusWeeks(1);
        updateWeekNum();
        updateDate();
        updateMonth();
        updateYear();
    }

    public void showToday(ActionEvent event) {
        tempDate = LocalDate.now();
        updateDate();
        updateWeekNum();
        updateMonth();
        updateYear();
    }


    public void onBtnShowNewAppointment(ActionEvent event) {
        client.newAppointment.Main newApp = new client.newAppointment.Main();
        try {
            newApp.showNewAppointment(primaryStage, Main.user);
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

    public void populateCalendars(Integer[] id){
        ArrayList<Appointment> apps = new ArrayList<>();
        for(int i : id){
            apps.addAll(Appointment.getAppointmentsInCalendar(i, socket));
        }
        populateCalendar(apps);
    }

    public void populateCalendar(int calID){
        ArrayList<Appointment> apps = Appointment.getAppointmentsInCalendar(calID, socket);
        populateCalendar(apps);
    }

    public void populateCalendar(ArrayList<Appointment> apps){
        for(Appointment app : apps){
            //only display appointments this week
            if(app.getStartDate().getDayOfYear()<displayDate.getDayOfYear()+7) {
                System.out.println(app.getTitle() +
                                ": den " + app.getStartDate()+
                                " i kalender "+app.getCal().getID()+
                                " i rom "+app.getRoom().getName()
                );

                AnchorPane pane = generateAppointmentPane(app, apps);
                insertPane(pane, app.getStartDate(), app.getEndDate());
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
        String color = "-fx-background-color: "+app.getCal().getColor(0.6)+" ";
        double padding = paneWidth*collisions.indexOf(app);
        System.out.println(color);
        pane.setStyle(color);
        pane.setCursor(Cursor.HAND);
        pane.setMaxWidth(paneWidth);
        //pane.setPadding(new Insets(0,0,0,padding));

        //interaction
        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Pressed "+app.getTitle());
                //client.detailedAppointment.Main.show(Main.stage, app);
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
        int col = startDate.getDayOfYear()-displayDate.getDayOfYear();
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