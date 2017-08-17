package com.bignerdranch.android.remindme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;

/**
 * Created by annika on 2017-08-09.
 */

public class MyListFragment extends Fragment implements MyRecyclerAdapter.UserInputDelegate {

    ArrayList<Reminder> reminders;
    View view;
    private RecyclerView recyclerView;
    private MyRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String newText = "";
    private MyRecyclerAdapter.ReminderHolder currentReminderHolder;


    @Override
    public void pickNewPlace(MyRecyclerAdapter.ReminderHolder holder) {
        currentReminderHolder = holder;
        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());

        if (status == ConnectionResult.SUCCESS) {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            try {
                startActivityForResult(builder.build(getActivity()), NewReminderFragment.PLACE_PICKER_REQUEST);

            } catch (GooglePlayServicesRepairableException e) {

                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NewReminderFragment.PLACE_PICKER_REQUEST) {

            if (resultCode == Activity.RESULT_OK) {

                Place place = PlacePicker.getPlace(data, getActivity());
                showConfirmationDialog(place);
            }
        }
    }

    private void showConfirmationDialog(Place place) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.confirmation_dialog_new_place);

        builder.setCancelable(false);

        final String name = (String)place.getName();
        final Location location = new Location(place.getName()+"");
        location.setLatitude(place.getLatLng().latitude);
        location.setLongitude(place.getLatLng().longitude);

        builder.setMessage("Change location to " + name + "?");


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                currentReminderHolder.reminder.setLocationName(name);
                currentReminderHolder.reminder.setLocation(location);

                // updates
                currentReminderHolder = null;
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                currentReminderHolder = null;
            }
        });

        builder.show();
    }


    @Override
    public void editText(MyRecyclerAdapter.ReminderHolder holder) {

        final MyRecyclerAdapter.ReminderHolder currentHolder = holder;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final EditText inputField = new EditText(getContext());
        inputField.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setTitle(R.string.input_dialog_new_title);
        builder.setView(inputField);
        builder.setCancelable(false); // hmmm

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                newText = inputField.getText().toString();
                currentHolder.reminder.setText(newText);
                // update
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        reminders = bundle.getParcelableArrayList(AppActivity.KEY_REMINDERS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.list_fragment_view, container, false);
        initializeRecyclerView(view);

        return view;
    }

    private void initializeRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyRecyclerAdapter(reminders, getContext(), (MyRecyclerAdapter.UserInputDelegate) this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putParcelableArrayList(AppActivity.KEY_REMINDERS, reminders);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            reminders = savedInstanceState.getParcelableArrayList(AppActivity.KEY_REMINDERS);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        currentReminderHolder = null;
    }

}