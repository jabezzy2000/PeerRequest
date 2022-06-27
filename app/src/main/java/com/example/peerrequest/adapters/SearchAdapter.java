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
        ImageView ivProfilePicture;
        TextView tvUsername;
        TextView tvTime;
        TextView tvTaskTitle;
        TextView tvTaskDescription;

        public ViewHolder(@NonNull View itemView) {
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