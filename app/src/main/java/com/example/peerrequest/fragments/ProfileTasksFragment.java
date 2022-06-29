package com.example.peerrequest.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peerrequest.R;
import com.example.peerrequest.Utilities;
import com.example.peerrequest.activities.LoginActivity;
import com.example.peerrequest.adapters.ProfileTasksAdapter;
import com.example.peerrequest.adapters.TaskDetailAdapter;
import com.example.peerrequest.models.Task;
import com.example.peerrequest.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileTasksFragment extends Fragment {
    public ProfileTasksAdapter profileTasksAdapter;
    public TextView name;
    public TextView rating;
    public ImageView profileImage;
    public ImageButton logOut;
    public TextView requestsNumber;
    private final int limit = 10;
    protected List<Task> allTasks;
    public RecyclerView recyclerView;
    private final String TAG = "TImelineFragment";
    private final String ERROR = "task unsuccessful";
    private User user;


    public ProfileTasksFragment() {
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
                    profileTasksAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public static ProfileTasksFragment newInstance() {
        ProfileTasksFragment fragment = new ProfileTasksFragment();
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
        user = (User) User.getCurrentUser();
        name = view.findViewById(R.id.tvName);
        rating = view.findViewById(R.id.tvRating);
        profileImage = view.findViewById(R.id.ivProfileImage);
        logOut = view.findViewById(R.id.ibLogOut);
        recyclerView = view.findViewById(R.id.rvProfileTasks);
        requestsNumber = view.findViewById(R.id.tvRequestsNumber);
        allTasks = new ArrayList<>();
        profileTasksAdapter = new ProfileTasksAdapter(getContext(), allTasks);
        recyclerView.setAdapter(profileTasksAdapter); //attaching adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        name.setText(User.getCurrentUser().getUsername());
        rating.setText(user.getUserRating());
        if (user.getProfilePicture() != null) {
            Utilities.roundedImage(getContext(), user.getProfilePicture().getUrl(), profileImage, 50);
        }
        queryTasks();

    }


    private void logout() { //defining method to logout
        ParseUser.logOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

    }
}