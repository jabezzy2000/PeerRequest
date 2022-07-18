package com.example.peerrequest.fragments;

import android.app.Activity;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peerrequest.R;
import com.example.peerrequest.Utilities;
import com.example.peerrequest.activities.ChatActivity;
import com.example.peerrequest.activities.HomeActivity;
import com.example.peerrequest.activities.LoginActivity;
import com.example.peerrequest.adapters.ProfileTasksAdapter;
import com.example.peerrequest.adapters.TaskDetailAdapter;
import com.example.peerrequest.models.Message;
import com.example.peerrequest.models.Ratings;
import com.example.peerrequest.models.Requests;
import com.example.peerrequest.models.Task;
import com.example.peerrequest.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;
import org.w3c.dom.Text;

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
    private User user;
    private Ratings currentUserRating;


    public ProfileTasksFragment() {
        // Required empty public constructor
    }

    private void queryTasks() { //specifying data I want to query
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.include(Task.KEY_USER);
        query.include(User.KEY_USER_RATINGS_PROPERTIES);
        query.whereEqualTo(Task.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(limit);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> tasks, ParseException e) {
                if (e != null) {
                    Utilities.showAlert("Error", ""+e.getMessage(),getContext());
                } else {
                    allTasks.addAll(tasks);
                    profileTasksAdapter.notifyDataSetChanged();
                }
            }
        });

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
        currentUserRating = user.getKeyUserRatingsProperties();
        name = view.findViewById(R.id.tvName);
        rating = view.findViewById(R.id.tvRating);
        profileImage = view.findViewById(R.id.ivProfileImage);
        logOut = view.findViewById(R.id.ibLogOut);
        requestsNumber = view.findViewById(R.id.tvRequestsNumber);
        allTasks = new ArrayList<>();
        profileTasksAdapter = new ProfileTasksAdapter(getContext(), allTasks);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        ParseQuery<Ratings> query = ParseQuery.getQuery(Ratings.class);
        query.whereEqualTo(Ratings.KEY_USER_POINTER, user);
        query.findInBackground(new FindCallback<Ratings>() {
            @Override
            public void done(List<Ratings> objects, ParseException e) {
                if(e==null){
                    currentUserRating = objects.get(0);
                    name.setText(user.getUsername());
                    rating.setText(String.valueOf(currentUserRating.getUserRating()));
                    if (user.getProfilePicture() != null) {
                        Utilities.roundedImage(getContext(), user.getProfilePicture().getUrl(), profileImage, 50);
                    }
                    queryTasks();
                }
                else{
                    Utilities.showAlert("Error", ""+e.getMessage(),getContext());
                }
            }
        });
    }

    private void logout() { //defining method to logout
        ParseUser.logOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

    }
}