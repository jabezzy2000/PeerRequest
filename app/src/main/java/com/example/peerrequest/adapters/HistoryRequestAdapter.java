package com.example.peerrequest.adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peerrequest.R;
import com.example.peerrequest.Utilities;
import com.example.peerrequest.models.Requests;
import com.example.peerrequest.models.Task;
import com.example.peerrequest.models.User;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;

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

    public class HistoryViewHolder extends RecyclerView.ViewHolder  {
        private ImageView historyProfilePicture;
        private TextView historyUsername;
        private TextView historyTaskTitle;
        private TextView historyRequestDescription;
        private TextView historyRequestStatus;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            historyProfilePicture = itemView.findViewById(R.id.historyRequestProfileImage);
            historyRequestDescription = itemView.findViewById(R.id.historyRequestDescription);
            historyUsername = itemView.findViewById(R.id.historyRequestProfileName);
            historyTaskTitle = itemView.findViewById(R.id.historyRequestTitle);
            historyRequestStatus = itemView.findViewById(R.id.historyNumberOfTasks);


        }

        public void bind(Requests request) {
            Task task = request.getTask();
            User user = task.getUser();
            historyTaskTitle.setText(task.getTaskTitle());
            historyUsername.setText(user.getUsername());
            historyRequestDescription.setText(request.getKeyCoverLetter());
            if(Objects.equals(request.getAccepted(), "true")){
                historyRequestStatus.setText(R.string.accepted_string);
                historyRequestStatus.setTextColor(Color.GREEN);
            }
            else{
                historyRequestStatus.setText(R.string.Pending);
            }

            Utilities.roundedImage(context, user.getProfilePicture().getUrl(), historyProfilePicture, 80);
        }

    }

}
