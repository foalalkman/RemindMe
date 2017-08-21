package com.bignerdranch.android.remindme;

import android.content.Context;
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

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            locationPermission();
        } else {
            Intent intent = new Intent(MainActivity.this, AppActivity.class);
            startActivity(intent);
        }
    }

    private void locationPermission() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case REQUEST_PERMISSION_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(this, AppActivity.class);
                    startActivity(intent);
                }
            }
        }
    }
}