package com.example.peerrequest.fragments;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.peerrequest.R;
import com.example.peerrequest.activities.HomeActivity;
import com.example.peerrequest.adapters.SearchAdapter;
import com.example.peerrequest.models.Location;
import com.example.peerrequest.models.Requests;
import com.example.peerrequest.models.Task;
import com.example.peerrequest.models.User;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchFragment extends Fragment {
    protected SearchAdapter searchAdapter;
    private final int limit = 50;
    public String querySearch;
    public EditText search;
    public Button searchButton;
    public RecyclerView recyclerView;
    public ChipGroup chipGroup;
    protected List<Task> allTasks;
    private final WeakReference<HomeActivity> homeActivityWeakReference;
    private final String TAG = "SearchFragment";
    private final String ERROR = "task unsuccessful";


    public SearchFragment(HomeActivity homeActivity) {
        homeActivityWeakReference = new WeakReference<HomeActivity>(homeActivity);
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    private void queryTasks(String querySearch) {
        ParseQuery<Task> query1 = ParseQuery.getQuery(Task.class);
        query1.whereMatches(Task.KEY_REQUESTS_TITLE, "(" + querySearch + ")", "i");

        ParseQuery<Task> query2 = ParseQuery.getQuery(Task.class);
        query2.whereMatches(Task.KEY_REQUEST_DESCRIPTION, "(" + querySearch + ")", "i");

        List<ParseQuery<Task>> list = new ArrayList<ParseQuery<Task>>();
        list.add(query1);
        list.add(query2);

        ParseQuery<Task> query = ParseQuery.or(list);
        query.include(Task.KEY_USER + "." + "userRatings");
        query.include(Task.KEY_USER);
        query.setLimit(limit);
        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, ERROR + e.getMessage(), e);
                } else {
                    allTasks.addAll(objects);
                    Collections.shuffle(allTasks);
                    searchAdapter.notifyDataSetChanged();

                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        allTasks = new ArrayList<>();
        recyclerView = view.findViewById(R.id.rvSearch);//linking recycler view
        searchAdapter = new SearchAdapter(getContext(), allTasks);
        recyclerView.setAdapter(searchAdapter);//linking to search adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        search = view.findViewById(R.id.etSearch);
        searchButton = view.findViewById(R.id.btnSearch);
        chipGroup = view.findViewById(R.id.chipGroup);
        Chip createdAtChip = chipGroup.findViewById(R.id.createdAtChip);
        Chip nearYouChip = chipGroup.findViewById(R.id.nearYouChip);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                querySearch = search.getText().toString();
                queryTasks(querySearch);
            }
        });
        createdAtChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (createdAtChip.isChecked()) {
                    createdAtChip.setChecked(true);
                    querySearch = search.getText().toString();
                    Toast.makeText(getContext(), querySearch, Toast.LENGTH_SHORT).show();
                    if (querySearch.equals("")) {
                        queryTasksWithoutTextForDate();
                    } else {
                        querySearch = search.getText().toString();
                        queryTasksWithTextForDate(querySearch);
                    }

                } else {
                    createdAtChip.setChecked(false);
                    allTasks.clear();
                    searchAdapter.notifyDataSetChanged();
                }
            }
        });

        nearYouChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nearYouChip.isChecked()) {
                    nearYouChip.setChecked(true);
                    querySearch = search.getText().toString();
                    if (querySearch.equals("")) {
                        queryTasksWithoutTextForLocation();
                    } else {
                        querySearch = search.getText().toString();
                        queryTasksWithTextForLocation(querySearch);
                    }

                } else {
                    createdAtChip.setChecked(false);
                    allTasks.clear();
                    searchAdapter.notifyDataSetChanged();
                }

            }
        });

    }

    private void queryTasksWithoutTextForLocation() {
        //Calculating distance with Haversine formula
        //if distance is greater than current dist, add to end of list
        //else, add to beginning of list
        ParseQuery<Location> query = ParseQuery.getQuery(Location.class);
        query.include(Location.KEY_POINTER_TO_TASK);
        query.include(Location.KEY_POINTER_TO_TASK + "." + "UserPointer");
        query.include(Location.KEY_POINTER_TO_TASK + "." + "UserPointer"+ "." + "userRatings");
        query.setLimit(limit);
        User currentUser = (User) User.getCurrentUser();
        double userLatitude = Double.parseDouble(currentUser.getKeyUserCurrentLocationLatitude());
        double userLongitude = Double.parseDouble(currentUser.getKeyUserCurrentLocationLongitude());
        query.findInBackground(new FindCallback<Location>() {
            @Override
            public void done(List<Location> locationsWithTasks, ParseException e) {
                if (e != null) {
                    Log.e(TAG, ERROR + e.getMessage(), e);
                } else {
                    double currentDistance = 0.0;
                    for(int i=0; i<locationsWithTasks.size(); i++){
                        Location location = locationsWithTasks.get(i);
                        double latitude = Double.parseDouble(location.getKeyLatitude());
                        double longitude = Double.parseDouble(location.getKeyLongitude());
                        if(getDistance(userLatitude,userLongitude,latitude,longitude) > currentDistance){
                            allTasks.add(location.getTask());
                            currentDistance = getDistance(userLatitude,userLongitude,latitude,longitude);
                        }
                        else{
                            allTasks.add(0,location.getTask());
                            currentDistance = getDistance(userLatitude,userLongitude,latitude,longitude);
                        }
                    }
                    searchAdapter.notifyDataSetChanged();

                }
            }
        });
    }

    private void queryTasksWithTextForLocation(String querySearch) {
        ParseQuery<Location> query1 = ParseQuery.getQuery(Location.class);
        query1.whereMatches(Location.KEY_TITLE, "(" + querySearch + ")", "i");

        ParseQuery<Location> query2 = ParseQuery.getQuery(Location.class);
        query2.whereMatches(Location.KEY_DESCRIPTION, "(" + querySearch + ")", "i");

        List<ParseQuery<Location>> list = new ArrayList<ParseQuery<Location>>();
        list.add(query1);
        list.add(query2);

        ParseQuery<Location> query = ParseQuery.or(list);
        query.include(Location.KEY_POINTER_TO_TASK);
        query.include(Location.KEY_POINTER_TO_TASK + "." + "UserPointer");
        query.include(Location.KEY_POINTER_TO_TASK + "." + "UserPointer"+ "." + "userRatings");
        query.setLimit(limit);
        User currentUser = (User) User.getCurrentUser();
        double userLatitude = Double.parseDouble(currentUser.getKeyUserCurrentLocationLatitude());
        double userLongitude = Double.parseDouble(currentUser.getKeyUserCurrentLocationLongitude());
        query.findInBackground(new FindCallback<Location>() {
            @Override
            public void done(List<Location> locationsWithTasks, ParseException e) {
                if (e != null) {
                    Log.e(TAG, ERROR + e.getMessage(), e);
                } else {
                    double currentDistance = 0.0;
                    for(int i=0; i<locationsWithTasks.size(); i++){
                        Location location = locationsWithTasks.get(i);
                        double latitude = Double.parseDouble(location.getKeyLatitude());
                        double longitude = Double.parseDouble(location.getKeyLongitude());
                        if(getDistance(userLatitude,userLongitude,latitude,longitude) > currentDistance){
                            allTasks.add(location.getTask());
                            currentDistance = getDistance(userLatitude,userLongitude,latitude,longitude);
                        }
                        else{
                            allTasks.add(0,location.getTask());
                            currentDistance = getDistance(userLatitude,userLongitude,latitude,longitude);
                        }
                    }
                    searchAdapter.notifyDataSetChanged();

                }
            }
        });

    }

    private void queryTasksWithTextForDate(String querySearch) {
        ParseQuery<Task> query1 = ParseQuery.getQuery(Task.class);
        query1.whereMatches(Task.KEY_REQUESTS_TITLE, "(" + querySearch + ")", "i");

        ParseQuery<Task> query2 = ParseQuery.getQuery(Task.class);
        query2.whereMatches(Task.KEY_REQUEST_DESCRIPTION, "(" + querySearch + ")", "i");

        List<ParseQuery<Task>> list = new ArrayList<ParseQuery<Task>>();
        list.add(query1);
        list.add(query2);

        ParseQuery<Task> query = ParseQuery.or(list);
        query.addDescendingOrder(Task.KEY_CREATED_AT);
        query.include(Task.KEY_USER);
        query.include(Task.KEY_USER + "." + "userRatings");
        query.setLimit(limit);
        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, ERROR + e.getMessage(), e);
                } else {
                    allTasks.addAll(objects);
                    searchAdapter.notifyDataSetChanged();

                }
            }
        });
    }

    private void queryTasksWithoutTextForDate() {
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.addDescendingOrder(Task.KEY_CREATED_AT);
        query.include(Task.KEY_USER + "." + "userRatings");
        query.include(Task.KEY_USER);
        query.setLimit(limit);
        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> tasks, ParseException e) {
                if (e != null) {
                    Log.e(TAG, ERROR + e.getMessage(), e);
                } else {
                    allTasks.addAll(tasks);
                    searchAdapter.notifyDataSetChanged();

                }
            }
        });
    }

    public static double getDistance(double latitude_1, double longitude_1, double latitude_2, double longitude_2) {

        double dat = Math.toRadians(latitude_2 - latitude_1);
        double don = Math.toRadians(longitude_2 - longitude_1);
        double lat1 = Math.toRadians(latitude_1);
        double lat2 = Math.toRadians(latitude_2);
        double a = Math.pow(Math.sin(dat / 2), 2) +
                Math.pow(Math.sin(don / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        double distance = c * rad;
        return distance;
    }
}

