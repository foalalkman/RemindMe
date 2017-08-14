package com.bignerdranch.android.remindme;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class AppActivity extends AppCompatActivity implements NewReminderFragment.ReminderCreator {

    public static final String LOCATION_UPDATE = "location update";
    public static final String LOCATION = "location";
    private BroadcastReceiver broadcastReceiver;

    private int status;
    private Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_view);

        Intent service = new Intent(this, CurrentLocationService.class);
        startService(service);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        FrameLayout fragmentFrame = (FrameLayout) findViewById(R.id.content_fragment);

        store = new Store();
    }

    // registrera broadcastre.
    @Override
    public void onResume() {
        super.onResume();

        if (broadcastReceiver == null) {
            broadcastReceiver = new LocationBroadcastReceiver();
        }

        registerReceiver(broadcastReceiver, new IntentFilter(LOCATION_UPDATE));
    }

    private void checkGooglePlayServicesAvailability() {
        status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
                GooglePlayServicesUtil.getErrorDialog(status, AppActivity.this,
                        100).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.new_reminder) {

            Fragment fragment = new NewReminderFragment();

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.content_fragment, fragment);
            transaction.commit();
            return true;
        }

        if (id == R.id.list_reminders) {
            Fragment fragment = new MyListFragment();

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.content_fragment, fragment);
            transaction.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    // clean up
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public void createReminder(Location l, String s) {
        Reminder newReminder = new Reminder(l, s, 100);
        store.add(newReminder);
    }

    public class LocationBroadcastReceiver extends BroadcastReceiver {
        @Override // when the receiver receives the intent
        public void onReceive(Context context, Intent intent) {

            Location location = intent.getParcelableExtra(LOCATION);

            if (store.isNear(location)) {
                Toast.makeText(AppActivity.this, "Near!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AppActivity.this, "Not near!", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
