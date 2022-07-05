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
import android.widget.ImageButton;

import com.example.peerrequest.R;
import com.example.peerrequest.adapters.MessageAdapter;
import com.example.peerrequest.models.Message;
import com.example.peerrequest.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class InProgressFragment extends Fragment {
    ImageButton send;
    EditText typeMessage;
    String data;
    String TAG = "InProgressFragment";
    RecyclerView chatRecyclerView;
    protected List<Message> mMessages;
    boolean mFirstLoad;
    protected MessageAdapter messageAdapter;
    private String userID;
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;




    public InProgressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_in_progress, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        send = view.findViewById(R.id.ibSend);
        typeMessage = view.findViewById(R.id.etMessage);
        chatRecyclerView = view.findViewById(R.id.messageRecyclerView);
        mMessages = new ArrayList<>();
        mFirstLoad = true;
        userID = User.getCurrentUser().getObjectId();
        messageAdapter = new MessageAdapter(getContext(),userID,mMessages);
        chatRecyclerView.setAdapter(messageAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        chatRecyclerView.setLayoutManager(linearLayoutManager);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpMessagePosting();
            }
        });

    }

    private void setUpMessagePosting() {
        Message message = new Message();
        data = typeMessage.getText().toString();
        message.setBody(data);
        message.setUserId(User.getCurrentUser().getUsername());
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!= null) {
                    Log.e(TAG, "done: " + e.getMessage());
                }
            }
        });
    }

    void refreshMessages(){
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        // Configure limit and sort order
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    mMessages.clear();
                    mMessages.addAll(messages);
                    messageAdapter.notifyDataSetChanged();
                    if (mFirstLoad) {
                        chatRecyclerView.scrollToPosition(0);
                        mFirstLoad = false;
                    }
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });
    }
}