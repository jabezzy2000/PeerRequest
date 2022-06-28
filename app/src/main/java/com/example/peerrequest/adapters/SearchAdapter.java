package com.example.peerrequest.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peerrequest.R;
import com.example.peerrequest.activities.TaskDetailActivity;
import com.example.peerrequest.models.Task;

import org.parceler.Parcels;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private final List<Task> tasks;

    public SearchAdapter(Context context, List<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView profilePicture;
        TextView username;
        TextView time;
        TextView taskTitle;
        TextView taskDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePicture = itemView.findViewById(R.id.ivProfilePicture);
            username = itemView.findViewById(R.id.tvUserName);
            time = itemView.findViewById(R.id.tvTime);
            taskTitle = itemView.findViewById(R.id.tvTaskTitile);
            taskDescription = itemView.findViewById(R.id.tvTaskDescription);
            itemView.setOnClickListener(this);
        }

        public void bind(Task task) {
            username.setText(task.getUser().getUsername());
            taskTitle.setText(task.getTaskTitle());
            taskDescription.setText(task.getDescription());
            time.setText(task.getCreatedAt().toString());
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
