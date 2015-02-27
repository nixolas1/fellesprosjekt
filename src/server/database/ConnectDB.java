package server.database;

/**
 * Created by Sondre on 25.02.15.
 */

import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;


public class ConnectDB {

    /*DriverManager.getConnection();*/

    /*/connectDB.getUser("nicolaat");
        //connectDB.getRow("User", "username", "nicolaat");
        connectDB.getRow("Room", "roomid", "2");
        //String[] asda = {"asdasd", "asdasd"};
        //connectDB.inDatabase("username", "User", "nikolaat");*/

    private String userName, password, serverName, dbName, dbms;
    private Integer portNumber;
    Connection conn;

    public ConnectDB (){
        System.out.println("ConnectDB()");
        this.userName = "nixo_fp";
        this.password = "kapteinsabeltann";
        this.serverName = "vsop.online.ntnu.no";
        this.dbms = "mysql";
        this.portNumber = 3306;
    }



    public Connection connect() {
        System.out.println("connect()");
        Statement stmt = null;
        this.conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);
        try {
            if (this.dbms.equals("mysql")) {
                this.conn = DriverManager.getConnection("jdbc:" + this.dbms + "://" + this.serverName + ":" + this.portNumber + "/", connectionProps);
                System.out.println("Connected to database");
                stmt = this.conn.createStatement();
                stmt.executeUpdate("use nixo_fp;");
                System.out.println("Navigated to database: nixo_fp");
            }
        } catch (SQLException e){
            System.out.println("SQLExeption triggered in connect(): " + e);
        }
        return conn;
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

    private String getResult(ResultSet result){
        System.out.println("printResults()");
        String returnString = null;
        try {
            while (result.next()) {
                returnString = result.getString(1);
                System.out.println(returnString);
            }
        } catch (SQLException e){
            System.out.println("SQLExeption triggered in printResults(): " + e);
        }
        return returnString;
    }


    public String[] getRow(String table, String identifyingAttribute, String identifyingValue){
        String getRow = "SELECT * FROM " + table + " WHERE " + identifyingAttribute + " = '" + identifyingValue + "';";
        Statement stmt = null;
        ResultSet result = null;
        String[] row = new String[getNumberOfColumns(table)];

        try {
            stmt = this.conn.createStatement();
            result = stmt.executeQuery(getRow);
        } catch (SQLException e) {
            System.out.println("SQLException triggered in getRow(): " + e);
        }
            try {
                if(result.next()){
                    for (int i = 0; i < row.length; i++) {
                        row[i] = result.getString(i + 1);
                        System.out.println("row[" + i + "]: " + row[i]);
                    }
                } else throw new NullPointerException(identifyingValue + " has no entry in " + table);
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

    public int getNumberOfColumns(String table) {
        String getNumberOfColumns = "SELECT COUNT(*) totalColumns FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name =  '" + table + "' AND TABLE_SCHEMA=(SELECT DATABASE())";
        ResultSet result = null;
        Statement stmt = null;
        int numberOfColumns = 0;

        try {
            stmt = this.conn.createStatement();
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




    public String[] getUser(String userName) throws NullPointerException{
        String query = "SELECT * FROM User WHERE username = '" + userName + "';";
        String query2 = "SELECT COUNT(*) totalColumns FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = 'User' AND TABLE_SCHEMA=(SELECT DATABASE())";
        String[] name = new String[6];
        Statement stmt = null;
        ResultSet result = null;
        ResultSet result2 = null;
        int numberOfColumns = 0;

        try {
            stmt = this.conn.createStatement();
            result2 = stmt.executeQuery(query2);
            //result = stmt.executeQuery(query);
            //printResults(result);
        } catch (SQLException e) {
            System.out.println("SQLExeption triggered in getUsername(): " + e);
        }
            try {
                if (result2.next()) {
                    //numberOfColumns = result2.getString(1);
                    numberOfColumns = result2.getInt(1);
                    System.out.println("result2:");
                    System.out.println(numberOfColumns);
                    /*name[0] = result.getString(1);
                    name[1] = result.getString(2);
                    name[2] = result.getString(3);
                    name[3] = result.getString(4);
                    name[4] = result.getString(5);
                    name[5] = result.getString(6);
                    System.out.println(name[0]);
                    System.out.println(name[1]);
                    System.out.println(name[2]);
                    System.out.println(name[3]);
                    System.out.println(name[4]);
                    System.out.println(name[5]);*/
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

        return name;


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