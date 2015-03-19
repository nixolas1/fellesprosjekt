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
            case "updateAttending": return AppointmentLogic.updateAttending(data);
            case "updateAppointment": return AppointmentLogic.updateAppointment(data);
            case "getAppointments": return AppointmentLogic.getCalendarAppointments(data);
            case "newAppointment": return AppointmentLogic.newAppointment(data);
            //case "getRooms": return RoomLogic.getRooms(data);
            case "getRows": return server.database.Logic.sendAllRows(data);
            case "updateRow": return server.database.Logic.updateRow(data);
            case "createGroup": return server.database.Logic.createGroup(data);
            case "createRoom": return server.database.Logic.createRoom(data);
            case "getNotifications": return NotificationLogic.getNotifications(data);
            case "roomLogic": return server.RoomLogic.initiateRoomLogic(data);
            //case "getAvailableRooms": return RoomLogic.initiateRoomLogic(data);
            //case "getLastGroupIdUsed": return server.database.Logic.getId();


        }
        return new Query("error");
    }


}
