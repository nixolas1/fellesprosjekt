package calendar;

import java.util.ArrayList;

/**
 * Created by nixo on 3/4/15.
 */
public class Group {
    String name;
    int id;
    ArrayList<UserModel> members = new ArrayList<UserModel>();
    public Group(int id, String name, ArrayList<UserModel> members){
        this.name = name;
        this.id = id;
        this.members = members;
    }
}
