package calendar;

import client.*;
import client.Main;
import network.ClientDB;
import network.Query;
import network.ThreadClient;

import java.util.ArrayList;
import java.util.Hashtable;
import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {

    private String username = "";
    private String firstName = "";
    private String lastName = "";
    private String phone = "";
    private String password = "";
    private String domain = "";
    private String email = "";
    private ArrayList<Calendar> calendar = new ArrayList<Calendar>();

    public int getPrivateCalendar() {
        int ret = -1;
        try {
            ArrayList<List<String>> rows = ClientDB.getAllTableRowsWhere("User_has_Calendar",
                    "User_email = '" + this.getEmail() + "' AND isPrivate = 1", Main.socket);
            ret = Integer.parseInt(rows.get(0).get(1));
        }
        catch(IndexOutOfBoundsException e){
            System.out.println(this.getEmail()+" has no private calendar.");
        }
        return ret;

    }

    public UserModel(){};

    public UserModel(String email, String password){
        setEmail(email);
        setPassword(password);
    }

    public UserModel(String username, String password, String domain, String firstName, String lastName, String phone){
        setPassword(password);
        setUsername(username);
        setDomain(domain);
        setFirstName(firstName);
        setLastName(lastName);
        setPhone(phone);
    }
    /*public UserModel(int id){
        String[] userRow = server.database.Logic.getRow("User", "email", Integer.toString(id));
        System.out.println(roomRow);
        new Room(id, "", 1, 420, 1300, new ArrayList<Utility>());
    }*/

    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getPhone() {
        return phone;
    }
    public String getPassword() {
        return password;
    }
    public String getDomain() {
        return domain;
    }

    public void setCalendars(ArrayList<Calendar> cal){this.calendar=cal;}
    public void addCalendar(Calendar cal){this.calendar.add(cal);}

    public void setDomain(String domain) {
        this.domain = domain;
        if (!this.username.equals(""))
            this.email = this.username + "@" + this.domain;
    }

    public void setEmail(String email) {
        this.email = email;
        this.username = email.split("@")[0];
        this.domain = email.split("@")[1];
    }

    public void setUsername(String username) {
        this.username = username;
        if(!this.domain.equals(""))this.email=this.username+"@"+this.domain;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString(){
        return "[Bruker: "+getUsername()+" passord: "+getPassword()+" fornavn: "+getFirstName()+" etternavn: "+
                getLastName()+" telefon: "+getPhone()+" domene: "+getDomain()+" email: "+getEmail() + "]";
    }

    public String displayInfo() {
        return getFirstName() + " " + getLastName() + ", " + getEmail();
    }

    public static UserModel getUserFromServer(final String email){
        try {
            String[] split = email.split("@");
            return getUserFromServer(split[0], split[1]);
        }catch (NullPointerException e){
            return server.database.Logic.getUser(email);
        }
    }

    public String getFullName(){
        return getFirstName()+" "+getLastName();
    }

    public static UserModel getUserFromServer(final String user, final String domain){

        //System.out.println("Getting user: "+user+", "+domain);
        Hashtable<String, String> data = new Hashtable<String, String>(){{
            put("username",user);
            put("domain", domain);
        }};

        Query reply = client.Main.socket.send(new Query("getUser", data));
        Hashtable<String, UserModel> response = reply.data;

        return response.get("reply");
    }
    public static ArrayList<UserModel> getAllUsers() {
        ThreadClient socket = new ThreadClient();
        Query reply = socket.send(new Query("getAllUsers",new ArrayList<UserModel>()));
        //System.out.println(reply.function);
        Hashtable<String, ArrayList<UserModel>> response = reply.data;
        return response.get("reply");
    }

    public static ArrayList<String> convertUserModelsToStringArrayList(ArrayList<UserModel> users) {
        ArrayList<String> converted = new ArrayList<>();
        for (UserModel user : users) {
            converted.add(user.getFirstName() + " " + user.getLastName() + ", " + user.getEmail());
        }
        return converted;
    }


}
