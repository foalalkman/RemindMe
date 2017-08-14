package com.bignerdranch.android.remindme;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by annika on 2017-08-14.
 */

public class Store {

    private ArrayList<Reminder> reminders;

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
        l1.setLongitude(11.1);
        l1.setLatitude(11.1);
        reminders.add(new Reminder(l1, "ettan", 100));

        Location l2 = new Location("");
        l2.setLongitude(22.2);
        l2.setLatitude(22.2);
        reminders.add(new Reminder(l2, "tv[an", 100));
    }
}
