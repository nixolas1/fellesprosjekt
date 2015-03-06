package calendar;

import java.io.Serializable;
import java.util.ArrayList;

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

    public Group() {

    }
}
