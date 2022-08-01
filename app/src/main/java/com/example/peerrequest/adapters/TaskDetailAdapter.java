package com.example.peerrequest.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peerrequest.R;
import com.example.peerrequest.Utilities;
import com.example.peerrequest.activities.ChatActivity;
import com.example.peerrequest.activities.TaskDetailActivity;
import com.example.peerrequest.fragments.InProgressFragment;
import com.example.peerrequest.models.Ratings;
import com.example.peerrequest.models.Requests;
import com.example.peerrequest.models.Task;
import com.example.peerrequest.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;


import org.parceler.Parcels;

import java.util.List;

public class TaskDetailAdapter extends RecyclerView.Adapter<TaskDetailAdapter.TaskDetailViewHolder> {
    private final List<Requests> requests;
    Context context;
    TaskDetailActivity taskDetailActivity;
    String TAG = "TaskDetailAdapter";
    String rating;


    public TaskDetailAdapter(TaskDetailActivity context, List<Requests> requests, TaskDetailActivity taskDetailActivity) {
        this.requests = requests;
        this.taskDetailActivity = taskDetailActivity;
        this.context = context;

    }

    @NonNull
    @Override
    public TaskDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_detail_item_task, parent, false);
        return new TaskDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskDetailViewHolder holder, int position) {
        Requests request = requests.get(position);
        holder.bind(request);

    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class TaskDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView username;
        TextView userRating;
        TextView itemTaskTimeRequested;
        TextView itemTaskCoverLetter;
        TextView usersCoverLetterText;


        public TaskDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.itemTaskName);
            userRating = itemView.findViewById(R.id.itemTaskRating);
            itemTaskTimeRequested = itemView.findViewById(R.id.itemTaskTime);
            itemTaskCoverLetter = itemView.findViewById(R.id.itemTaskCoverLetter);
            usersCoverLetterText = itemView.findViewById(R.id.tvUsersRequestLetter);
            itemView.setOnClickListener(this);

        }

        public void bind(Requests requests) {
            username.setText(requests.getUser().getUsername());
            itemTaskCoverLetter.setText(requests.getKeyCoverLetter());
            itemTaskTimeRequested.setText(Utilities.getSimpleTime(requests.getCreatedAt()));
            usersCoverLetterText.setText(String.format("%s's Cover Letter:", requests.getUser().getUsername()));

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Requests request = requests.get(position);
                Task task = request.getTask();
                User user = (User) User.getCurrentUser();
                User requester = request.getUser();
                ParseQuery<Ratings> query = ParseQuery.getQuery(Ratings.class);
                query.whereEqualTo(Ratings.KEY_USER_POINTER, requester);
                query.findInBackground(new FindCallback<Ratings>() {
                    @Override
                    public void done(List<Ratings> ratings, ParseException e) {
                        if (e != null) {
                            Utilities.showAlert("Error", ""+e.getMessage(),context);
                            return;
                        }
                        else
                        {
                            Double roundedRating = Utilities.roundRating(ratings.get(0).getUserRating());
                            rating = String.valueOf(roundedRating);
                        }
                    }
                });
                Utilities.createNewSubmitDialog(request, context, taskDetailActivity, user, rating, task);
            }
            }

        }
    }


