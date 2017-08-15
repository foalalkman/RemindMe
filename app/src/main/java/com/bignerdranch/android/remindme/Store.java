package com.bignerdranch.android.remindme;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by annika on 2017-08-14.
 */

public class Store {

    private ArrayList<Reminder> reminders;
    public static final int MAX_DISTANCE = 50;

    public Store() {
        reminders = new ArrayList<>();
        testReminders();
    }

    public Reminder add(Reminder r) {
        if (!reminders.contains(r)) { // ers'tts den ej?
            reminders.add(r);
            return r;

        } else {
            return null;
        }
    }

    public boolean isNear(Location location) {

        for (Reminder reminder : reminders) {
            if (reminder.isNear(location)) {
                return true;
            }
        }

        return false;
    }


    private void testReminders() {
        Location l1 = new Location("");
        l1.setLongitude(59.345502);
        l1.setLatitude(18.111529);
        reminders.add(new Reminder(l1, "ettan", MAX_DISTANCE));

        Location l2 = new Location("");
        l2.setLongitude(59.344616);
        l2.setLatitude(18.108195);
        reminders.add(new Reminder(l2, "tv[an", MAX_DISTANCE));
    }
}
