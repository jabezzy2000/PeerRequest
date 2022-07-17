package com.example.peerrequest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peerrequest.R;
import com.example.peerrequest.Utilities;
import com.example.peerrequest.models.Requests;
import com.example.peerrequest.models.Task;
import com.example.peerrequest.models.User;

import java.util.List;

public class HistoryRequestAdapter extends RecyclerView.Adapter<HistoryRequestAdapter.HistoryViewHolder> {
    private final List<Requests> requestsList;
    Context context;

    public HistoryRequestAdapter(Context context, List<Requests> requests) {
        this.requestsList = requests;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Requests request = requestsList.get(position);
        holder.bind(request);

    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView historyProfilePicture;
        private TextView historyUsername;
        private TextView historyTaskTitle;
        private TextView historyRequestDescription;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            historyProfilePicture = itemView.findViewById(R.id.historyRequestProfileImage);
            historyRequestDescription = itemView.findViewById(R.id.historyRequestDescription);
            historyUsername = itemView.findViewById(R.id.historyRequestProfileName);
            historyTaskTitle = itemView.findViewById(R.id.historyRequestTitle);
        }

        public void bind(Requests request) {
            Task task = request.getTask();
            User user = task.getUser();
            historyTaskTitle.setText(task.getTaskTitle());
            historyUsername.setText(user.getUsername());
            historyRequestDescription.setText(request.getKeyCoverLetter());
            Utilities.roundedImage(context, user.getProfilePicture().getUrl(), historyProfilePicture, 80);
        }
    }
}
