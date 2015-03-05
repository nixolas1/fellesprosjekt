package network;

import calendar.UserModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public class Query implements Serializable{
    public String function="";
    public Hashtable data;

    public Query(String function){
        this.function = function;
        this.data = new Hashtable<String, String>(){{}};
    }

    public Query(String function, final String data){
        this.function = function;
        this.data = new Hashtable<String, String>(){{put("reply", data);}};
    }

    public Query(String function, final ArrayList<UserModel> data) {
        this.function = function;
        this.data = new Hashtable<String, ArrayList<UserModel>>(){{put("reply",data);}};
    }

    public Query(String function, final UserModel data) {
        this.function = function;
        this.data = new Hashtable<String, UserModel>(){{put("reply",data);}};
    }

    public Query(String function, final Boolean valid){
        this.function = function;
        this.data = new Hashtable<String, Boolean>(){{
            put("reply", valid);
        }};
    }

    public Query(String function, final Boolean valid, final Boolean data){
        this.function = function;
        this.data = new Hashtable<String, Boolean>(){{
            put("reply", valid);
            put("data", data);
        }};
    }

    public Query(String function, Hashtable<String, String> data){
        this.function = function;
        this.data = data;
    }
}
