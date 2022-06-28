package com.example.peerrequest.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peerrequest.R;
import com.example.peerrequest.activities.TaskDetailActivity;
import com.example.peerrequest.models.Task;
import com.example.peerrequest.models.User;

import org.parceler.Parcels;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private final List<Task> tasks;

    public TaskAdapter(Context context, List<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }


    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivProfilePicture;
        TextView tvUsername;
        TextView tvTime;
        TextView tvTaskTitle;
        TextView tvTaskDescription;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePicture = itemView.findViewById(R.id.ivProfilePicture);
            tvUsername = itemView.findViewById(R.id.tvUserName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTaskTitle = itemView.findViewById(R.id.tvTaskTitile);
            tvTaskDescription = itemView.findViewById(R.id.tvTaskDescription);
            itemView.setOnClickListener(this);
        }

        public void bind(Task task) {
            tvUsername.setText(task.getUser().getUsername());
            tvTaskTitle.setText(task.getTaskTitle());
            tvTaskDescription.setText(task.getDescription());
            tvTime.setText(task.getCreatedAt().toString());

            //implement glide method to see if user has a profile picture
        }

        @Override
        public void onClick(View view) { // gets position, opens a new activity for the detail
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Task task = tasks.get(position);
                Intent intent = new Intent(view.getContext(), TaskDetailActivity.class);
                intent.putExtra(Task.class.getSimpleName(), Parcels.wrap(task));
                view.getContext().startActivity(intent);

            }
        }
    }
}




