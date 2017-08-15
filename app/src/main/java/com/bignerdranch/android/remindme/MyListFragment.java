package com.bignerdranch.android.remindme;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by annika on 2017-08-09.
 */

public class MyListFragment extends Fragment {

    ArrayList<Reminder> reminders;
    View view;
    private TextView tempView;

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
        tempView = (TextView) view.findViewById(R.id.temp_text_view);

        printReminders();

        return view;
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

    private void printReminders() {
        if (reminders != null) {
            String s = "";

            for (Reminder r : reminders) {
                s += r.getText() + " at " + r.getLocationName() + "\n";
            }

            tempView.setText(s);

        } else {
            tempView.setText("Nothing");

        }

    }



}