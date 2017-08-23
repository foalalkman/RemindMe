package com.bignerdranch.android.remindme;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by annika on 2017-08-16.
 */


/**
 * MyRecyclerAdapter is a customized RecyclerAdapter for displaying the reminders with the text and
 * the location name. It also provides the user with options for each reminder, such as edit text,
 * change Place and remove.
 * It communicates with MyListFragment through the UserInputDelegate interface.
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

    /**
     * Interface implemented by MyListFragment, for handling button clicks
     * in the options menu.
     */
    interface UserInputDelegate {
        void pickNewPlace(ReminderHolder holder);
        void editText(ReminderHolder r);
        void notifyActivity();
        void setButtonVisibility();
        void initializeRecyclerView();
    }

    /**
     * Inflates the ReminderHolder with the view.
     * @param parent the parent in which the new view will be added to.
     * @param viewType view type of the new view.
     * @return a ViewHolder with the new View.
     */
    @Override
    public ReminderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recyclerview, parent, false);

        return new ReminderHolder(v);
    }

    /**
     * The RecyclerView calls it to display the data at the specified position.
     * @param holder the current reminderholder.
     * @param position the specified position.
     */
    @Override
    public void onBindViewHolder(ReminderHolder holder, int position) {
        final Reminder reminder = dataset.get(position);
        holder.reminder = reminder;
        holder.titleView.setText(reminder.getText());
        holder.titleView.setTypeface(null, Typeface.BOLD);
        holder.locationView.setText(reminder.getLocationName());
        setButtonListener(holder);
        holder.position = position;
    }

    /**
     * Creates the optionsButton and sets the onclickListener to it.
     * When clicked, a little options menu pops up, where the user
     * can choose to remove the reminder, edit the text or change the place.
     * @param holder
     */
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
                                delegate.setButtonVisibility();
                                delegate.initializeRecyclerView();
                                delegate.notifyActivity();
                                break;
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });
    }

    /**
     * @return the number of reminders held by the adapter.
     */
    @Override
    public int getItemCount() {
        return dataset.size();
    }

    /**
     * ReminderHolder is a customized ViewHolder,
     * that binds the data of the Reminder to a view for being displayed
     * inside the RecyclerView.
     */
    class ReminderHolder extends RecyclerView.ViewHolder {

        TextView titleView;
        TextView locationView;
        ImageButton optionsButton;
        Reminder reminder;
        int position;

        ReminderHolder(View view) {
            super(view);

            titleView = (TextView) view.findViewById(R.id.reminder_title_view);
            locationView = (TextView) view.findViewById(R.id.reminder_location_view);
            optionsButton = (ImageButton) view.findViewById(R.id.options_menu_button);
        }
    }
}
