package com.bignerdranch.android.remindme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by annika on 2017-08-09.
 */

public class NewReminderFragment extends android.support.v4.app.Fragment {


    private ReminderCreator reminderCreator;

    private static final int PLACE_PICKER_REQUEST = 199;
    private String dialogInputString;
    private int status;

    public interface ReminderCreator {
        void createReminder(Location l, String s);
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);

        try {
            reminderCreator = (ReminderCreator) a;

        } catch (ClassCastException e) {
            throw new ClassCastException(a.toString() + " does not implement ReminderCreator interface");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.map_layout_view, container, false);

        showInputDialog();

        return view;
    }

    private void launchPlacePicker() {
        status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());
        if (status == ConnectionResult.SUCCESS) {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            try {
                startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);

            } catch (GooglePlayServicesRepairableException e) {

                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        } else {
        }
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText inputField = new EditText(getContext());
        inputField.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setTitle(R.string.input_dialog_title);
        builder.setView(inputField);
        builder.setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInputString = inputField.getText().toString();

                launchPlacePicker();
            }
        });

        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {

            if (resultCode == Activity.RESULT_OK) {

                Place place = PlacePicker.getPlace(data, getActivity());
                showConfirmationDialog(place);
//                renderUserInput(place);
            }
        }
    }

    private void showConfirmationDialog(Place place) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(buildMessageString(place)).setTitle(R.string.confirmation_dialog_title);
        builder.setCancelable(false);

        final Location location = new Location(place.getName()+"");
        location.setLatitude(place.getLatLng().latitude);
        location.setLongitude(place.getLatLng().longitude);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                reminderCreator.createReminder(location, dialogInputString);
                dialogInputString = "";
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // reset variables
                dialogInputString = "";
            }
        });

        builder.show();
    }

    private String buildMessageString(Place place) {
        String message = String.format("%s at %s", dialogInputString, place.getName());
        return message;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        dialogInputString = "";
    }
}