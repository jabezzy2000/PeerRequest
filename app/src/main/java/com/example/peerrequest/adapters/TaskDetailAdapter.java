package com.example.peerrequest.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peerrequest.R;
import com.example.peerrequest.Utilities;
import com.example.peerrequest.activities.TaskDetailActivity;
import com.example.peerrequest.models.Requests;
import com.parse.ParseException;
import com.parse.SaveCallback;


import java.util.List;

public class TaskDetailAdapter extends RecyclerView.Adapter<TaskDetailAdapter.TaskDetailViewHolder> {
    private  List<Requests> requests;
    Context context;
    String TAG = "TaskDetailAdapter";

    public TaskDetailAdapter(Context context, List<Requests> requests) {
        this.requests = requests;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_detail_item_task,parent,false);
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

    public class TaskDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView username;
        TextView userRating;
        TextView itemTaskTimeRequested;
        TextView itemTaskCoverLetter;


        public TaskDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.itemTaskName);
            userRating = itemView.findViewById(R.id.itemTaskRating);
            itemTaskTimeRequested = itemView.findViewById(R.id.itemTaskTime);
            itemTaskCoverLetter = itemView.findViewById(R.id.itemTaskCoverLetter);

        }

        public void bind(Requests requests) {
            username.setText(requests.getUser().getUsername());
            userRating.setText(requests.getUser().getUserRating());
            itemTaskCoverLetter.setText(requests.getKeyCoverLetter());
            itemTaskTimeRequested.setText(requests.getCreatedAt().toString());

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION){
                Requests request = requests.get(position);
                //createNewSubmitDialog
            }

        }

//        private void createNewSubmitDialog() {
//                AlertDialog.Builder dialogBuilder;
//                AlertDialog dialog;
//
//                dialogBuilder = new AlertDialog.Builder(context);
//                final View popup = getLayoutInflater().inflate(R.layout.accept_request_popup, null);
//                ImageView submitRequestProfileImage =popup.findViewById(R.id.submitRequestProfilePicture);
//                TextView submitRequestName = popup.findViewById(R.id.acceptRequestName);
//                TextView acceptRequestRating = popup.findViewById(R.id.acceptRequestRating);
//                TextView acceptRequestCoverLetter = popup.findViewById(R.id.acceptRequestCoverLetter);
//                Button acceptRequest = popup.findViewById(R.id.acceptRequestBtn);
//                Button popupCancel = popup.findViewById(R.id.cancelRequestBtn);
//                dialogBuilder.setView(popup);
//                dialog = dialogBuilder.create();
//                acceptRequest.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        request.setAccepted("true");
//                        dialog.dismiss();
//                    }
//                });
//
//                popupCancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
        }


    }

