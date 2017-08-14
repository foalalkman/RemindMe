package com.bignerdranch.android.remindme;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

/**
 * Created by annika on 2017-08-09.
 */

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSION_ACCESS_FINE_LOCATION = 1;

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationPermission();
    }

    private void locationPermission() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
//                showExplanation("Permission Needed", "Rationale", android.Manifest.permission.ACCESS_FINE_LOCATION);

            } else {
                // No explanation needed, we can request the permission.
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case REQUEST_PERMISSION_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, AppActivity.class);
                    startActivity(intent);

                } else {

                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;

            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}