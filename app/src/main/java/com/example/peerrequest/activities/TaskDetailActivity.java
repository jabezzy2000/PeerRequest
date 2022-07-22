package com.example.peerrequest.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peerrequest.R;
import com.example.peerrequest.Utilities;
import com.example.peerrequest.adapters.TaskDetailAdapter;
import com.example.peerrequest.databinding.ActivityChatLayoutBinding;
import com.example.peerrequest.databinding.ActivityTaskDetailBinding;
import com.example.peerrequest.models.Ratings;
import com.example.peerrequest.models.Requests;
import com.example.peerrequest.models.Task;
import com.example.peerrequest.models.User;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class TaskDetailActivity extends AppCompatActivity {
    public TextView name;
    public User user;
    public TextView taskTitle;
    public TextView taskDescription;
    public ImageView profilePicture;
    public TextView rating;
    public ImageButton requestButton;
    public ImageButton edit;
    public Task task;
    public TextView requestsText;
    public RecyclerView recyclerView;
    public TextView makeARequest;
    private final int limit = 30;
    protected List<Requests> allRequests;
    protected TaskDetailAdapter taskDetailAdapter;
    public AlertDialog.Builder dialogBuilder;
    public AlertDialog dialog;
    public EditText coverLetter;
    public EditText popupTaskTitle, popupTaskDescription;
    public Button popupSave, popupCancel;
    ActivityTaskDetailBinding binding;
    public String TAG = "TaskDetailActivity";
    private final String ERROR = "Query Unsuccessful";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        task = (Task) Parcels.unwrap(getIntent().getParcelableExtra(Task.class.getSimpleName()));
        recyclerView = binding.rvTaskDetailTasks;
        allRequests = new ArrayList<>();
        taskDetailAdapter = new TaskDetailAdapter(this, allRequests, this);
        recyclerView.setAdapter(taskDetailAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        user = task.getUser();
        name = binding.tvTaskDetailName;
        requestsText = binding.tvRequests;
        taskTitle = binding.tvTaskDetailTitle;
        taskDescription = binding.tvTaskDetailDescription;
        profilePicture = binding.ivTaskDetailProfilePicture;
        rating = binding.tvTaskDetailRating;
        requestButton = binding.ibTaskDetailRequestBtn;
        makeARequest = binding.tvMakeARequest;
        edit = binding.ibTaskDetailEditBtn;
        if (User.getCurrentUser().getUsername().equals(task.getUser().getUsername())) {
            recyclerView.setVisibility(View.VISIBLE);
            makeARequest.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            makeARequest.setVisibility(View.VISIBLE);
        }

        if (User.getCurrentUser().getUsername().equals(task.getUser().getUsername())) {
            requestsText.setVisibility(View.VISIBLE);
        } else {
            requestsText.setVisibility(View.GONE);
        }
        //if the current user isn't the author of the task,button becomes invisible
        if (!User.getCurrentUser().getUsername().equals(task.getUser().getUsername())) {
            edit.setVisibility(View.GONE);
        } else {
            edit.setVisibility(View.VISIBLE);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createNewEditDialog();
                }
            });
        }
        //setting request button visible/invisible depending on current user
        if (User.getCurrentUser().getUsername().equals(task.getUser().getUsername())) {
            requestButton.setVisibility(View.GONE);
        } else {
            requestButton.setVisibility(View.VISIBLE);
            requestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createNewRequest();
                }
            });
        }
        task = (Task) Parcels.unwrap(getIntent().getParcelableExtra(Task.class.getSimpleName()));
        name.setText(task.getUser().getUsername());
        user = (User) task.getUser();
        setRatings(user, task);
    }

    private void setRatings(User user, Task task) {
        ParseQuery<Ratings> query = ParseQuery.getQuery(Ratings.class);
        query.whereEqualTo("pointerToUser", user);
        query.findInBackground(new FindCallback<Ratings>() {
            @Override
            public void done(List<Ratings> objects, ParseException e) {
                if (e == null) {
                    Ratings ratings = objects.get(0);
                    String value = String.valueOf(ratings.getUserRating());
                    rating.setText(value);
                    //checking if the user has a profile picture to avoid app crashing
                    if (user.getProfilePicture() != null) {
                        Utilities.setImage(getApplicationContext(), user.getProfilePicture().getUrl(), profilePicture);
                    }
                    taskTitle.setText(task.getTaskTitle());
                    taskDescription.setText(task.getDescription());
                    queryRequests();

                }
            }
        });
    }

    private void queryRequests() { //querying requests according to what task is being shown
        ParseQuery<Requests> query = ParseQuery.getQuery(Requests.class);
        query.include(Requests.KEY_USER);
        query.include(Requests.KEY_USER + "." + "userRatings");
        query.whereEqualTo(Requests.KEY_TASK, task);
        query.setLimit(limit);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Requests>() {
            @Override
            public void done(List<Requests> requests, ParseException e) {
                if (e != null) {
                    Utilities.showAlert("Error", ""+e.getMessage(),TaskDetailActivity.this);
                    return;
                }
                else {
                    if(requests.size()==0){
                        binding.noRequestsYet.setVisibility(View.VISIBLE);
                    }
                    else {
                        allRequests.addAll(requests);
                        taskDetailAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void createNewRequest() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View request_task_popup = getLayoutInflater().inflate(R.layout.request_task_popup, null);
        coverLetter = request_task_popup.findViewById(R.id.etCoverLetter);
        popupSave = request_task_popup.findViewById(R.id.btnOk);
        popupCancel = request_task_popup.findViewById(R.id.btnCancel);
        dialogBuilder.setView(request_task_popup);
        dialog = dialogBuilder.create();
        dialog.show();
        popupSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Requests requests = new Requests();
                String CoverLetter = coverLetter.getText().toString();
                requests.setKeyCoverLetter(CoverLetter);
                requests.setTask(task);
                requests.setKeyUser((User) User.getCurrentUser());
                requests.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Utilities.showAlert("Error", e.getMessage(), getApplicationContext());
                        } else {
                            task.increaseRequestNumber(); //increasing the number of requests for this particular task
                            task.saveInBackground();
                            Snackbar snackbar = Snackbar.make(v, "Request Submitted", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            //subscribing the user to a push notification channel
                            //naming the channel {username} + {requestCoverLetter} in the event many users have the same coverletter/username
                            ParsePush.subscribeInBackground("" + requests.getUser().getUsername() + requests.getKeyCoverLetter());
                        }
                    }
                });
                dialog.dismiss();
            }
        });

        popupCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    private void createNewEditDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View popup = getLayoutInflater().inflate(R.layout.popup, null);
        popupTaskTitle = popup.findViewById(R.id.taskTitle);
        popupTaskDescription = popup.findViewById(R.id.taskDescription);
        popupSave = popup.findViewById(R.id.btnOk);
        popupCancel = popup.findViewById(R.id.btnCancel);
        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        dialog.show();
        popupSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = popupTaskTitle.getText().toString();
                String description = popupTaskDescription.getText().toString();
                task.setTaskTitle(title);
                task.setDescription(description);
                task.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                });
                dialog.dismiss();
            }
        });

        popupCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
