package com.bignerdranch.android.remindme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;

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


    @Override
    public void pickNewPlace() {
        Toast.makeText(getContext(), "Pickplace", Toast.LENGTH_SHORT);
    }

    @Override
    public void editText(MyRecyclerAdapter.ReminderHolder holder) {
        Toast.makeText(getContext(), "EditText", Toast.LENGTH_SHORT);

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

//        DefaultItemAnimator animator = new DefaultItemAnimator() {
//            @Override
//            public boolean canReuseUpdatedViewHolder(MyRecyclerAdapter.ReminderHolder viewHolder) {
//
//                return true;
//            }
//        };
//        recyclerView.setItemAnimator(animator);

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

}