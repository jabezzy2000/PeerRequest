package com.example.peerrequest.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.peerrequest.R;
import com.example.peerrequest.adapters.HistoryRequestAdapter;
import com.example.peerrequest.adapters.HistoryTaskAdapter;
import com.example.peerrequest.models.Requests;
import com.example.peerrequest.models.Task;
import com.example.peerrequest.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {
    private static final int MAX_LIMIT = 40 ;
    protected HistoryRequestAdapter historyRequestAdapter;
    protected HistoryTaskAdapter historyTaskAdapter;
    private final int limit = 30;
    private Button requestButton;
    private Button taskButton;
    private RecyclerView recyclerView;
    protected List<Requests> allRequests;
    protected List<Task> allTasks;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        allRequests = new ArrayList<>();
        allTasks = new ArrayList<>();
        historyRequestAdapter = new HistoryRequestAdapter(getContext(),allRequests);
        historyTaskAdapter = new HistoryTaskAdapter(allTasks,getContext());
        requestButton= view.findViewById(R.id.requestsHistory);
        taskButton = view.findViewById(R.id.tasksHistory);
        recyclerView = view.findViewById(R.id.historyRecyclerView);
//        recyclerView.setAdapter(historyRequestAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "request button clicked", Toast.LENGTH_SHORT).show();
                recyclerView.setAdapter(historyRequestAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                queryRequests();

            }
        });

        taskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "task button clicked", Toast.LENGTH_SHORT).show();
                recyclerView.setAdapter(historyTaskAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                queryTasks();
            }
        });
//        queryRequests();
    }

    private void queryRequests(){
        ParseQuery<Requests> query = ParseQuery.getQuery(Requests.class);
        query.setLimit(MAX_LIMIT);
        query.whereEqualTo(Requests.KEY_USER, User.getCurrentUser());
        query.include(Requests.KEY_USER);
        query.include(Requests.KEY_TASK + "." + "UserPointer");
        query.findInBackground(new FindCallback<Requests>() {
            @Override
            public void done(List<Requests> objects, ParseException e) {
                if (e==null){
                    allRequests.clear();
                    allRequests.addAll(objects);
                    historyRequestAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    private void queryTasks(){
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.setLimit(MAX_LIMIT);
        query.whereEqualTo(Task.KEY_USER, User.getCurrentUser());
        query.include(Task.KEY_USER);
        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> objects, ParseException e) {
                if (e==null){
                    allTasks.clear();
                    allTasks.addAll(objects);
                    historyTaskAdapter.notifyDataSetChanged();
                }

            }
        });
    }
}