package server;

/**
 * Created by nixo on 2/23/15.
 */
public class Logic {

    public static String process(String request){
        switch (request){
            case "end": return null;
            case "login": return "yes";
            case "create": return "yes";
        }
        return "Error: No function could process that query.";
    }

}
