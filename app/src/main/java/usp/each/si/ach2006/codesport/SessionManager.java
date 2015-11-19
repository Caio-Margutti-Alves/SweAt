package usp.each.si.ach2006.codesport;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;

import usp.each.si.ach2006.codesport.models.event.Event;
import usp.each.si.ach2006.codesport.models.user.User;


/**
 * Created by caioa_000 on 16/11/2015.
 */

public class SessionManager extends Application {

    private static User currentUser;
    private static HashMap<String, Event> allEvents;
    private static ArrayList<Event> upcomingEvents;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        SessionManager.currentUser = currentUser;
    }

    public static HashMap<String, Event> getAllEvents() {
        return allEvents;
    }

    public static void setAllEvents(HashMap<String, Event> allEvents) {
        SessionManager.allEvents = allEvents;
    }

    public static ArrayList<Event> getUpcomingEvents() {
        return upcomingEvents;
    }

    public static void setUpcomingEvents(ArrayList<Event> upcomingEvents) {
        SessionManager.upcomingEvents = upcomingEvents;
    }

    public static void dispose(){
        currentUser = null;
        allEvents = null;
        upcomingEvents = null;
    }
}
