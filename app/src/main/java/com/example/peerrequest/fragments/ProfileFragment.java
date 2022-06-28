package com.example.peerrequest.fragments;

import android.content.Context;
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
import android.widget.TextView;

import com.example.peerrequest.R;
import com.example.peerrequest.activities.LoginActivity;
import com.example.peerrequest.adapters.ProfileAdapter;
import com.example.peerrequest.adapters.TaskAdapter;
import com.example.peerrequest.models.Task;
import com.example.peerrequest.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    public ProfileAdapter profileAdapter;
    public TextView name;
    public TextView rating;
    public ImageView profileImage;
    public ImageButton logOut;
    private final int limit = 10;
    protected List<Task> allTasks;
    public RecyclerView recyclerView;
    private final String TAG = "TImelineFragment";
    private final String ERROR = "task unsuccessful";


    public ProfileFragment() {
        // Required empty public constructor
    }


    private void queryTasks() { //specifying data I want to query
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.include(Task.KEY_USER);
        query.whereEqualTo(Task.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(limit);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> tasks, ParseException e) {
                if (e != null) {
                    Log.e(TAG, ERROR, e);
                } else {
                    allTasks.addAll(tasks);
                    profileAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = view.findViewById(R.id.tvName);
        rating = view.findViewById(R.id.tvRating);
        profileImage = view.findViewById(R.id.ivProfileImage);
        logOut = view.findViewById(R.id.ibLogOut);
        recyclerView = view.findViewById(R.id.rvProfileTasks);
        allTasks = new ArrayList<>();
        profileAdapter = new ProfileAdapter(getContext(), allTasks);
        recyclerView.setAdapter(profileAdapter); //attaching adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        name.setText(User.getCurrentUser().getUsername());
        queryTasks();

    }


    private void logout() { //defining method to logout
        ParseUser.logOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

    }
}