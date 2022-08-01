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
import com.example.peerrequest.Utilities;
import com.example.peerrequest.activities.TaskDetailActivity;
import com.example.peerrequest.models.Task;
import com.example.peerrequest.models.User;

import org.parceler.Parcels;

import java.util.List;

public class HistoryTaskAdapter extends RecyclerView.Adapter<HistoryTaskAdapter.HistoryViewHolder> {
    private final List<Task> taskList;
    Context context;

    public HistoryTaskAdapter(List<Task> tasks, Context context) {
        this.taskList = tasks;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.bind(task);

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView historyTaskProfilePicture;
        private TextView historyTaskUsername;
        private TextView historyTaskTitle;
        private TextView historyTaskDescription;
        private TextView historyNumberOfRequests;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            historyTaskProfilePicture = itemView.findViewById(R.id.historyTaskProfileImage);
            historyTaskDescription = itemView.findViewById(R.id.historyTaskDescription);
            historyTaskUsername = itemView.findViewById(R.id.historyTaskProfileName);
            historyTaskTitle = itemView.findViewById(R.id.historyTaskTitle);
            historyNumberOfRequests = itemView.findViewById(R.id.historyNumberOfTasks);
            itemView.setOnClickListener(this);
        }

        public void bind(Task task) {
            User user = task.getUser();
            historyTaskTitle.setText(task.getTaskTitle());
            historyTaskUsername.setText(user.getUsername());
            historyTaskDescription.setText(task.getDescription());
            if(task.getKeyNumberOfRequests().equals(1)){
                historyNumberOfRequests.setText(""+ task.getKeyNumberOfRequests() + " request");
            }
            else{
                historyNumberOfRequests.setText(""+ task.getKeyNumberOfRequests() + " requests");
            }
            Utilities.roundedImage(context,user.getProfilePicture().getUrl(),historyTaskProfilePicture,80);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Task task = taskList.get(position);
                Intent intent = new Intent(v.getContext(), TaskDetailActivity.class);
                intent.putExtra(Task.class.getSimpleName(), Parcels.wrap(task));
                v.getContext().startActivity(intent);


            }
        }
    }
}
