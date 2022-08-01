package com.example.peerrequest.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

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
import com.example.peerrequest.models.Task;
import com.example.peerrequest.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChatActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 30;
    public User userFromChatLayout;
    public User userFromTaskDetail;
    static final long POLL_INTERVAL = TimeUnit.SECONDS.toMillis(50);
    public Handler myHandler = new android.os.Handler();
    public Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages(); // where the query first happens
            myHandler.postDelayed(this, POLL_INTERVAL);
        }
    };
    private EditText etMessage;
    private TextView tvHitButtonToComplete;
    private ImageButton ibSend;
    private RecyclerView rvChat;
    private List<Message> mMessages;
    private Boolean mFirstLoad;
    private RecyclerView.Adapter mAdapter;
    private Button completed;
    private Requests requests;
    private User user;
    private User taskLister;
    private Task taskBeingCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //after accepting a message, you'd automatically be brought to the chat activity
        //the task was passed over in the intent so it's easily retrieved
        taskBeingCompleted = (Task) Parcels.unwrap(getIntent().getParcelableExtra("task"));
        //In the instances where the user decides to move to the chat on his/her own volition,
        //the task needs to be retrieved manually by querying previous messages and then getting the task from one
        progressBar = findViewById(R.id.chatProgressBar);
        //since there are two ways to move to the chat activity, I try to account for both "other users"
        userFromTaskDetail = (User) Parcels.unwrap(getIntent().getParcelableExtra("requester"));
        userFromChatLayout = getIntent().getParcelableExtra("otherUser");
        tvHitButtonToComplete = findViewById(R.id.textView3);
        if (userFromTaskDetail == null) {
            setupMessagePosting(userFromChatLayout);
        } else {
            setupMessagePosting(userFromTaskDetail);
        }
        queryMessages();
    }

    private Task getTaskFromPreviousMessage() {
        Message previousMessage = mMessages.get(0);
        taskBeingCompleted = previousMessage.getTaskIdPointer();
        return taskBeingCompleted;
    }

    private void queryMessages() {
        //         Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        query.whereEqualTo(Message.SENDER_ID_KEY, User.getCurrentUser());
        query.whereEqualTo(Message.RECEIVER_ID_KEY, userFromChatLayout);


        ParseQuery<Message> query2 = ParseQuery.getQuery(Message.class);
        query2.whereEqualTo(Message.RECEIVER_ID_KEY, (User) ParseUser.getCurrentUser());
        query2.whereEqualTo(Message.SENDER_ID_KEY, userFromChatLayout);

        List<ParseQuery<Message>> list = new ArrayList<ParseQuery<Message>>();
        list.add(query);
        list.add(query2);

        ParseQuery<Message> query3 = ParseQuery.or(list);
        query3.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query3.include(Message.SENDER_ID_KEY);
        query3.include(Message.RECEIVER_ID_KEY);
        query3.include(Message.TASK_ID_POINTER);

        query3.addDescendingOrder(Message.KEY_CREATED_AT);
        query3.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() == mMessages.size()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        return;
                    } else {
                        mMessages.clear();
                        for(Message m : objects){
                            if(m.getTaskIdPointer().getTaskTitle().equals(taskBeingCompleted.getTaskTitle())){
                               mMessages.add(m);
                            }
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                        mAdapter.notifyDataSetChanged(); // update adapter
                    }
                } else {
                    Utilities.showAlert("Error", "" + e.getMessage(), getApplicationContext());
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
        progressBar = findViewById(R.id.chatProgressBar);
        try {
             taskLister = (User) taskBeingCompleted.getUser().fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(!taskLister.getUsername().equals(User.getCurrentUser().getUsername())){
            tvHitButtonToComplete.setVisibility(View.INVISIBLE);
            completed.setVisibility(View.INVISIBLE);

        }


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
                if (taskBeingCompleted != null) {
                    message.setTaskIdPointer(taskBeingCompleted);
                } else {
                    Task currentTaskBeingCompleted = getTaskFromPreviousMessage();
                    message.setTaskIdPointer(currentTaskBeingCompleted);
                }
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            progressBar.setVisibility(View.INVISIBLE);
                            mMessages.add(0, message);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Utilities.showAlert("Error", "" + e.getMessage(), getApplicationContext());
                        }
                    }
                });
                etMessage.setText(null);
            }
        });

        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRateUserDialog(ChatActivity.this, otherUser, taskBeingCompleted);
            }
        });
    }

    void refreshMessages() {
//         Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        query.whereEqualTo(Message.SENDER_ID_KEY, User.getCurrentUser());
        query.whereEqualTo(Message.RECEIVER_ID_KEY, userFromChatLayout);


        ParseQuery<Message> query2 = ParseQuery.getQuery(Message.class);
        query2.whereEqualTo(Message.RECEIVER_ID_KEY, (User) ParseUser.getCurrentUser());
        query2.whereEqualTo(Message.SENDER_ID_KEY, userFromChatLayout);

        List<ParseQuery<Message>> list = new ArrayList<ParseQuery<Message>>();
        list.add(query);
        list.add(query2);

        ParseQuery<Message> query3 = ParseQuery.or(list);
        query3.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query3.include(Message.SENDER_ID_KEY);
        query3.include(Message.RECEIVER_ID_KEY);
        query3.include(Message.TASK_ID_POINTER);

        query3.addDescendingOrder(Message.KEY_CREATED_AT);
        query3.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() == mMessages.size()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        return;
                    } else {
                        mMessages.clear();
                        for(Message m : objects){
                            if(m.getTaskIdPointer().getTaskTitle().equals(taskBeingCompleted.getTaskTitle())){
                                mMessages.add(m);
                            }
                        }
                        progressBar.setVisibility(View.INVISIBLE);
//                        mMessages.addAll(objects);
                        mAdapter.notifyDataSetChanged(); // update adapter
                    }
                } else {
                    Utilities.showAlert("Error", "" + e.getMessage(), getApplicationContext());
                }
            }
        });
    }

    private void createRateUserDialog(Context context, User otherUser, Task taskBeingCompleted) {
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        dialogBuilder = new AlertDialog.Builder(context);
        User user = otherUser; //this is the user we are rating
        View popup = View.inflate(context, R.layout.rating_dialog, null);
        RatingBar ratingBar = popup.findViewById(R.id.rating);
        Button setRating = popup.findViewById(R.id.setRating);
        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        setRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRatings(ratingBar, dialog, taskBeingCompleted);
            }
        });
        dialog.show();
    }

    private void saveRatings(RatingBar ratingBar, AlertDialog dialog, Task taskBeingCompleted) {
        //querying current rating
        ParseQuery<Ratings> query = ParseQuery.getQuery(Ratings.class);
        query.whereEqualTo("pointerToUser", userFromChatLayout);
        query.findInBackground(new FindCallback<Ratings>() {
            @Override
            public void done(List<Ratings> objects, ParseException e) {
                if (e == null) {
                    //if there is no error
                    //We calculate the and add the recent rating to the user's rating
                    Ratings ratings = objects.get(0);
                    double rating = ratingBar.getRating(); // this will be added to the total rating
                    Ratings currentUserRatingSet = ratings;
                    double currentNumberOfRatings = currentUserRatingSet.getNumberOfRating();
                    double currentTotalRatings = currentUserRatingSet.getKeyTotalRating();
                    int newCurrentTotalRatings = (int) (currentTotalRatings + rating);
                    currentUserRatingSet.setKeyTotalRating((currentTotalRatings + rating));
                    double newNumberOfRating = currentNumberOfRatings + 1;
                    currentUserRatingSet.setNumberOfRating(newNumberOfRating);
                    double userRating = newCurrentTotalRatings / newNumberOfRating;
                    ratings.setKeyRating(userRating);
                    ratings.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                //mark task as complete and then save changes to back4app
                                taskBeingCompleted.markTaskAsComplete();
                                taskBeingCompleted.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e==null){
                                            //if task saves successfully, show a toast that shows it's been saved successfully
                                            dialog.dismiss();
                                            User notificationReceiver;
                                            if (userFromTaskDetail==null){
                                                notificationReceiver = userFromChatLayout;
                                            }
                                            else{
                                                notificationReceiver = userFromTaskDetail;
                                            }
                                            Utilities.sendANotification(notificationReceiver,getApplicationContext(),taskBeingCompleted);
                                            Utilities.makeToast(getApplicationContext(), "Task Ended Successfully");
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                            //delay moving to home activity for snackbar to show
                                                @Override
                                                public void run() {
                                                    goHomeActivity();
                                                }

                                            }, 2000000); // 20000ms delay
                                        }
                                        else{
                                            Utilities.showAlert("Error", "An Error Has Occured",getApplicationContext());
                                        }
                                    }
                                });
                            } else {
                                Utilities.showAlert("Error", "" + e.getMessage(), getApplicationContext());
                            }
                        }
                    });

                    goHomeActivity();
                } else {
                    Utilities.showAlert("Error", "" + e.getMessage(), getApplicationContext());
                }
            }
        });
    }

    private void goHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }


}