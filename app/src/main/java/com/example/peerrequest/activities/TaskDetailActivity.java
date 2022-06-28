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
import com.example.peerrequest.models.Requests;
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
    ImageButton requestbutton;
    ImageButton edit;
    Task task;
    public AlertDialog.Builder dialogBuilder;
    public AlertDialog dialog;
    public EditText etCoverLetter;
    public EditText popupTaskTitle, popupTaskDescription;
    public Button popupSave, popupCancel;
    public String TAG = "TaskDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        task = (Task) Parcels.unwrap(getIntent().getParcelableExtra(Task.class.getSimpleName()));
        user = task.getUser();
        name = findViewById(R.id.tvTaskDetailName);
        taskTitle = findViewById(R.id.tvTaskDetailTitle);
        taskDescription = findViewById(R.id.tvTaskDetailDescription);
        profilePicture = findViewById(R.id.ivTaskDetailProfilePicture);
        rating = findViewById(R.id.tvTaskDetailRating);
        requestbutton = findViewById(R.id.ibTaskDetailRequestBtn);
        edit = findViewById(R.id.ibTaskDetailEditBtn);
        if (!User.getCurrentUser().getUsername().equals(task.getUser().getUsername())) {
            edit.setVisibility(View.GONE);
        }//if the current user isn't the author of the task, make button invisible
        else {
            edit.setVisibility(View.VISIBLE);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //edit button
                    createNewEditDialog();
                }
            });
        }
        //setting request button visible/invisible depending on current user
        if (User.getCurrentUser().getUsername().equals(task.getUser().getUsername())) {
            requestbutton.setVisibility(View.GONE);
        }//if the current user isn't the author of the task, make button visible
        else {
            requestbutton.setVisibility(View.VISIBLE);
            requestbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //edit button
                    createNewRequest();
                }
            });
        }
        task = (Task) Parcels.unwrap(getIntent().getParcelableExtra(Task.class.getSimpleName()));
        name.setText(task.getUser().getUsername());
        taskTitle.setText(task.getTaskTitle());
        taskDescription.setText(task.getDescription());
        user = (User) task.getUser();
        rating.setText(user.getUserRating());
    }

    private void createNewRequest() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View request_task_popup = getLayoutInflater().inflate(R.layout.request_task_popup, null);
        etCoverLetter = request_task_popup.findViewById(R.id.etCoverLetter);
        popupSave = request_task_popup.findViewById(R.id.btnOk);
        popupCancel = request_task_popup.findViewById(R.id.btnCancel);
        dialogBuilder.setView(request_task_popup);
        dialog = dialogBuilder.create();
        dialog.show();
        popupSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Requests requests = new Requests();
                String CoverLetter = etCoverLetter.getText().toString();
                requests.setKeyCoverLetter(CoverLetter);
                requests.setTask(task);
                requests.setKeyUser(user);
                requests.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                });
                Toast.makeText(TaskDetailActivity.this, "Requested", Toast.LENGTH_SHORT).show();
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
