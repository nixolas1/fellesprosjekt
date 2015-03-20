package server.database;

import calendar.*;
import network.Query;
import com.sun.org.apache.regexp.internal.RESyntaxException;
import server.AppointmentLogic;
//import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Sondre on 26.02.15.
 */
public class Logic {

    private static Connection conn;

    public Logic(Connection connection){
        conn = connection;
    }

    public static void setConn(Connection connection){
        conn = connection;
    }


    public static String[] getRow(String table, String identifyingAttribute, String identifyingValue){
        String getRow = "SELECT * FROM " + table + " WHERE " + identifyingAttribute + " = '" + identifyingValue + "';";
        String[] row = new String[getNumberOfColumns(table)];
        Statement stmt = null;
        ResultSet result = null;

        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(getRow);
        } catch (SQLException e) {
            System.out.println("SQLException triggered in getRow(), 1. try block:  " + e);
        }
        try {
            if(result.next()){
                for (int i = 0; i < row.length; i++) {
                    row[i] = result.getString(i + 1);
                    //TODO fuck u System.out.println("row[" + i + "]: " + row[i]);
                }
            } else throw new NullPointerException(identifyingAttribute + ": " + identifyingValue + " has no entry in table " + table);
        } catch (SQLException e){
            System.out.println("SQLException triggered in getRow(), 2. try block: " + e);
        }
        finally {
            closeDB(stmt);
        } return row;
    }

    public static Query sendAllRows(Hashtable<String, String> data){
        try {
            String table = data.get("table");
            String where = data.get("where");
            ArrayList<List<String>> rows = null;
            if(where == null || where.equals("")){
                rows = getAllRows(table);
            }else{
                rows = getAllRowsWhere(table, where);
            }

            return new Query("getRows", rows);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Query("getRows", false);
    }

    public static Query updateRow(Hashtable<String, String> data){
        try {
            String table = data.get("table");
            String where = data.get("where");
            String set = data.get("set");
            Boolean reply = updateRowField(table, where, set);
            return new Query("getRows", reply);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Query("getRows", false);
    }

    public static ArrayList<List<String>> getAllRows(String table) {
        return getAllRowsWhere(table, null);
    }
    public static ArrayList<List<String>> getAllRowsWhere(String table, String where){
        String whereText = where != null ? " WHERE "+where : "";
        return getAllRowsQuery(table, whereText);
    }
    public static ArrayList<List<String>> getAllRowsQuery(String table, String query){
        String getRow = "SELECT * FROM " + table + " " + query + ";";
        String[] row = new String[getNumberOfColumns(table)];
        Statement stmt = null;
        ResultSet result = null;
        ArrayList<List<String>> allRows = new ArrayList<List<String>>(getNumberOfRows(table));
        int count = 0;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(getRow);
        } catch (SQLException e) {
            System.out.println("SQLException triggered in getRows(), 1. try block:  " + e);
        }
        try {
            while(result.next()){
                allRows.add(new ArrayList<String>());
                for (int i = 0; i < row.length; i++) {
                    allRows.get(count).add(result.getString(i + 1));
                    //TODO fuck System.out.println(table + "[" + count + "][" + i + "]: " + result.getString(i + 1));
                } count ++;
                //System.out.println("");
            }
        } catch (SQLException e){
            System.out.println("SQLException triggered in getRows(), 2. try block: " + e);
        }
        finally {
            closeDB(stmt);
        } return allRows;
    }


    public static ArrayList<UserModel> getAllUsers() {
        String getRow = "SELECT * FROM User;";
        Statement stmt = null;
        ResultSet result = null;
        ArrayList<UserModel> allUsers = new ArrayList<UserModel>(getNumberOfRows("User"));

        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(getRow);
        } catch (SQLException e) {
            System.out.println("SQLException triggered in getRows(), 1. try block:  " + e);
        }
        try {
            while (result.next()) {
                allUsers.add(new UserModel(result.getString("username"),
                                            result.getString("passwordHash"),
                                            result.getString("domain"),
                                            result.getString("firstName"),
                                            result.getString("lastName"),
                                            result.getString("phone")));
                /*System.out.println("\nUSER\nusername: " + result.getString("username") + "\npasswordHash: " +
                        result.getString("passwordHash") + "\ndomain: " +
                        result.getString("domain") + "\nfirstName: " +
                        result.getString("firstName") + "\nlastName: " +
                        result.getString("lastName") + "\nphone: " +
                        result.getString("phone"));*/
            }
        } catch (SQLException e) {
            System.out.println("SQLException triggered in getRows(), 2. try block: " + e);
        } finally {
            closeDB(stmt);
        }
        return allUsers;
    }


    public static int getNumberOfRows(String table) {
        String getNumberOfColumns = "SELECT COUNT(*) FROM " + table;
        ResultSet result = null;
        Statement stmt = null;
        int numberOfRows = 0;

        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(getNumberOfColumns);
        } catch (SQLException e) {
            System.out.println("SQLExeption triggered in getNumberOfRows(), 1. try block: " + e);
        }
        try {
            if (result.next()) {
                numberOfRows = result.getInt(1);
                //System.out.println("numberOfRows: " + numberOfRows);
            } else throw new NullPointerException(table + " has no columns");
        } catch (SQLException e) {
            System.out.println("SQLExeption triggered in getNumberOfRows(), 2. try block: " + e);
        } finally {
            closeDB(stmt);
        } return numberOfRows;
    }


    public static int getLastGroupIdUsed(){
        String query = "SELECT calendarid FROM Calendar ORDER BY calendarid DESC LIMIT 1;";
        ResultSet result = null;
        Statement stmt = null;
        int lastIdUsed = 0;

        try{
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException e){
            System.out.println("SQLException triggered in getLastGroupIdUsed(), 1. catch block: " + e);
        } try {
            if (result.next()){
                lastIdUsed = result.getInt(1);
                //System.out.println("lastIdUsed: " + lastIdUsed);
            }
        } catch (SQLException f){
            System.out.println("SQLException triggered in getLastGroupIdUsed(), 2. catch block: " + f);
        } finally {
            closeDB(stmt);
        } return lastIdUsed;
    }

    public static boolean addAppointment(Appointment appointment){
        return true;
    }

    public static boolean createAppointment(Appointment app){

        String q1 = "INSERT INTO `nixo_fp`.`Appointment` (`appointmentid`, `title`, `description`, `location`, `startTime`, `endTime`, `isVisible`, `isAllDay`, `isPrivate`, `Room_roomid1`) VALUES ";

        String location = app.getLocation() != null && app.getLocation().length() > 0 ?
                ( "'" + app.getLocation() + "'" ) : "NULL" ;
        String description = app.getPurpose() != null && app.getPurpose().length() > 0 ?
                ("'" + app.getPurpose() + "'") : "NULL";
        String roomId = app.getRoom() != null ? String.valueOf(app.getRoom().getId()) : "NULL";
        String isPrivate = app.getIsPrivate().toString();
        String allDay = app.getAllDay() ? "1" : "0";
        String isVisible = app.getIsVisible().toString();
        /*
        this.cals = Calendar.getAllCalendarsInAppointment(this.id);
        this.attendees = Attendee.getAllAttendeesForAppointment(this.id);
         */


        Statement stmt = null;

        try {
            stmt = conn.createStatement();
                   //appointmentid 	   title                description 	location        startTime	              endTime                repeatEndDate	   repeat	 isVisible	    isAllDay	isPrivate	Room_roomid1
            //`appointmentid`, `title`, `description`, `location`, `startTime`, `endTime`, `repeatEndDate`, `repeat`, `isVisible`, `isAllDay`, `isPrivate`, `Room_roomid1`
            String q2 = "(NULL, '"
                    +app.getTitle()+"', "
                    +description+", "
                    +location+", '"
                    +app.getStartDate()+"', '"
                    +app.getEndDate()+"', "
                    +isVisible+", "
                    +allDay+", "
                    +isPrivate+", "
                    +roomId+") ";

            //String q3 = "RETURNING Appointment.appointmentid INTO ?;";
            //System.out.println(q1+q2);
            stmt.executeUpdate(q1 + q2, Statement.RETURN_GENERATED_KEYS);

            ResultSet result = stmt.getGeneratedKeys();
            if(result.next())
                app.setId(result.getInt(1));

            String calHasAppQuery = "INSERT INTO `nixo_fp`.`Calendar_has_Appointment` (`Appointment_appointmentid`, `Calendar_calendarid`) VALUES ('"+app.getId()+"', '";
            for(Calendar c : app.getCals()){
                //System.out.println(calHasAppQuery + c.getId() + "');");
                stmt.executeUpdate(calHasAppQuery + c.getId() + "');");
            }

            String attQuery = "INSERT INTO `nixo_fp`.`Attendee` (`User_email`, `Appointment_appointmentid`, `timeInvited`, `timeAnswered`, `willAttend`, `isOwner`, `alarm`) VALUES ('";
            String s = "', '";
            String n = "NULL";
            String ownerEmail = "";

            for(Attendee a : app.getAttendees()){
                if(a.getIsOwner()) {
                    ownerEmail = a.getUser().getEmail();
                    break;
                }

            }

            for(String email : AppointmentLogic.getListOfDistinctAttendees(app)){
                String isOwner = "0";
                if(email.equals(ownerEmail))isOwner="1";
                String q = attQuery + email+s+app.getId()+s+LocalDateTime.now()+"', NULL, '1', "+isOwner+", NULL);";
                //System.out.println(q);
                stmt.executeUpdate(q);
            }

            //System.out.println("Appointment [Title='" + app.getTitle() + "'] successfully created in database");
        } catch (SQLException e) {
            System.out.println("SQLException triggered in createAppointment(): " + e);
            return false;
        } finally {
            closeDB(stmt);
        }
        return true;
    }

    public static boolean updateAppointment(Appointment app){
        System.out.println("updateAppointment()");
        String location = app.getLocation() != null && app.getLocation().length() > 0 ?
                ( "'" + app.getLocation() + "'" ) : "NULL" ;
        String description = app.getPurpose() != null && app.getPurpose().length() > 0 ?
                ("'" + app.getPurpose() + "'") : "NULL";
        String roomId = app.getRoom() != null ? String.valueOf(app.getRoom().getId()) : "NULL";
        String isPrivate = app.getIsPrivate() ? "1" : "0";
        String isAllDay = app.getAllDay() ? "1" : "0";
        String isVisible = app.getIsVisible() ? "1" : "0";

        String qry ="UPDATE  `nixo_fp`.`Appointment` SET  `title` =  '"+app.getTitle()+"', `description` = "+description+", `location` = "+location+
                    ", `startTime` = '"+app.getStartDate()+"', `endTime` = '"+app.getEndDate()+"', `isVisible` = "+isVisible+", `isPrivate` = "+isPrivate+
                    ", `isAllDay` = "+isAllDay+", `Room_roomid1` = "+roomId+" WHERE  `Appointment`.`appointmentid` = "+app.getId()+";";

        String query = "UPDATE Appointment SET title = '" + app.getTitle() + "', description = " + description + ", location = " + location
                + ", startTime = '" + app.getStartDate() + "', endTime = '" + app.getEndDate() + "', repeatEndDate = NULL, repeat = NULL, isVisible = " +
                isVisible + ", isAllDay = " + isAllDay + ", isPrivate = " + isPrivate + ", Room_roomid1 = " + roomId +
                " WHERE appointmentid = " + app.getId() + ";";

        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            System.out.println("QUERY: " + qry);
            stmt.executeUpdate(qry);
            System.out.println("Appointment '" + app.getTitle() + "' successfullt updated in database");
        } catch (SQLException e) {
            System.out.println("SQLException triggered in createAppointment(): " + e);
            return false;
        } finally {
            closeDB(stmt);
        } return true;
    }

    public static int getLastRoomIdUsed(){
        String query = "SELECT roomid FROM Room ORDER BY roomid DESC LIMIT 1;";
        ResultSet result = null;
        Statement stmt = null;
        int lastIdUsed = 0;

        try{
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException e){
            System.out.println("SQLException triggered in getLastRoomIdUsed(), 1. catch block: " + e);
        } try {
            if (result.next()){
                lastIdUsed = result.getInt(1);
                //System.out.println("lastIdUsed: " + lastIdUsed);
            }
        } catch (SQLException f){
            System.out.println("SQLException triggered in getLastRoomIdUsed(), 2. catch block: " + f);
        } finally {
            closeDB(stmt);
        } return lastIdUsed;
    }

    public static Query createRoom(Hashtable<String, Room> data) {
        Room room = data.get("reply");
        System.out.println(data.get("reply"));
        int roomId = getLastRoomIdUsed() + 1;
        System.out.println(roomId);
        System.out.println(room.getName());
        Statement stmt = null;
        String query = "INSERT INTO Room (roomid, name, capacity, opensAt, closesAt) VALUES (" + roomId + ", '" + room.getName() + "', " + room.getCapacity() + ", " + room.getOpensAt() + ", " + room.getClosesAt() + ");";
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println(String.format("Room '%s' successfully added to Room with id = %s", room.getName(), roomId));
        } catch (SQLException e) {
            System.out.println("SQLException triggered in createRoom(), 1. try block: " + e);
            return new Query("createRoom", false);
        }
        if(room.getUtilities()!= null) {
            for (int i = 0; i < room.getUtilities().size(); i++) {
                try {
                    String utilityQuery = "INSERT INTO Room_has_Utility (Room_roomid, Utility_utilityid) VALUES (" + roomId + ", " + room.getUtilities().get(i).getId() + ");";
                    stmt = conn.createStatement();
                    stmt.executeUpdate(utilityQuery);
                } catch (SQLException e) {
                    System.out.println("SQLException triggered in createRoom(), 2. try block: " + e);
                    return new Query("createRoom", false);
                } catch (Exception f) {
                    System.out.println("Exception triggered in createRoom(): " + f);
                }
            }
        }
        closeDB(stmt);
        System.out.println(String.format("Room '%s' successfully added to Room with id = %s", room.getName(), roomId));
        return new Query("createRoom", true);
    }
