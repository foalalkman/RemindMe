package com.bignerdranch.android.remindme;

import android.location.Location;

import com.google.android.gms.location.Geofence;

/**
 * Created by annika on 2017-08-14.
 */

public class Reminder {

    private Location location;
    private String text;
    private int radius;

    public Reminder(Location l, String t, int r) {
        location = l;
        text = t;
        radius = r;
    }

    public Location getLocation() {
        return location;
    }

    public String getText() {
        return text;
    }

    public boolean isNear(Location other) {
        if (this.location.distanceTo(other) < radius) {
            return true;
        }

        return false;

    }

}
