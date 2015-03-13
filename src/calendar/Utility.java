package calendar;

import java.io.Serializable;

/**
 * Created by nixo on 3/4/15.
 */
public class Utility implements Serializable {
    int id;
    String name;
    public Utility(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
}
