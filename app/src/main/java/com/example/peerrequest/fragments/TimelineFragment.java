package com.example.peerrequest.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.peerrequest.Utilities;
import com.example.peerrequest.activities.MapsActivity;
import com.example.peerrequest.R;
import com.example.peerrequest.activities.HomeActivity;
import com.example.peerrequest.adapters.TaskAdapter;
import com.example.peerrequest.models.Task;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TimelineFragment extends Fragment {
    protected TaskAdapter taskAdapter;
    private ImageView profileImage;
    private final int limit = 30;
    private ImageButton addTasks;
    private RecyclerView recyclerView;
    public ImageButton mapButton;
    protected List<Task> allTasks;
    private SwipeRefreshLayout swipeContainer;
    public LatLng latLng;
    public ProgressBar progressBar;
    public HomeActivity homeActivity;


    public TimelineFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        progressBar = view.findViewById(R.id.pbLoading);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        homeActivity = (HomeActivity) getActivity();
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.VISIBLE);
                queryTasks(); // query new tasks
                swipeContainer.setRefreshing(false); // stop refreshing after tasks have been queried
                progressBar.setVisibility(View.GONE);
            }
        });

        // Configuring the colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_purple, android.R.color.holo_red_light);
        return view;
    }


    //specifying data I want to query
    private void queryTasks() {
        progressBar.setVisibility(View.VISIBLE);
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        //proactively querying the User class related to task
        query.include(Task.KEY_USER);
        //proactively querying the userRatings using the pointer in the User class
        query.include(Task.KEY_USER + "." + "userRatings");
        query.setLimit(limit);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> tasks, ParseException e) {
                if (e != null) {
                    Utilities.showAlert("Error", "" + e.getMessage(), getContext());
                } else {
                    if (allTasks == tasks) {
                        return;
                    } else {
                        allTasks.clear();
                        progressBar.setVisibility(View.INVISIBLE);
                        allTasks.addAll(tasks);
                        //randomizing tasks on homescreen to allow for variety
                        Collections.shuffle(allTasks);
                        taskAdapter.notifyDataSetChanged();
                    }

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
                Utilities.createAddTaskDialog(getContext(), requireActivity(), homeActivity);
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
        intent.putExtra("allTasks", Parcels.wrap(allTasks));
        startActivity(intent);
    }
}