//INSERT INTO `nixo_fp`.`Notification` (`Appointment_appointmentid`, `User_email`, `text`, `seen`, `sent`) VALUES
// ('1', 'admin@stud.ntnu.no', 'Invitert til Gruppemøte med gruppe 19', NULL, '2015-03-16 14:00:00');


    public static Boolean storeNotification(Notification n){
        String query = "INSERT INTO `nixo_fp`.`Notification` (`Appointment_appointmentid`, `User_email`, `text`, `seen`, `sent`) VALUES ('"+
                n.app.getId() + "', '" +
                n.user.getEmail() + "', '" +
                n.text + "', '" +
                "0', '" +
                LocalDateTime.now() + "');";
        System.out.println("query: " + query);
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("Stored notification "+n.text+" for "+n.user.getEmail());
            return true;
        } catch (SQLException f) {
            System.out.println("SQLException triggered in createUser(): " + f);
        }
        return false;
    }


    public static Query createGroup(Hashtable<String, Calendar> data){
        Calendar groupCalendar = data.get("reply");
        int groupId = getLastGroupIdUsed() + 1;
        int isPrivate = data.get("private") == null ? 0 : 1;
        int isGroup = isPrivate == 1 ? 0:1;
        //int groupId = groupCalendar.getId();
        String memberQuery = "INSERT INTO User_has_Calendar (Calendar_calendarid, User_email, isVisible, notifications, isPrivate) VALUES (" + groupId + ", ";
        String calendarQuery = "INSERT INTO Calendar (calendarid, name, description, isGroup) VALUES (" + groupId + ", '" + groupCalendar.getName() + "', '" + groupCalendar.getDescription() + "', "+isGroup+");";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            //System.out.println("QUERY: " + calendarQuery);
            stmt.executeUpdate(calendarQuery);
            System.out.println(String.format("GroupCalendar '%s' successfully added to Calendar with id = %s", groupCalendar.getName(), groupId));
        } catch (SQLException e){
            System.out.println("SQLException triggered in createGroup(), 1. try block: " + e);
        }
        for (UserModel user : groupCalendar.getMembers()){
            try {
                stmt = conn.createStatement();
                //System.out.println("QUERY: " + memberQuery + "'" + user.getEmail() + "', 1, 1);");
                stmt.executeUpdate(memberQuery + "'" + user.getEmail() + "', 1, 1, "+isPrivate+");");
                System.out.println(String.format("User '%s' successfully added to GroupCalendar with id = %s", user.getEmail(), groupId));

            } catch (SQLException e) {
                System.out.println("SQLExeption triggered in createGroup(): " + e);
                System.out.println("This happend during inserting user '" + user.getEmail() + "' into GroupCalendar");
                return new Query("createGroup", false);
            } catch (Exception f) {
                System.out.println("Exception triggered in createGroup():  " + f);
                return new Query("createGroup", false);
            }
        } closeDB(stmt);
        server.NotificationLogic.newNotifications(
                new Notification("Du er nå medlem av gruppen: "+groupCalendar.getName()),
                groupCalendar.getMembers());
        System.out.println(String.format("Group '%s' successfully created in database with id = %s ", groupCalendar.getName(), groupId));
        return new Query("createGroup", true);
    }

    public static int getNumberOfColumns(String table) {
        String getNumberOfColumns = "SELECT COUNT(*) totalColumns FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name =  '" + table + "' AND TABLE_SCHEMA=(SELECT DATABASE())";
        ResultSet result = null;
        Statement stmt = null;
        int numberOfColumns = 0;

        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(getNumberOfColumns);
        } catch (SQLException e) {
            System.out.println("SQLExeption triggered in getNumberOfColumns(), 1. try block: " + e);
        } catch (Exception f){
            System.out.println("Exception triggered in getNumberofColumns(), 1. try block: "+ f);
        }
        try {
            if (result.next()) {
                numberOfColumns = result.getInt(1);
                //System.out.println("numberOfColumns: " + numberOfColumns);
            } else throw new NullPointerException(table + " has no columns");
        } catch (SQLException e) {
            System.out.println("SQLExeption triggered in getNumberOfColumns(), 2. try block: " + e);
        } catch (Exception f){
            System.out.println("Exception triggered in getNumberofColumns(), 2. try block: " + f);
        } finally {
            closeDB(stmt);
        } return numberOfColumns;
    }

    public static boolean inDatabase(String table, String identifyingAttribute, String identifyingValue){
        String query = "SELECT " + identifyingAttribute +" FROM " + table + " WHERE " + identifyingAttribute+ " = '" + identifyingValue+ "';";
        Statement stmt = null;
        ResultSet result = null;
        String queryResult = null;

        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
            queryResult = getResult(result);
        } catch (SQLException e) {
            System.out.println("SQLExeption triggered in inDatabase(), 1. try block: " + e);
        } finally {
            closeDB(stmt);
        }

        if (queryResult != null && queryResult.equalsIgnoreCase(identifyingValue)){
            //System.out.println("'" + identifyingAttribute + "' = '" + identifyingValue + "' exists in table '" + table + "'");
            return true;
        } else {
            //System.out.println("'" + identifyingAttribute + "' = '" + identifyingValue + "' does not exist in table '" + table + "'");
            return false;
        }
    }

    private static String getResult(ResultSet result) {
        //System.out.println("getResults()");
        String returnString = null;
        try {
            while (result.next()) {
                returnString = result.getString(1);
                //System.out.println(returnString);
            }
        } catch (SQLException e) {
            System.out.println("SQLExeption triggered in getResults(): " + e);
        }
        return returnString;
    }




    public static boolean createUser(UserModel user) {
        System.out.println("createUser()");
        //String UserTable = "email, domain, username, passwordHash, firstName, lastName, phone";
        String query = "INSERT INTO User VALUES ('" + user.getEmail() + "', '" +
                user.getDomain() + "', '" +
                user.getUsername() + "', '" +
                user.getPassword() + "', '" +
                user.getFirstName() + "', '" +
                user.getLastName() + "', '" +
                user.getPhone() + "');";
        System.out.println("query: " + query);
        Statement stmt = null;
        System.out.println("\nChecking if user in db: ");
        try {
            if (inDatabase("User", "email", user.getEmail())) {
                System.out.println("User '" + user.getEmail() + "' already exists in database");
                return false;
            } else {
                try {
                    stmt = conn.createStatement();
                    stmt.executeUpdate(query);
                } catch (SQLException f) {
                    System.out.println("SQLException triggered in createUser(): " + f);
                } finally {
                    System.out.println("User '" + user.getEmail() + "' successfully created in database. Creating private calendar now...");
                    Calendar cal = new Calendar("Min kalender", new ArrayList<UserModel>(){{add(user);}});
                    cal.setDescription(user.getFullName());
                    createGroup(new Hashtable<String, Calendar>(){{put("reply", cal); put("private", new Calendar(-1));}});
                    closeDB(stmt);
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Exception triggered in createUser(): " + e);
        } return false;
    }
        /*
        } catch (NullPointerException e){
            // THIS EXCEPTION IS INTENTIONAL AND NECESSARY
            // TRIGGERING IT COMFIRMES THERE ALREADY IS A USER WITH THAT EMAIL
            // System.out.println("NullPointerException triggered in createUser()");
            */


    public static boolean updateUser(UserModel user){
        String query = "UPDATE User SET passwordHash = '" + user.getPassword() + "', " +
                                                      "firstName = '" + user.getFirstName() + "', " +
                                                      "lastName = '" + user.getLastName() + "', " +
                                                      "phone = '" + user.getPhone() + "' WHERE " +
                                                        "email = '" + user.getEmail() + "';";
        System.out.println("query: " + query);
        Statement stmt = null;
        System.out.println("\nCHECKING IF USER ALREADY EXISTS IN DATABASE: ");

        if (inDatabase("User",  "email",  user.getEmail())) {
            try {
                stmt = conn.createStatement();
                stmt.executeUpdate(query);
            } catch (SQLException f) {
                System.out.println("SQLException triggered in updateUser(): " + f);
            } finally {
                System.out.println("User '" + user.getEmail() + "' successfully created in database");
                closeDB(stmt);
                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean updateRowField(String table, String where, String set){
        String query = "UPDATE "+table+" SET "+set+" WHERE " + where+";";
        System.out.println("query: " + query);
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException f) {
            System.out.println("SQLException triggered in updateUser(): " + f);
        } finally {
            closeDB(stmt);
        }
        return false;
    }



    public static UserModel getUser(String mail) {
        String query = "SELECT * FROM User WHERE email = '" + mail + "';";
        Statement stmt = null;
        ResultSet result = null;
        String email = null, passwordHash = null, username = null, domain = null, firstName = null, lastName = null, phone = null;

        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("SQLExeption triggered in getUsername(): " + e);
        }
        try {
            if (result.next()) {
                email = result.getString("email");
                passwordHash = result.getString("passwordHash");
                username = result.getString("username");
                domain = result.getString("domain");
                firstName = result.getString("firstName");
                lastName = result.getString("lastName");
                phone = result.getString("phone");
                /*System.out.println("\nFROM DATABASE: ");
                System.out.println("email: + '" + email + "'");
                System.out.println("passwordHash: '" + passwordHash + "'");
                System.out.println("username: '" + username + "'");
                System.out.println("domain: '" + domain + "'");
                System.out.println("firstName: '" + firstName + "'");
                System.out.println("lastName: '" + lastName + "'");
                System.out.println("phone: '" + phone + "'\n");*/

            } else {
                throw new NullPointerException("User "+ mail + " has no entry in table = 'User");
            }
        } catch (SQLException e){
            System.out.println("SQLExeption triggered in getUsername(), 2. try block: " + e);
        }
        finally {
            closeDB(stmt);
        }

        return new UserModel(username, passwordHash, domain, firstName, lastName, phone);

    }




    public static ArrayList<String> getUsersInGroupCalendar(int calendarId) {
        String query = "SELECT User_email FROM User_has_Calendar WHERE Calendar_calendarid = " + calendarId+ ";";
        ArrayList<String> emailsInCalendar = new ArrayList<String>();
        Statement stmt = null;
        ResultSet result = null;
        String email = null;

        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("SQLExeption triggered in getUsersInGroupCalendar(): " + e);
        }
        try {
            while (result.next()) {
                //emailsInCalendar.add(result.getString("User_email"));
                emailsInCalendar.add(result.getString(1));
            }
        } catch (SQLException e){
            System.out.println("SQLExeption triggered in getUsersInGroupCalendar(), 2. try block: " + e);
        }
        finally {
            closeDB(stmt);
        }

        return emailsInCalendar;

    }


    private String[] printResults(ResultSet result){
        System.out.println("printResults()");
        String[] name = new String[2];

        try {
            while (result.next()) {
                name[0] = result.getString(1);
                name[1] = result.getString(2);
                System.out.println(name[0]);
                System.out.println(name[1]);
            }
        } catch (SQLException e){
            System.out.println("SQLExeption triggered in printResults(): " + e);
        }
        return name;
    }

    public static void closeDB(Statement stmt){
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException ef){
            System.out.println("SQLException triggered in closeDB(), in last try block");
        }
    }




}
