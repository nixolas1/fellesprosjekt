package server;

/**
 * Created by Sondre on 25.02.15.
 */

import java.sql.*;
import java.util.Properties;


public class ConnectDB {

    /*DriverManager.getConnection();*/

    private String userName, password, serverName, dbName, dbms;
    private Integer portNumber;
    Connection conn;

    public ConnectDB (){
        System.out.println("ConnectDB()");
        this.userName = "nixo_fp";
        this.password = "kapteinsabeltann";
        this.serverName = "vsop.online.ntnu.no";
        this.dbms = "mysql";
        this.portNumber = 3306  ;

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
        String query = "SELECT " + whatToFind + " FROM " + table + " WHERE " + attribute + " = '" + whatToFind + "';";
        Statement stmt = null;
        ResultSet result = null;

        try {
            stmt = this.conn.createStatement();
            result = stmt.executeQuery(query);
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

        if (getResult(result) == whatToFind){
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



    public void getName(String userName){
        String query = "SELECT firstName, lastName FROM User WHERE userName = '" + userName + "';";
        Statement stmt = null;


        try {
            stmt = this.conn.createStatement();
            ResultSet result = stmt.executeQuery(query);
            printResults(result);
        } catch (SQLException e) {
            System.out.println("SQLExeption triggered in getUsername(): " + e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            }   catch (SQLException f){
                System.out.println("SQLExeption triggered in getUsername(), 2. try block: " + f);
            }

        }
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