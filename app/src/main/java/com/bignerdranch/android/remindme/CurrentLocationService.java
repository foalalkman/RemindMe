package com.bignerdranch.android.remindme;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.Toast;


/**
 * Created by annika on 2017-08-11.
 */

public class CurrentLocationService extends Service {

    private MyLocationListener locationListener;
    private LocationManager locationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        locationListener = new MyLocationListener();
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent,flags,startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // clean
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            //noinspection MissingPermission
            locationManager.removeUpdates(locationListener);
        }
    }

    private class MyLocationListener implements LocationListener {

        // nu ska vi broadcasta datat vi får genom att lägga det i ett Intent
        @Override
        public void onLocationChanged(Location location) {
            Intent intent = new Intent(AppActivity.LOCATION_UPDATE); // nyckelsträng för att hitta rätt intent
            intent.putExtra(AppActivity.LOCATION, location.getLongitude() + " " + location.getLatitude()); // skapa locaionobject??
            sendBroadcast(intent);
        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle b) {
        }
    }

}
