package com.example.peerrequest.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peerrequest.R;
import com.example.peerrequest.Utilities;
import com.example.peerrequest.adapters.ChatAdapter;
import com.example.peerrequest.models.Message;
import com.example.peerrequest.models.Ratings;
import com.example.peerrequest.models.Requests;
import com.example.peerrequest.models.User;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    public User otherUser;
    public User otherTexter;
    static final long POLL_INTERVAL = TimeUnit.SECONDS.toMillis(3);
    Handler myHandler = new android.os.Handler();
    Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages(); // where the query first happens
            myHandler.postDelayed(this, POLL_INTERVAL);
        }
    };

    EditText etMessage;
    ImageButton ibSend;
    RecyclerView rvChat;
    List<Message> mMessages;
    Boolean mFirstLoad;
    RecyclerView.Adapter mAdapter;
    Button completed;
    Requests requests;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        otherTexter = (User) Parcels.unwrap(getIntent().getParcelableExtra("requester"));
//        otherTexter = getIntent().getParcelableExtra("requester");
        otherUser = getIntent().getParcelableExtra("otherUser");
        if (otherTexter == null) {
            setupMessagePosting(otherUser);
        } else {
            setupMessagePosting(otherTexter);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        // Only start checking for new messages when the app becomes active in foreground
        myHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);
    }

    @Override
    protected void onPause() {
        // Stop background task from refreshing messages, to avoid unnecessary traffic & battery drain
        myHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    // Set up button event handler which posts the entered message to Parse
    void setupMessagePosting(User otherUser) {
        requests = Parcels.unwrap(getIntent().getParcelableExtra("request"));
        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));
        etMessage = (EditText) findViewById(R.id.etMessage);
        ibSend = (ImageButton) findViewById(R.id.ibSend);
        rvChat = (RecyclerView) findViewById(R.id.rvChat);
        completed = findViewById(R.id.completedBtn);
        mMessages = new ArrayList<>();
        mFirstLoad = true;
        mAdapter = new ChatAdapter(ChatActivity.this, mMessages);
        rvChat.setAdapter(mAdapter);

        // associate the LayoutManager with the RecylcerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setReverseLayout(true);
        rvChat.setLayoutManager(linearLayoutManager);

        // When send button is clicked, create message object on Parse
        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etMessage.getText().toString();
                Message message = new Message();
                message.setSenderIdKey((User) User.getCurrentUser());
                message.setReceiverIdKey(otherUser);
                message.setBody(data);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(ChatActivity.this, "Successfully created message on Parse",
                                Toast.LENGTH_SHORT).show();
                        refreshMessages();
                    }
                });
                etMessage.setText(null);
            }
        });

        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRateUserDialog(ChatActivity.this);
            }
        });
    }

    void refreshMessages() {
//         Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        query.whereEqualTo(Message.SENDER_ID_KEY, User.getCurrentUser());
        query.whereEqualTo(Message.RECEIVER_ID_KEY, otherUser);


        ParseQuery<Message> query2 = ParseQuery.getQuery(Message.class);
        query2.whereEqualTo(Message.RECEIVER_ID_KEY, (User) ParseUser.getCurrentUser());
        query2.whereEqualTo(Message.SENDER_ID_KEY, otherUser);

        List<ParseQuery<Message>> list = new ArrayList<ParseQuery<Message>>();
        list.add(query);
        list.add(query2);

        ParseQuery<Message> query3 = ParseQuery.or(list);
        query3.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query3.include(Message.SENDER_ID_KEY);
        query3.include(Message.RECEIVER_ID_KEY);

        query3.addDescendingOrder(Message.KEY_CREATED_AT);
        query3.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> objects, ParseException e) {
                if (e == null) ;
                {
                    mMessages.clear();
                    mMessages.addAll(objects);
                    mAdapter.notifyDataSetChanged(); // update adapter
                }
            }
        });
    }

    private void createRateUserDialog(Context context) {
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        Toast.makeText(context, "request created by " + requests.getUser().getUsername(), Toast.LENGTH_SHORT).show();
        dialogBuilder = new AlertDialog.Builder(context);
        User user = requests.getUser(); //this is the user we are rating
        View popup = View.inflate(context, R.layout.rating_dialog, null);
        RatingBar ratingBar = popup.findViewById(R.id.rating);
        Button setRating = popup.findViewById(R.id.setRating);
        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        setRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double rating = ratingBar.getRating(); // this will be added to the total rating
                double currrentRating = Double.parseDouble(user.getUserRating());
                int currentNumberOfRatings = requests.getNumberOfRating();
                int currentTotalRatings = requests.getKeyTotalRating();
                int newCurrentTotalRatings = (int) (currentTotalRatings + rating);
                requests.setKeyTotalRating((int) (currentTotalRatings + rating));
                int newNumberOfRating = currentNumberOfRatings + 1;
                requests.setNumberOfRating(newNumberOfRating);
                int userRating = newCurrentTotalRatings / newNumberOfRating;
                requests.setKeyRating(String.valueOf(userRating));
                requests.setCompletedLister("true");
                dialog.dismiss();
                requests.saveInBackground();
            }
        });
        dialog.show();


    }

}