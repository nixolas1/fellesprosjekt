package client.calendar;

import calendar.Appointment;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.ThreadClient;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private ThreadClient socket = new ThreadClient(); //TODO: REMOVE IN MASTER BRANCH

    private int dayOfMonth = displayDate.getDayOfMonth();
    private int sevenDays = 7;

    @FXML
    void initialize() {
        //chooseCalendar.setItems(FXCollections.observableArrayList("Gunnar Greve"));
        populateCalendars(new Integer[]{1, 2, 3});
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
        year.setText(displayDate.getYear() + "");
    }

    public void updateMonth() {

        month.setText(monthName(displayDate.getMonthValue()) + "");
    }
    TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
    public void updateWeekNum() {

        weekNum.setText(displayDate.get(woy) + "");
    }


    public void updateDate() {
        monDate.setText(dayOfMonth + "");
        tueDate.setText(dayOfMonth+1 + "");
        wedDate.setText(dayOfMonth+2 + "");
        thuDate.setText(dayOfMonth+3 + "");
        friDate.setText(dayOfMonth+4 + "");
        satDate.setText(dayOfMonth+5 + "");
        sunDate.setText(dayOfMonth+6 + "");
    }

    public void showNextWeek(ActionEvent event) {
        dayOfMonth = displayDate.getDayOfMonth() + sevenDays;
        updateDate();
        sevenDays += 7;

    }

    public void showLastWeek(ActionEvent event) {
        dayOfMonth = displayDate.getDayOfMonth() - sevenDays;
        updateDate();
        sevenDays -= 7;
    }

    public void showToday(ActionEvent event) {
        updateDate();
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
        for(int i : id)
            populateCalendar(i);
    }

    public void populateCalendar(int calID){
        ArrayList<Appointment> apps = Appointment.getAppointmentsInCalendar(calID, socket);
        for(Appointment app : apps){
            //only display appointments this week
            if(app.getStartDate().getDayOfYear()<displayDate.getDayOfYear()+7) {
                System.out.println(app.getTitle() +
                                ": den " + app.getStartDate()+
                                " i kalender "+app.getCal().getID()+
                                " i rom "+app.getRoom().getName()
                );

                AnchorPane pane = generateAppointmentPane(app);
                insertPane(pane, app.getStartDate(), app.getEndDate());
            }
        }
    }

    private AnchorPane generateAppointmentPane(Appointment app){

        String startText = app.getStartDate().format(DateTimeFormatter.ofPattern("HH:mm"));
        String endText = app.getEndDate().format(DateTimeFormatter.ofPattern("HH:mm"));

        final Label title = paneLabel(app.getTitle());
        final Label time = paneLabel(startText + " - " + endText);
        final Label location = paneLabel("");//TODO find solution for location display. app.getRoom().getName());

        AnchorPane pane = new AnchorPane(title, time, location);
        setAnchor(time, "top", 0);
        setAnchor(title, "top", 20);
        setAnchor(location, "bottom", 0);

        if(app.getEndDate().getHour()-app.getStartDate().getHour()<2)
            location.setText("");

        //style
        String color = "-fx-background-color: "+app.getCal().getColor(0.6);
        pane.setStyle(color);
        pane.setCursor(Cursor.HAND);

        //interaction
        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Pressed "+app.getTitle());
                //client.detailedAppointment.Main.show(Main.stage, app);
                event.consume();
            }
        });

        return pane;
    }

    private Label paneLabel(String text){
        final Label label = new Label(text);
        label.setMaxWidth(calendarGrid.getColumnConstraints().get(1).getPrefWidth()-5);
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


}

/*for(int day = 0; day<days; day++){
            for(int hour = 0; hour<hours; hour++){
                insertPane(, hour, day);
            }
        }*/