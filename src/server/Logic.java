package server;

import network.Query;
import security.Login;

import java.util.Hashtable;

import static security.Login.validLogin;


public class Logic {

    public static Query process(String request, Hashtable data){
        switch (request){
            case "end": return null;
            case "login": return Login.validLogin(data);
            case "create": return CreateUser.createUser(data);
            case "reset": return ForgottenPass.resetPassword(data);
            case "getUser": return User.getUser(data);
            case "getAllUsers": return User.getAllUsers();
            case "updateSettings": return UserSettings.updateSettings(data);
            case "getAppointments": return AppointmentLogic.getCalendarAppointments(data);
            case "newAppointment": return AppointmentLogic.newAppointment(data);
            //case "getRooms": return RoomLogic.getRooms(data);
            case "getRows": return server.database.Logic.sendAllRows(data);
            case "createGroup": return server.database.Logic.createGroup(data);
            //case "getLastGroupIdUsed": return server.database.Logic.getId();


        }
        return new Query("error");
    }


}
