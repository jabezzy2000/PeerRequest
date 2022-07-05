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
import com.example.peerrequest.models.Task;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    protected SearchAdapter searchAdapter;
    private final int limit = 10;
    public String querySearch;
    public EditText search;
    public Button searchButton;
    public RecyclerView recyclerView;
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

    private void queryTasks(String querySearch) { //added a string param
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.whereEqualTo(Task.KEY_REQUESTS_TITLE, querySearch);
        query.setLimit(limit);
        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> tasks, ParseException e) {
                if (e != null) {
                    Log.e(TAG, ERROR, e);
                } else {
                    allTasks.addAll(tasks);
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
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                querySearch = search.getText().toString();
                queryTasks(querySearch);
            }
        });

    }
}