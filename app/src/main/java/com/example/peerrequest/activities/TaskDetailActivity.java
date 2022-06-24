package com.example.peerrequest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peerrequest.R;
import com.example.peerrequest.models.Task;
import com.example.peerrequest.models.User;

import org.parceler.Parcels;

public class TaskDetailActivity extends AppCompatActivity {
    TextView name;
    TextView taskTitle;
    TextView taskDescription;
    ImageView profilePicture;
    TextView rating;
    ImageButton request;
    ImageButton edit;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        name= findViewById(R.id.tvTaskDetailName);
        taskTitle = findViewById(R.id.tvTaskDetailTitle);
        taskDescription = findViewById(R.id.tvTaskDetailDescription);
        profilePicture = findViewById(R.id.ivTaskDetailProfilePicture);
        rating = findViewById(R.id.tvTaskDetailRating);
        request = findViewById(R.id.ibTaskDetailRequestBtn);
        edit = findViewById(R.id.ibTaskDetailEditBtn);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edit button
                Toast.makeText(TaskDetailActivity.this, "Button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TaskDetailActivity.this,"button clicked", Toast.LENGTH_SHORT).show();
            }
        });
        task = (Task) Parcels.unwrap(getIntent().getParcelableExtra(Task.class.getSimpleName()));
        name.setText(User.getCurrentUser().getUsername());
        taskTitle.setText(task.getTaskTitle());
        taskDescription.setText(task.getDescription());




    }
}