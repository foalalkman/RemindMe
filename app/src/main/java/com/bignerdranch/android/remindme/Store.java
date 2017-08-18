package com.bignerdranch.android.remindme;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by annika on 2017-08-14.
 */

public class Store implements Parcelable {

    private ArrayList<Reminder> reminders;
    public static final int MAX_DISTANCE = 75;

    public Store() {
        reminders = new ArrayList<>();
        testReminders();
    }

    private Store(Parcel in) {
        in.readTypedList(reminders, null);
    }

    public Reminder add(Reminder r) {
        if (!reminders.contains(r)) { // ers'tts den ej?
            reminders.add(r);
            return r;

        } else {
            return null;
        }
    }

    public ArrayList<Reminder> getReminders() {
        return reminders;
    }

    public Reminder getIndex(int i) {
        return reminders.get(i);
    }

    public int isNear(Location location) {

        for (Reminder reminder : reminders) {
            if (reminder.isNear(location)) {
                return reminders.indexOf(reminder);
            }
        }

        return -1;
    }

    public boolean isEmpty() {
        return reminders.isEmpty();
    }

    public void remove(int index) {
        reminders.remove(index);
    }

    private void testReminders() {
        Location l1 = new Location("");
        l1.setLongitude(59.345615);
        l1.setLatitude(18.111754);
        reminders.add(new Reminder(l1, "Diska", "Öregrundsgatan 11", MAX_DISTANCE));

        Location l2 = new Location("");
        l2.setLongitude(59.344616);
        l2.setLatitude(18.108195);
        reminders.add(new Reminder(l2, "Handla ost", "Ica Värtan", MAX_DISTANCE));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(reminders);
    }

    public final Parcelable.Creator<Store> CREATOR = new Parcelable.Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel source) {
            return new Store(source);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };
}
