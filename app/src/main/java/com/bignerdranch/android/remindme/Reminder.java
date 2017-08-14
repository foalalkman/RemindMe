package com.bignerdranch.android.remindme;

import com.google.android.gms.location.Geofence;

/**
 * Created by annika on 2017-08-14.
 */

public class Reminder {

    private Geofence geofence;
    private String text;

    public Reminder(Geofence g, String t) {
        geofence = g;
        text = t;
    }

    public Geofence getGeofence() {
        return geofence;
    }

    public String getText() {
        return text;
    }

}
