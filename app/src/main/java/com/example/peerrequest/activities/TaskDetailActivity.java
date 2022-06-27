package com.example.peerrequest.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import com.example.peerrequest.models.Task;
import com.example.peerrequest.models.User;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

public class TaskDetailActivity extends AppCompatActivity {
    TextView name;
    User user;
    TextView taskTitle;
    TextView taskDescription;
    ImageView profilePicture;
    TextView rating;
    ImageButton request;
    ImageButton edit;
    Task task;
    public AlertDialog.Builder dialogBuilder;
    public AlertDialog dialog;
    public EditText popupTaskTitle;
    public EditText popupTaskDescription;
    public Button popupSave, popupCancel;
    public String TAG = "TaskDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        task = (Task) Parcels.unwrap(getIntent().getParcelableExtra(Task.class.getSimpleName()));

        name= findViewById(R.id.tvTaskDetailName);
        taskTitle = findViewById(R.id.tvTaskDetailTitle);
        taskDescription = findViewById(R.id.tvTaskDetailDescription);
        profilePicture = findViewById(R.id.ivTaskDetailProfilePicture);
        rating = findViewById(R.id.tvTaskDetailRating);
        request = findViewById(R.id.ibTaskDetailRequestBtn);
        edit = findViewById(R.id.ibTaskDetailEditBtn);
        Log.i(TAG, "onCreate: " + User.getCurrentUser().getUsername());
        Log.i(TAG, "other: " + task.getUser().getUsername());
        if(User.getCurrentUser().getUsername()!=task.getUser().getUsername()){
            edit.setVisibility(View.GONE);
        }//if the current user is the author of the task, make button invisible
        else{
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //edit button
                    createNewEditDialog();
                }
            });
        }
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        task = (Task) Parcels.unwrap(getIntent().getParcelableExtra(Task.class.getSimpleName()));
        name.setText(User.getCurrentUser().getUsername());
        taskTitle.setText(task.getTaskTitle());
        taskDescription.setText(task.getDescription());
        user = (User) task.getUser();
        rating.setText(user.getUserRating());
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
