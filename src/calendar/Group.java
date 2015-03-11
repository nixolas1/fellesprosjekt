package calendar;

import network.Query;
import network.ThreadClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by nixo on 3/4/15.
 */
public class Group implements Serializable {
    String name;
    int id;
    ArrayList<UserModel> members = new ArrayList<UserModel>();


    public Group(int id, String name, ArrayList<UserModel> members){
        this.name = name;
        this.id = id;
        this.members = members;
    }

    public ArrayList<UserModel> getMembers(){
        return this.members;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public static ArrayList<Group> getAllGroups() {
        ThreadClient socket = new ThreadClient();
        Query reply = socket.send(new Query("getAllGroups",new ArrayList<Group>()));
        System.out.println(reply.function);
        Hashtable<String, ArrayList<Group>> response = reply.data;
        return response.get("reply");
    }

    public String displayInfo() {
        return name + " ["+id+"]";
    }

}
