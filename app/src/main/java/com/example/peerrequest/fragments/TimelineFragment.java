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
import android.widget.Toast;

import com.example.peerrequest.activities.MapsActivity;
import com.example.peerrequest.R;
import com.example.peerrequest.activities.MainActivity;
import com.example.peerrequest.adapters.TaskAdapter;
import com.example.peerrequest.models.Task;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
    String SUCCESS = "Task Successful";
    String ERROR = "Task Unsuccessful";
    private MainActivity mainActivity;

    public TimelineFragment(MainActivity mainActivity) {
        // Required empty public constructor
        this.mainActivity = mainActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    private void queryTasks() { //specifying data I want to query
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.include(Task.KEY_USER);
        query.setLimit(limit);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> tasks, ParseException e) {
                if (e != null) {
                    Log.e(TAG, ERROR, e);
                } else {
                    Log.i(TAG, SUCCESS);
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
        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        dialog.show();
        popupSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = popupTaskTitle.getText().toString();
                String description = popupTaskDescription.getText().toString();
                Task task = new Task();
                task.setUser(ParseUser.getCurrentUser());
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
            }
        });

        popupCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}