package com.example.peerrequest.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peerrequest.R;
import com.example.peerrequest.Utilities;
import com.example.peerrequest.activities.TaskDetailActivity;
import com.example.peerrequest.models.Task;

import org.parceler.Parcels;

import java.util.List;

import okhttp3.internal.Util;

public class ProfileTasksAdapter extends RecyclerView.Adapter<ProfileTasksAdapter.ProfileViewHolder> {
    private final List<Task> tasks;
    Context context;
    String TAG = "ProfileTasksAdapter";


    public ProfileTasksAdapter(Context context, List<Task> tasks) {
        this.tasks = tasks;
        this.context = context;

    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_profile_item_task, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView taskTitle;
        private TextView taskDescription;
        private TextView time;
        private TextView numberOfRequests;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.tvProfileTaskTitle);
            taskDescription = itemView.findViewById(R.id.tvProfileTaskDescription);
            time = itemView.findViewById(R.id.tvProfileTime);
            numberOfRequests = itemView.findViewById(R.id.tvRequestsNumber);
            itemView.setOnClickListener(this);

        }

        public void bind(Task task) {
            taskTitle.setText(task.getTaskTitle());
            taskDescription.setText(task.getDescription());
            time.setText(Utilities.getSimpleTime(task.getCreatedAt()));
           // numberOfRequests.setText();

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Task task = tasks.get(position);
                Intent intent = new Intent(v.getContext(), TaskDetailActivity.class);
                intent.putExtra(Task.class.getSimpleName(), Parcels.wrap(task));
                v.getContext().startActivity(intent);

            }
        }
    }
}


