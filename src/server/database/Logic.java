package server.database;

import calendar.Appointment;
import calendar.Group;
import calendar.UserModel;
import network.Query;
import com.sun.org.apache.regexp.internal.RESyntaxException;
//import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                    System.out.println("row[" + i + "]: " + row[i]);
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

    public static ArrayList<List<String>> getAllRows(String table) {
        return getAllRowsWhere(table, null);
    }

    public static ArrayList<List<String>> getAllRowsWhere(String table, String where){
        String whereText = where != null ? " WHERE "+where : "";
        String getRow = "SELECT * FROM " + table + whereText + ";";
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
                    System.out.println(table + "[" + count + "][" + i + "]: " + result.getString(i + 1));
                } count ++;
                System.out.println("");
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
                System.out.println("\nUSER\nusername: " + result.getString("username") + "\npasswordHash: " +
                        result.getString("passwordHash") + "\ndomain: " +
                        result.getString("domain") + "\nfirstName: " +
                        result.getString("firstName") + "\nlastName: " +
                        result.getString("lastName") + "\nphone: " +
                        result.getString("phone"));
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
                System.out.println("numberOfRows: " + numberOfRows);
            } else throw new NullPointerException(table + " has no columns");
        } catch (SQLException e) {
            System.out.println("SQLExeption triggered in getNumberOfRows(), 2. try block: " + e);
        } finally {
            closeDB(stmt);
        } return numberOfRows;
    }


    public static int getLastGroupIdUsed(){
        String query = "SELECT Calendar_calendarid FROM GroupCalendar ORDER BY Calendar_calendarid DESC LIMIT 1;";
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

    public static boolean createAppointment(Appointment app){
        //String query = "INSERT INTO Appointment (title, description, location, starttime, endtime, repeatEndDate, repeat, Calendar_calendarid, Room_roomid) VALUES ";
        // title	description	location	starttime	endtime	repeatEndDate	repeat	Calendar_calendarid	Room_roomid
        String query = "INSERT INTO `nixo_fp`.`Appointment` (`appointmentid`, `title`, `description`, `location`, `starttime`, `endtime`, `repeatEndDate`, `repeat`, `Calendar_calendarid`, `Room_roomid`) VALUES ";
        String location = app.getLocation() != null && app.getLocation().length() > 0 ? ( "'" + app.getLocation() + "'" ) : "NULL" ;
        String description = app.getPurpose() != null && app.getPurpose().length() > 0 ? ("'" + app.getPurpose() + "'") : "NULL";
        String repeatEndDate = app.getEndRepeatDate() != null ? ("'" + app.getEndRepeatDate() + "'" ) : "NULL";
        String repeat = app.getRepeatEvery() > 0 ? String.valueOf(app.getRepeatEvery()) : "NULL";
        String roomId = app.getRoom() != null ? String.valueOf(app.getRoom().getId()) : "NULL";
        //System.out.println("String location = '" + location + "'");
        //System.out.println("int roomId = " + roomId);
        Statement stmt = null;
        //app.getStartDate();
        try {
            stmt = conn.createStatement();
            /*query += String.format("('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');",
                    app.getTitle(), app.getPurpose(), app.getRoom(), app.getStartDate(),
                    app.getEndDate(), app.getEndRepeatDate(), app.getRepeatEvery(), app.getCal().getID(), roomId);
                    */
            //String q = "INSERT INTO Appointment (appointmentid, title, description, location, starttime, endtime, repeatEndDate, repeat, Calendar_calendarid, Room_roomid) VALUES (NULL, 'testmoete', 'for aa teste vel', 'IT bygget', '2015-04-04 10:00:00', '2015-04-04 16:00:00', NULL, NULL, '5', '0');";
            //String q2 = "VALUES ('null', 'Testmøte3', 'blablabla', 'ITbygg', '2015-04-04 10:00:00', '2015-04-04 16:00:00', 'null', 'null', '6', '13');";
            //String query3 = "(`appointmentid`, `title`, `description`, `location`, `starttime`, `endtime`, `repeatEndDate`, `repeat`, `Calendar_calendarid`, `Room_roomid`) VALUES ";
            String query2 = "(NULL, '"+app.getTitle()+"', "+description+", "+location+", '"+app.getStartDate()+"', '"+app.getEndDate()+"', "+repeatEndDate+", "+repeat+", "+app.getCal().getID()+", "+roomId+");";
            System.out.println("QUERY: " + query + query2);
            //System.out.println("QUERY: " + query);
            stmt.executeUpdate(query + query2);
            System.out.println("Appointment [Title='" + app.getTitle() + "'] successfully created in database");
        } catch (SQLException e) {
            System.out.println("SQLException triggered in createAppointment(): " + e);
            return false;
        } finally {
            closeDB(stmt);
        }
        return true;
    }


    public static boolean createGroup(Group group){
        // Dette blir gjort når Group objektet instansieres, og ikke i databasen:
        //int groupId = getLastGroupIdUsed() + 1;
        int groupId = group.getId();
        String query = "INSERT INTO GroupCalendar VALUES (" + groupId + ", ";
        ResultSet result = null;
        Statement stmt = null;
        for (UserModel user : group.getMembers()){
            try {
                stmt = conn.createStatement();
                System.out.println("QUERY: " + query + "'" + user.getEmail() + "');");
                stmt.executeUpdate(query + "'" + user.getEmail() + "');");
                System.out.println(user.getEmail() + " was successfully added to GroupCalendar with ID = " + groupId);
            } catch (SQLException e) {
                System.out.println("SQLExeption triggered in createGroup(): " + e);
                System.out.println("This happend during inserting user '" + user.getEmail() + "' into GroupCalendar");
                return false;
            } catch (Exception f) {
                System.out.println("Exception triggered in createGroup():  " + f);
                return false;
            }
        } closeDB(stmt);
        System.out.println("Group '" + group.getName() + "' with id = " + groupId + " successfully created in database");
        return true;
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
                System.out.println("numberOfColumns: " + numberOfColumns);
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
            System.out.println("'" + identifyingAttribute + "' = '" + identifyingValue + "' already exists in table '" + table + "'");
            return true;
        } else {
            System.out.println("'" + identifyingAttribute + "' = '" + identifyingValue + "' does not exist in table '" + table + "'");
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
                    System.out.println("User '" + user.getEmail() + "' successfully created in database");
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
                System.out.println("\nFROM DATABASE: ");
                System.out.println("email: + '" + email + "'");
                System.out.println("passwordHash: '" + passwordHash + "'");
                System.out.println("username: '" + username + "'");
                System.out.println("domain: '" + domain + "'");
                System.out.println("firstName: '" + firstName + "'");
                System.out.println("lastName: '" + lastName + "'");
                System.out.println("phone: '" + phone + "'\n");

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
