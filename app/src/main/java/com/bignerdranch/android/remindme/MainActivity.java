package com.bignerdranch.android.remindme;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by annika on 2017-08-09.
 */

/**
 * MainActivity asks the user for permission to access the device's location.
 * If permission is granted, it starts the AppActivity, otherwise nothing
 * will happen.
 */
public class MainActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSION_ACCESS_FINE_LOCATION = 1;

    /**
     * If API level is 23 or higher, prompt the user for permission before
     * launching the app.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissionStatus();
    }

    /**
     * If API level >= 23 location permission is necessary.
     */
    private void checkPermissionStatus() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            locationPermission();
        } else {
            launchAppActivity();
            finish();
        }
    }

    /**
     * Launches the app.
     */
    private void launchAppActivity() {
        Intent intent = new Intent(MainActivity.this, AppActivity.class);
        startActivity(intent);
    }

    /**
     * Asks for permission
     */
    private void locationPermission() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION },
                        REQUEST_PERMISSION_ACCESS_FINE_LOCATION);
                //

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION },
                        REQUEST_PERMISSION_ACCESS_FINE_LOCATION);
            }

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION },
                    REQUEST_PERMISSION_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handes the permission result.
     * @param requestCode who sent this.
     * @param permissions permissions being asked.
     * @param grantResults the results.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {

            case REQUEST_PERMISSION_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    launchAppActivity();
                    finish();
                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(MainActivity.this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}