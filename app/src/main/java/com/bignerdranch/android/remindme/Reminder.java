package com.bignerdranch.android.remindme;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by annika on 2017-08-14.
 */

public class Reminder implements Parcelable {

    private Location location;
    private String locationName;
    private String text;
    private int radius;

    public Reminder(Location l, String t, String ln, int r) {
        location = l;
        locationName = ln;
        text = t;
        radius = r;
    }

    public void setLocation(Location newLocation) {
        location = newLocation;
    }

    public void setLocationName(String newName) {
        locationName = newName;
    }

    public void setText(String newText) {
        text = newText;
    }



    public Location getLocation() {
        return location;
    }

    public String getLocationName() { return locationName; }

    public String getText() {
        return text;
    }

    public boolean isNear(Location other) {
        if (this.location.distanceTo(other) < radius) {
            return true;
        }

        return false;

    }

    /**
     * The private constructor used by the Creator, that assigns the saved values to the
     * new instance.
     * @param in the Parcel holding the old state.
     */
    private Reminder(Parcel in) {
        location = Location.CREATOR.createFromParcel(in);
        radius = in.readInt();
        text = in.readString();
        locationName = in.readString();
    }

    /**
     * Makes it possible to pass more information about the class, in form of a bit mask.
     * @return a bitmask with information, 0 if none.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * This is where the Parcel object is filled with the values that needs to be saved.
     * @param parcel a Parcel object for storing data.
     * @param i describes different ways to write to the parcel.
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        location.writeToParcel(parcel, i);
        parcel.writeInt(radius);
        parcel.writeString(text);
        parcel.writeString(locationName);
    }

    /**
     * For receiving the class loader in the Java Runtime Environment,
     * which can load a class dynamically during runtime. It will use the private constructor
     * that takes a Parcel instance.
     */
    public final Parcelable.Creator<Reminder> CREATOR
            = new Parcelable.Creator<Reminder>() {

        /**
         * When createFromParcel is called by the system, it takes the Parcel with all the
         * information and use the private constructor to make a new instance of Reminder that
         * mirrors the old one.
         * @param source the parcel storing the old state.
         * @return a new Reminder object similar to the old one.
         */
        @Override
        public Reminder createFromParcel(Parcel source) {
            return new Reminder(source);
        }

        /**
         * Allows an array of the class to be parcelled.
         * @param size the size of the array to be created.
         * @return An empty array with the specified length.
         */
        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };

}
