package com.bignerdranch.android.remindme;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by annika on 2017-08-16.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ReminderHolder> {
    ArrayList<Reminder> dataset;

    public MyRecyclerAdapter(ArrayList<Reminder> reminders) {
        dataset = reminders;
    }


    @Override
    public ReminderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recyclerview, parent, false);

        return new ReminderHolder(v);
    }

    @Override
    public void onBindViewHolder(ReminderHolder holder, int position) {
        final Reminder reminder = dataset.get(position);
        holder.reminder = reminder;
        holder.titleView.setText(reminder.getText());
        holder.locationView.setText(reminder.getLocationName());
        // holder update
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    class ReminderHolder extends RecyclerView.ViewHolder {

        TextView titleView;
        TextView locationView;
        ImageButton optionsButton;
        Reminder reminder;

        public ReminderHolder(View view) {
            super(view);
            titleView = (TextView) view.findViewById(R.id.reminder_title_view);
            locationView = (TextView) view.findViewById(R.id.reminder_location_view);
            optionsButton = (ImageButton) view.findViewById(R.id.options_menu_button);
            optionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //
                }
            });
        }
    }



}
