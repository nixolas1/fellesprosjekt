package user;

import calendar.UserModel;
import server.database.Logic;

/**
 * Created by Rae on 02.03.15.
 */
public class Controller {


    public static void main(String[] args) {
        UserModel user = server.database.Logic.getUser("sondrejw@stud.ntnu.no");
        user.getEmail();
        user.setEmail();
        user.setFirstName();


    }
}
