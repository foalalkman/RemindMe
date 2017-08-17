package com.bignerdranch.android.remindme;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;

import java.util.ArrayList;

/**
 * Created by annika on 2017-08-16.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ReminderHolder>
{
    private ArrayList<Reminder> dataset;
    private Context context;
    private UserInputDelegate delegate;

    public MyRecyclerAdapter(ArrayList<Reminder> reminders, Context c, UserInputDelegate d) {
        dataset = reminders;
        context = c;
        delegate = d;
    }

    public interface UserInputDelegate {
        void pickNewPlace(ReminderHolder holder);
        void editText(ReminderHolder r);
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
        setButtonListener(holder);
        holder.position = position;

        // holder update
    }

    private void setButtonListener(ReminderHolder holder) {
        final Context c = this.context;
        final ReminderHolder reminderHolder = holder;
        final ArrayList<Reminder> reminderArrayList = dataset;

        holder.optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(c, reminderHolder.optionsButton);
                popupMenu.inflate(R.menu.reminder_options_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.options_menu_edit_location:
                                delegate.pickNewPlace(reminderHolder);
                                break;

                            case R.id.options_menu_edit_text:
                                delegate.editText(reminderHolder);

                                notifyItemChanged(reminderHolder.position);
                                break;

                            case R.id.options_menu_delete:
                                reminderArrayList.remove(reminderHolder.reminder);
                                notifyDataSetChanged();
                                break;
                        }
                        return false;
                    }
                });

                popupMenu.show();

            }
        });
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
        int position;

        public ReminderHolder(View view) {
            super(view);

            titleView = (TextView) view.findViewById(R.id.reminder_title_view);
            locationView = (TextView) view.findViewById(R.id.reminder_location_view);
            optionsButton = (ImageButton) view.findViewById(R.id.options_menu_button);
        }
    }
}
