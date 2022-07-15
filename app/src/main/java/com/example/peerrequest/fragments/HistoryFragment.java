package com.example.peerrequest.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.peerrequest.R;
import com.example.peerrequest.adapters.HistoryAdapter;


public class HistoryFragment extends Fragment {
    protected HistoryAdapter historyAdapter;
    private final int limit = 30;
    private Button requestButton;
    private Button taskButton;
    private RecyclerView recyclerView;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }
}