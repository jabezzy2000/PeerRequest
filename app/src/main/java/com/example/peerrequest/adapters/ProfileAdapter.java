package com.example.peerrequest.adapters;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peerrequest.R;
import com.example.peerrequest.fragments.TimelineFragment;
import com.example.peerrequest.models.Task;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private final List<Task> tasks;
    Context context;
    String TAG = "ProfileAdapter";
    public AlertDialog.Builder dialogBuilder;
    public AlertDialog dialog;
    public EditText popupTaskTitle;
    public EditText popupTaskDescription;
    public Button popupSave, popupCancel;

    public ProfileAdapter(Context context, List<Task> tasks) {
        this.tasks = tasks;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_profile_item_task, parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaskTitle;
        TextView tvTaskDescription;
        TextView tvTime;
        ImageButton ibEditButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskTitle = itemView.findViewById(R.id.tvProfileTaskTitle);
            tvTaskDescription = itemView.findViewById(R.id.tvProfileTaskDescription);
            tvTime = itemView.findViewById(R.id.tvProfileTime);

//            ibEditButton.setOnClickListener(new View.OnClickListener() { // try moving to a new fragment instead
//                @Override
//                public void onClick(View v) {
////                    createNewContactDialog();
//                }
//            });
//        }
       }

        public void bind(Task task) {
            tvTaskTitle.setText(task.getTaskTitle());
            tvTaskDescription.setText(task.getDescription());
            tvTime.setText(task.getCreatedAt().toString());

        }
    }
}


