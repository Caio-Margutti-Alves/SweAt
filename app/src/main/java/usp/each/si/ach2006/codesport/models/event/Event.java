package usp.each.si.ach2006.codesport.models.event;

import com.google.android.gms.maps.model.Marker;

import java.util.Date;

import usp.each.si.ach2006.codesport.R;
import usp.each.si.ach2006.codesport.codeUtils.Util;
import usp.each.si.ach2006.codesport.models.user.User;

/**
 * Created by caioa_000 on 16/11/2015.
 */
public class Event {

    private String date;
    private User owner;
    private Marker marker;
    private String description;

    public Event() {
    }

    public Event(String date, User owner, Marker marker) {
        this.date = date;
        this.marker = marker;
        this.owner = owner;
    }

    public Event(String date, User owner) {
        this.date = date;
        this.owner = owner;
    }

    public String getDate() {
        return date;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker sport) {
        this.marker = sport;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
