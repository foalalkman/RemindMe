package com.bignerdranch.android.remindme;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
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
    public static final String KEY_STORE = "store";
    public static final String KEY_REMINDERS = "reminders";
    public static final String KEY_CURRENT_FRAGMENT = "current fragment";

    private static final int NOTIFICATION_ID = 321123;

    private BroadcastReceiver broadcastReceiver;
    private Fragment currentFragment;
    private FragmentManager fragmentManager;

    private int status;
    private Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_view);

        Intent service = new Intent(this, CurrentLocationService.class);
        startService(service);

        fragmentManager = getSupportFragmentManager();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        FrameLayout fragmentFrame = (FrameLayout) findViewById(R.id.content_fragment);
        initialixeState(savedInstanceState);

    }

    private void initialixeState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            store = savedInstanceState.getParcelable(KEY_STORE);
            currentFragment =  getSupportFragmentManager().getFragment(savedInstanceState, KEY_CURRENT_FRAGMENT);
            if (currentFragment != null) {
                launchListFragment();
            }

        } else {
            store = new Store();
            currentFragment = new MyListFragment();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putParcelable(KEY_STORE, store);
        getSupportFragmentManager().putFragment(savedInstanceState, KEY_CURRENT_FRAGMENT, currentFragment);

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

            launchNewReminderFragment();
            return true;
        }

        if (id == R.id.list_reminders) {
            launchListFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void launchNewReminderFragment() {
        Fragment fragment = new NewReminderFragment();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_fragment, fragment);
        transaction.commit();

        currentFragment = fragment;
    }

    private void launchListFragment() {
        Fragment fragment = new MyListFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_REMINDERS, store.getReminders());
        fragment.setArguments(bundle);
        //
        transaction.replace(R.id.content_fragment, fragment);
        transaction.commit();
        currentFragment = fragment;
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
    public void createReminder(Location l, String s, String n) {

        Reminder newReminder = new Reminder(l, s, n, Store.MAX_DISTANCE);
        if (store != null) {
            store.add(newReminder);
        }
    }

    public class LocationBroadcastReceiver extends BroadcastReceiver {
        @Override // when the receiver receives the intent
        public void onReceive(Context context, Intent intent) {

            Location location = intent.getParcelableExtra(LOCATION);

            int index = store.isNear(location);

            if (index >= 0) {
                Reminder reminder = store.getIndex(index);
//                Toast.makeText(AppActivity.this, "Near " + reminder.getLocationName(), Toast.LENGTH_SHORT).show();
                createNotification();
            } else {
                Toast.makeText(AppActivity.this, "Not near!", Toast.LENGTH_SHORT).show();
            }

        }

        private void createNotification() {
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(AppActivity.this)
                        .setSmallIcon(R.drawable.remindme_icon)
                        .setAutoCancel(true)
                        .setTicker("Tkcner")
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle("My notification")
                        .setContentText("Hello world1")
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(Notification.PRIORITY_MAX);


            Intent resultIntent = new Intent(AppActivity.this, AppActivity.class);

            PendingIntent pendingIntent =
                    PendingIntent.getActivity(AppActivity.this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(pendingIntent);

            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(NOTIFICATION_ID, notificationBuilder.build());


//            TaskStackBuilder stackBuilder = TaskStackBuilder.create(AppActivity.this);
//            stackBuilder.addParentStack(AppActivity.class);
//            stackBuilder.addNextIntent(resultIntent);
//
//            PendingIntent resultPendingIntent =
//                    stackBuilder.getPendingIntent(
//                            0,
//                            PendingIntent.FLAG_CANCEL_CURRENT
//                    );
//
//
//            notificationBuilder.setContentIntent(resultPendingIntent);
//            NotificationManager notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());


        }
    }
}
