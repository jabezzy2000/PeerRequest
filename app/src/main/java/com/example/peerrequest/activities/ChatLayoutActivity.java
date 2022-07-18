package com.example.peerrequest.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.example.peerrequest.R;
import com.example.peerrequest.Utilities;
import com.example.peerrequest.adapters.ChatLayoutAdapter;
import com.example.peerrequest.databinding.ActivityChatLayoutBinding;
import com.example.peerrequest.databinding.ActivityMapsBinding;
import com.example.peerrequest.models.Message;
import com.example.peerrequest.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatLayoutActivity extends AppCompatActivity {
    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 20;
    private ChatLayoutAdapter chatLayoutAdapter;
    private List<Message> messages;
    private String userID;
    private TextView noChatYet;
    private ActivityChatLayoutBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //using ViewBinding instead of repetitive findViewsByIds
        binding = ActivityChatLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        messages = new ArrayList<>();
        chatLayoutAdapter = new ChatLayoutAdapter(this,userID,messages);
        binding.chatLayoutRecyclerView.setAdapter(chatLayoutAdapter);
        binding.chatLayoutRecyclerView.setLayoutManager(linearLayoutManager);
        noChatYet = findViewById(R.id.tvNoMessages);
        queryMessages();

    }

    private void queryMessages() {
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        query.whereEqualTo(Message.SENDER_ID_KEY,  User.getCurrentUser());


        ParseQuery<Message> query2 = ParseQuery.getQuery(Message.class);
        query2.whereEqualTo(Message.RECEIVER_ID_KEY, User.getCurrentUser());

        List<ParseQuery<Message>> list = new ArrayList<ParseQuery<Message>>();
        list.add(query);
        list.add(query2);

        ParseQuery<Message> query3 = ParseQuery.or(list);
        query3.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query3.addDescendingOrder(Message.KEY_CREATED_AT);
        query3.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> objects, ParseException e) {
                if (e==null) {
                    Set<String> container = new HashSet<>();
                    for (Message m: objects) {
                        String senderAndReceiver = "";
                        try{
                            String sender = ((User) (m.getSenderIdKey().fetchIfNeeded())).getObjectId();
                            String receiver = ((User) (m.getReceiverIdKey().fetchIfNeeded())).getObjectId();
                            if (sender.equals(ParseUser.getCurrentUser().getObjectId())) {
                                senderAndReceiver += sender + " " + receiver;

                            } else {
                                senderAndReceiver += receiver + " " + sender;
                            }
                            if (container.contains(senderAndReceiver)) {
                                continue;
                            }  else {
                                messages.add(m);
                                container.add(senderAndReceiver);
                            }


                        } catch(ParseException err) {
                            Utilities.showAlert("Error", "An Error Has Occured", getApplicationContext());

                        }
                    }
                    if(messages.isEmpty()){
                        noChatYet.setVisibility(View.VISIBLE);
                    }
                    binding.chatLayoutProgressBar.setVisibility(View.INVISIBLE);
                    chatLayoutAdapter.notifyDataSetChanged();
                } else {
                    Utilities.showAlert("Error", "An Error Has Occured", getApplicationContext());;
                }
            }
        });

    }
}
