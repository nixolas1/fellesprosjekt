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

}