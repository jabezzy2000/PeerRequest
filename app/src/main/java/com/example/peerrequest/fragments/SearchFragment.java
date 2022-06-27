package com.example.peerrequest.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.peerrequest.R;
import com.example.peerrequest.activities.MainActivity;
import com.example.peerrequest.adapters.SearchAdapter;
import com.example.peerrequest.adapters.TaskAdapter;
import com.example.peerrequest.models.Task;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    protected SearchAdapter searchAdapter;
    private int limit = 10;
    String querySearch;
    EditText search;
    Button searchButton;
    RecyclerView recyclerView;
    protected List<Task> allTasks;
    private MainActivity mainActivity;
    String TAG = "SearchFragment";
    String SUCCESS = "task successful";
    String ERROR = "task unsuccessful";


    public SearchFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
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
                    Log.i(TAG, SUCCESS);
                    allTasks.addAll(tasks);
                    searchAdapter.notifyDataSetChanged();
                }
            }
        });
    }

//    private void queryTasks(String querySearch) {
//        ParseQuery<Task> query = new ParseQuery<>("Tasks");
//        query.whereContains("RequestsTitle", querySearch);
//        query.findInBackground((Task, e) -> {
//            if (e == null) {
//                searchAdapter = new SearchAdapter(getContext(), allTasks);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                recyclerView.setAdapter(searchAdapter);
//            } else {
//                Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

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
                Toast.makeText(mainActivity, "Search for:" + querySearch, Toast.LENGTH_SHORT).show();
                queryTasks(querySearch);
            }
        });

    }
}