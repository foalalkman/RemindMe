package com.bignerdranch.android.remindme;

import android.app.ActivityManager;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

public class AppActivity extends AppCompatActivity
        implements NewReminderFragment.ReminderCreator, MyListFragment.ReceiverController {

    public static final String LOCATION_UPDATE = "location update";
    public static final String LOCATION = "location";
    public static final String KEY_STORE = "store";
    public static final String KEY_REMINDERS = "reminders";
    public static final String KEY_CURRENT_FRAGMENT = "current fragment";

    private static final int NOTIFICATION_ID = 321123;

    Intent locationService;

    private BroadcastReceiver broadcastReceiver;
    private Fragment currentFragment;
    private FragmentManager fragmentManager;
    private Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_view);

        locationService = new Intent(this, CurrentLocationService.class);

        fragmentManager = getSupportFragmentManager();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        FrameLayout fragmentFrame = (FrameLayout) findViewById(R.id.content_fragment);
        initializeState(savedInstanceState);

    }

    private void initializeState(Bundle savedInstanceState) {
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

            if (serviceHasStopped(CurrentLocationService.class)) {
                startService(locationService);
            }
        }
    }

    private boolean serviceHasStopped(Class<?> serviceClass) {
        ActivityManager activityMaager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo serviceInfo : activityMaager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(serviceInfo.service.getClassName())) {
                Toast.makeText(AppActivity.this, "Service is running", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        Toast.makeText(AppActivity.this, "Service has stopped", Toast.LENGTH_SHORT).show();
        return true;
    }

    private void removeReminder(int index) {
        if (store != null) {
            store.remove(index);
        }
    }

//    private void locationServiceUpdate() {
//        if (store.isEmpty()) {
//            stopService(locationService);
//            Toast.makeText(AppActivity.this, "Store is empty", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void createNotification(String message) {

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(AppActivity.this)
                        .setSmallIcon(R.drawable.notification_yellow)
                        .setAutoCancel(true)
                        .setTicker("Ticker")
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle("Reminder")
                        .setContentText(message)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(Notification.PRIORITY_MAX)
                ;


        Intent resultIntent = new Intent(AppActivity.this, AppActivity.class);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(AppActivity.this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    @Override
    public void datasetChanged() {
        if (store.isEmpty()) {
            stopService(locationService);
            Toast.makeText(AppActivity.this, "Store is empty", Toast.LENGTH_SHORT).show();
        }
    }

    public class LocationBroadcastReceiver extends BroadcastReceiver {
        @Override // when the receiver receives the intent
        public void onReceive(Context context, Intent intent) {

            Location location = intent.getParcelableExtra(LOCATION);
            int index = store.isNear(location);

            if (index >= 0) {
                Reminder reminder = store.getIndex(index);
                createNotification(reminder.getText());
                removeReminder(index);
                datasetChanged();

            } else {
                Toast.makeText(AppActivity.this, "Not near!", Toast.LENGTH_SHORT).show();
            }

        }



    }
}
