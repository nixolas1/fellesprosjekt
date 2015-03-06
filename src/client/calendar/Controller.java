package client.calendar;

import calendar.Appointment;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.ThreadClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

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

    @FXML
    void initialize() {
        //chooseCalendar.setItems(FXCollections.observableArrayList("Gunnar Greve"));
        populateCalendars(new Integer[]{1, 2, 3});
    }

    public void updateYear() {
       // String yearNow = displayDate.getYear();
       // year.setText();
    }

    public void updateMonth() {

    }

    public void updateWeekNum() {

    }

    public void updateDate() {

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
        ThreadClient socket = new ThreadClient();
        ArrayList<Appointment> apps = Appointment.getAppointmentsInCalendar(calID, socket);
        for(Appointment app : apps){
            //only display appointments this week
            if(app.getStartDate().getDayOfYear()<displayDate.getDayOfYear()+7) {
                System.out.println(app.getTitle() + " " + app.getStartDate());
                AnchorPane pane = generateAppointmentPane(app);
                insertPane(pane, app.getStartDate(), app.getEndDate());
            }
        }
    }

    private AnchorPane generateAppointmentPane(Appointment app){
        AnchorPane pane = new AnchorPane();
        String color = app.getCal().color; color = "lightblue";
        pane.setStyle("-fx-background-color: "+color);
        return pane;
    }


    private void insertPane(AnchorPane pane, LocalDateTime startDate, LocalDateTime endDate) {
        int col = startDate.getDayOfYear()-displayDate.getDayOfYear();
        int row = startDate.getHour();
        insertPane(pane, col, row);
    }

    private void insertPane(AnchorPane pane, int row, int col) {

        calendarGrid.add(pane, row, col);
  }
}

/*for(int day = 0; day<days; day++){
            for(int hour = 0; hour<hours; hour++){
                insertPane(, hour, day);
            }
        }*/