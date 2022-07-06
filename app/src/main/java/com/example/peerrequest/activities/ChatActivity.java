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
import com.example.peerrequest.models.Requests;
import com.example.peerrequest.models.User;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    static final String USER_ID_KEY = "userId";
    static final String BODY_KEY = "body";
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    static final long POLL_INTERVAL = TimeUnit.SECONDS.toMillis(3);
    Handler myHandler = new android.os.Handler();
    Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
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

        if (ParseUser.getCurrentUser() != null) {
            startWithCurrentUser(); //TODO: We will build out this method in the next step
        } else {
            login();
        }
    }

    // Get the userId from the cached currentUser object
    void startWithCurrentUser() {
        // TODO:
        setupMessagePosting();
    }


    // Create an anonymous user using ParseAnonymousUtils and set sUserId
    void login() {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Anonymous login failed: ", e);
                } else {
                    startWithCurrentUser();
                }
            }
        });
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
    void setupMessagePosting() {
        requests = Parcels.unwrap(getIntent().getParcelableExtra("request"));
        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));
        etMessage = (EditText) findViewById(R.id.etMessage);
        ibSend = (ImageButton) findViewById(R.id.ibSend);
        rvChat = (RecyclerView) findViewById(R.id.rvChat);
        completed = findViewById(R.id.completedBtn);
        mMessages = new ArrayList<>();
        mFirstLoad = true;
        final String userId = ParseUser.getCurrentUser().getObjectId();
        mAdapter = new ChatAdapter(ChatActivity.this, userId, mMessages);
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
                // Using new `Message` Parse-backed model now
                Message message = new Message();
                message.setUserId(ParseUser.getCurrentUser().getObjectId());
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
        // Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        // Configure limit and sort order
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);

        // get the latest 50 messages, order will show up newest to oldest of this group
        query.orderByDescending("createdAt");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    mMessages.clear();
                    mMessages.addAll(messages);
                    mAdapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
                    if (mFirstLoad) {
                        rvChat.scrollToPosition(0);
                        mFirstLoad = false;
                    }
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });
    }

    private void createRateUserDialog(Context context) {
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        Toast.makeText(context, "request created by " + requests.getUser().getUsername(), Toast.LENGTH_SHORT).show();
        dialogBuilder = new AlertDialog.Builder(context);
        User user = requests.getUser();
        View popup = View.inflate(context, R.layout.rating_dialog, null);
        RatingBar ratingBar = popup.findViewById(R.id.rating);
        Button setRating = popup.findViewById(R.id.setRating);
        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        setRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Rating is " + ratingBar.getRating(), Toast.LENGTH_SHORT).show();
                double rating = ratingBar.getRating(); // this will be added to the total rating
                double currrentRating = Double.parseDouble(requests.getUser().getUserRating());
                int currentNumberOfRatings = requests.getUser().getNumberOfRating();
                int currentTotalRatings = requests.getUser().getKeyTotalRating();
                requests.getUser().setKeyTotalRating((int) (currentTotalRatings + rating));
                requests.getUser().setNumberOfRating(currentNumberOfRatings+1);
                int currentTotalRating = requests.getUser().getKeyTotalRating();
                int currentNumberOfRating = requests.getUser().getNumberOfRating();
                int userRating = currentTotalRating/currentNumberOfRating;
                requests.setKeyRating(String.valueOf(userRating));
                requests.increment("numberOfRating");
                dialog.dismiss();
                try {
                    requests.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.show();


    }
}