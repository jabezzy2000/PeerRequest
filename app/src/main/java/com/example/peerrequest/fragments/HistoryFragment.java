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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peerrequest.R;
import com.example.peerrequest.Utilities;
import com.example.peerrequest.adapters.HistoryRequestAdapter;
import com.example.peerrequest.adapters.HistoryTaskAdapter;
import com.example.peerrequest.databinding.FragmentHistoryBinding;
import com.example.peerrequest.models.Requests;
import com.example.peerrequest.models.Task;
import com.example.peerrequest.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {
    private FragmentHistoryBinding binding;
    private static final int MAX_LIMIT = 40 ;
    protected HistoryRequestAdapter historyRequestAdapter;
    protected HistoryTaskAdapter historyTaskAdapter;
    private Button requestButton;
    private Button taskButton;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    protected List<Requests> allRequests;
    protected List<Task> allTasks;
    private TextView clickToContinueText;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Binding layout to view
        binding = FragmentHistoryBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        allRequests = new ArrayList<>();
        allTasks = new ArrayList<>();
        historyRequestAdapter = new HistoryRequestAdapter(getContext(),allRequests);
        historyTaskAdapter = new HistoryTaskAdapter(allTasks,getContext());
        clickToContinueText = binding.tvDisposableText;
        requestButton = binding.requestsHistory;
        taskButton = binding.tasksHistory;
        progressBar = binding.historyProgressBar;
        recyclerView = binding.historyRecyclerView;
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickToContinueText.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(historyRequestAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                queryRequests();
            }
        });

        taskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickToContinueText.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(historyTaskAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                queryTasks();
            }
        });

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
                    progressBar.setVisibility(View.INVISIBLE);
                    historyRequestAdapter.notifyDataSetChanged();
                }
                else{
                    Utilities.showAlert("Error", ""+e.getMessage(),getContext());
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
                    progressBar.setVisibility(View.INVISIBLE);
                    historyTaskAdapter.notifyDataSetChanged();
                }
                else{
                    Utilities.showAlert("Error", ""+e.getMessage(),getContext());
                }
            }
        });
    }

    @Override
    public void onDestroyView() { //setting bind to null for when the view is destroyed
        super.onDestroyView();
        binding=null;
    }
}