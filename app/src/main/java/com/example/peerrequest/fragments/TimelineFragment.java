package com.example.peerrequest.fragments;

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
import android.widget.ImageView;

import com.example.peerrequest.R;
import com.example.peerrequest.adapters.TaskAdapter;
import com.example.peerrequest.models.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class TimelineFragment extends Fragment {
    protected TaskAdapter adapter;
    ImageView profileImage;
    Button listToggle;
    Button mapToggle;
    public AlertDialog.Builder dialogBuilder;
    public AlertDialog dialog;
    public EditText popupTaskTitle;
    public  EditText popupTaskDescription;
    public Button popupSave, popupCancel;
    FloatingActionButton addTasks;
    RecyclerView recyclerView;
    protected List<Task> allTasks;
    String TAG = "tag";
    String SUCCESS = "task successful";
    String ERROR = "task unsuccessful";

    public TimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    private void queryTasks() {
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.include(Task.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> tasks, ParseException e) {
                if (e != null) {
                    Log.e(TAG, ERROR, e);
                } else {
                    Log.i(TAG, SUCCESS);
                    allTasks.addAll(tasks);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.rvHomeTimeline);
        allTasks = new ArrayList<>();
        adapter = new TaskAdapter(getContext(), allTasks);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        profileImage = view.findViewById(R.id.ivProfileImage);
        listToggle = view.findViewById(R.id.btnList);
        mapToggle = view.findViewById(R.id.btnMaps);
        addTasks = view.findViewById(R.id.fabAddButton);
        popupTaskTitle = view.findViewById(R.id.taskTitle);
        popupTaskDescription = view.findViewById(R.id.taskDescription);
        popupSave = view.findViewById(R.id.btnOk);
        popupCancel = view.findViewById(R.id.btnCancel);
        addTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewContactDialog();
            }
        });
        queryTasks();
    }

    private void addNewTask() {
    }

    private void createNewContactDialog(){ // did this
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View popup = getLayoutInflater().inflate(R.layout.popup, null);
        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        dialog.show();
    }
}