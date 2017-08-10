package com.bignerdranch.android.remindme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

/**
 * Created by annika on 2017-08-09.
 */

public class NewReminderFragment extends android.support.v4.app.Fragment {

    View view;
    private TextView tempView;
    private static final int PLACE_PICKER_REQUEST = 199;
    private int status;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.new_reminder_fragment_view, container, false);
        tempView = (TextView) view.findViewById(R.id.temp_text_view);


        status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());
        if (status == ConnectionResult.SUCCESS) {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            try {
                startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);

            } catch (GooglePlayServicesRepairableException e) {
                Toast.makeText(getActivity(), "exception", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                Toast.makeText(getActivity(), "exception", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "NOT", Toast.LENGTH_LONG).show();
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {

            if (resultCode == Activity.RESULT_OK) {

                Place place = PlacePicker.getPlace(data, getActivity());
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }
}