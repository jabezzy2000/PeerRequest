package com.example.peerrequest.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.peerrequest.activities.MapsActivity;
import com.example.peerrequest.R;
import com.example.peerrequest.activities.HomeActivity;
import com.example.peerrequest.adapters.TaskAdapter;
import com.example.peerrequest.models.Location;
import com.example.peerrequest.models.Task;
import com.example.peerrequest.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.lang.ref.WeakReference;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class TimelineFragment extends Fragment {
    protected TaskAdapter taskAdapter;
    ImageView profileImage;
    private int limit = 10;
    public AlertDialog.Builder dialogBuilder;
    public AlertDialog dialog;
    public EditText popupTaskTitle;
    public EditText popupTaskDescription;
    public Button popupSave, popupCancel;
    ImageButton addTasks;
    RecyclerView recyclerView;
    public ImageButton mapButton;
    protected List<Task> allTasks;
    String TAG = "TimelineFragment";
    String ERROR = "Task Unsuccessful";
    LatLng latLng;


    public TimelineFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    //specifying data I want to query
    private void queryTasks() {
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.include(Task.KEY_USER);
        query.setLimit(limit);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> tasks, ParseException e) {
                if (e != null) {
                    Log.e(TAG, ERROR + e.getMessage(), e);
                } else {
                    allTasks.addAll(tasks);
                    taskAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rvHomeTimeline);
        allTasks = new ArrayList<>();
        latLng = MapsActivity.currentLocation;
        taskAdapter = new TaskAdapter(getContext(), allTasks);
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        profileImage = view.findViewById(R.id.ivProfileImage);
        addTasks = view.findViewById(R.id.fabAddTask);
        mapButton = view.findViewById(R.id.ibMap);
        addTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewContactDialog();
            }
        });
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToMapActivity();
            }
        });
        queryTasks();
    }

    private void changeToMapActivity() {
        Intent intent = new Intent(getActivity(), MapsActivity.class);
        startActivity(intent);
    }

    public void createNewContactDialog() {
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View popup = getLayoutInflater().inflate(R.layout.popup, null);
        popupTaskTitle = popup.findViewById(R.id.taskTitle);
        popupTaskDescription = popup.findViewById(R.id.taskDescription);
        popupSave = popup.findViewById(R.id.btnOk);
        popupCancel = popup.findViewById(R.id.btnCancel);
        popupSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();
                String title = popupTaskTitle.getText().toString();
                String description = popupTaskDescription.getText().toString();

                task.setUser((User) ParseUser.getCurrentUser());
                task.setTaskTitle(title);
                task.setDescription(description);
                task.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                });
                dialog.dismiss();
                HomeActivity homeActivity = (HomeActivity) getActivity();
                Location location = new Location();
                location.setKeyLongitude(homeActivity.getLongitude()+"");
                location.setKeyLatitude(homeActivity.getLatitude()+"");
                location.setKeyTitle(title);
                location.saveInBackground();
            }
        });

        popupCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        dialog.show();
    }



}