package com.bignerdranch.android.remindme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by annika on 2017-08-09.
 */

public class MyListFragment extends Fragment {

    View view;
    private TextView tempView;
    private static final String KEY_REMINDERS_LIST = "list of reminders";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.list_fragment_view, container, false);
        tempView = (TextView) view.findViewById(R.id.temp_text_view);

        return view;
    }



}