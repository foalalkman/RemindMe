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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AppActivity extends AppCompatActivity
        implements NewReminderFragment.ReminderCreator, ServiceControllerFragment.ServiceController {

    private static final String FILENAME = "remindme_data";
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

            String data = loadData();
            if (!data.isEmpty()) {
                Toast.makeText(this, "Not empty", Toast.LENGTH_SHORT).show();
                store.deSerialize(data);
            }

            else {
                Toast.makeText(this, "empty", Toast.LENGTH_SHORT).show();
            }

            currentFragment = new MyListFragment();
        }
    }

    private void saveData() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(store.serialize());
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private String loadData() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            String data = (String) ois.readObject();

            Toast.makeText(this, "Data" + data, Toast.LENGTH_LONG).show();

            ois.close();
            return data;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return "";
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

    // clean up and save data
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }

        saveData();
    }

    @Override
    public void createReminder(Location l, String s, String n) {
        Reminder newReminder = new Reminder(l, s, n, Store.MAX_DISTANCE);
        if (store != null) {
            store.add(newReminder);

            if (serviceHasStopped(CurrentLocationService.class)) {
                Toast.makeText(AppActivity.this, "Service stopped, start it", Toast.LENGTH_SHORT).show();
                startService(locationService);
            }
            if (serviceHasStopped(CurrentLocationService.class)) {
                Toast.makeText(AppActivity.this, "Service still stopped", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AppActivity.this, "Service running", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean serviceHasStopped(Class<?> serviceClass) {
        ActivityManager activityMaager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo serviceInfo : activityMaager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(serviceInfo.service.getClassName())) {
                return false;
            }
        }
        return true;
    }

    private void removeReminder(int index) {
        if (store != null) {
            store.remove(index);
        }
    }

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

        Notification notification = notificationBuilder.mNotification;
        notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;

        Intent resultIntent = new Intent(AppActivity.this, AppActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);


        PendingIntent pendingIntent =
                PendingIntent.getActivity(AppActivity.this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    @Override
    public void serviceControl() {
        if (store.isEmpty()) {
            stopService(locationService);
            Toast.makeText(AppActivity.this, "Store is empty", Toast.LENGTH_SHORT).show();
        } else {

            if (serviceHasStopped(CurrentLocationService.class)) {
                startService(locationService);
            }
        }
    }

    public class LocationBroadcastReceiver extends BroadcastReceiver {

        @Override // when the receiver receives the intent
        public void onReceive(Context context, Intent intent) {

            Location location = intent.getParcelableExtra(LOCATION);
            int index = store.isNear(location);

            if (index >= 0) {
                Toast.makeText(AppActivity.this, "Near!", Toast.LENGTH_SHORT).show();
                Reminder reminder = store.getIndex(index);
                createNotification(reminder.getText());
                removeReminder(index);
                serviceControl();

            } else {
                Toast.makeText(AppActivity.this, "Not near!", Toast.LENGTH_SHORT).show();
            }

        }



    }
}
