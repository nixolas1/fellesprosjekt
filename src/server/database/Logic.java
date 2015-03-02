package server.database;

import calendar.UserModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Sondre on 26.02.15.
 */
public class Logic {

    static Connection conn;

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
            try {
                if (stmt != null) {
                    stmt.close();
                }
            }   catch (SQLException f){
                System.out.println("SQLExeption triggered in getUsername(), 3. try block: " + f);
            }
        } return row;
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
        }
        try {
            if (result.next()) {
                numberOfColumns = result.getInt(1);
                System.out.println("numberOfColumns: " + numberOfColumns);
            } else throw new NullPointerException(table + " has no columns");
        } catch (SQLException e) {
            System.out.println("SQLExeption triggered in getNumberOfColumns(), 2. try block: " + e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException f) {
                System.out.println("SQLExeption triggered in getUsername(), 3. try block: " + f);
            }
        } return numberOfColumns;
    }

    public boolean inDatabase(String whatToFind, String table, String attribute){
        String query = "SELECT * FROM " + table + " WHERE " + whatToFind + " = '" + attribute+ "';";
        Statement stmt = null;
        ResultSet result = null;
        String queryResult = null;

        try {
            stmt = this.conn.createStatement();
            result = stmt.executeQuery(query);
            System.out.println("QUERY RESULT: " + result);
            queryResult = getResult(result);
            System.out.println("queryResult: " + queryResult);
        } catch (SQLException e) {
            System.out.println("SQLExeption triggered in inDatabase(): " + e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            }   catch (SQLException f){
                System.out.println("SQLExeption triggered in inDatabase(), 2. try block: " + f);
            }
        }

        if (queryResult == whatToFind){
            System.out.println("TRUE");
            return true;
        } else {
            System.out.println("FALSE");
            return false;
        }
    }

    private String getResult(ResultSet result) {
        System.out.println("printResults()");
        String returnString = null;
        try {
            while (result.next()) {
                returnString = result.getString(1);
                System.out.println(returnString);
            }
        } catch (SQLException e) {
            System.out.println("SQLExeption triggered in getResults(): " + e);
        }
        return returnString;
    }


    public static void createUser(UserModel user){
        System.out.println("createUser()");
        //String UserTable = "email, domain, username, passwordHash, firstName, lastName, phone";
        String query = "INSERT INTO User VALUES (" + user.getEmail() + ", " +
                                                     user.getDomain() + ", " +
                                                     user.getUsername() + ", " +
                                                     user.getPassword() + ", " +
                                                     user.getFirstName() + ", " +
                                                     user.getLastName() + ", " +
                                                     user.getPhone() + ");";
        System.out.println("query: " + query);
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e){
            System.out.println("SQLExecption triggered in createUser(): " + e);
        } catch (Exception e){
            System.out.println("Exeption triggered in createUser(): " + e);
        } finally {
            System.out.println("User successfully created in database");
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
                System.out.println(email);
                System.out.println(passwordHash);
                System.out.println(username);
                System.out.println(domain);
                System.out.println(firstName);
                System.out.println(lastName);
                System.out.println(phone + "\n");

            } else {
                throw new NullPointerException("User has no entry");
            }
        } catch (SQLException e){
            System.out.println("SQLExeption triggered in getUsername(), 2. try block: " + e);
        }
        finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            }   catch (SQLException f){
                System.out.println("SQLExeption triggered in getUsername(), 3. try block: " + f);
            }
        }

        return new UserModel(email, passwordHash, username, domain, firstName, lastName, phone);

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

    // HVIS DU SKAL INSERTE ELLER UPDATE
    // stmt.executeUpdate(createString);
    //System.out.println("Command executed:");
    //System.out.println(ex);





}
