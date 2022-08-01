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
import com.example.peerrequest.activities.ChatActivity;
import com.example.peerrequest.activities.ChatLayoutActivity;
import com.example.peerrequest.models.Message;
import com.example.peerrequest.models.User;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class ChatLayoutAdapter extends RecyclerView.Adapter<ChatLayoutAdapter.ViewHolder> {
    private List<Message> mMessages;
    private Context mContext;
    private String mUserId;
    private User otherUser;

    public ChatLayoutAdapter(Context context, String userId, List<Message> messages) {
        mMessages = messages;
        this.mUserId = userId;
        mContext = context;
    }

    @NonNull
    @Override
    public ChatLayoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contactView = inflater.inflate(R.layout.item_chat_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatLayoutAdapter.ViewHolder holder, int position) {
        Message message = mMessages.get(position);
        holder.bind(message);



    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView profileImageOfOther;
        TextView usernameOfOther;
        TextView lastMessageInChat;
        TextView chatTaskTitle;
        TextView completedText;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             profileImageOfOther = itemView.findViewById(R.id.ivProfilePictureLayout);
             usernameOfOther = itemView.findViewById(R.id.tvNameLayout);
             lastMessageInChat = itemView.findViewById(R.id.tvLastMessageLayout);
             chatTaskTitle = itemView.findViewById(R.id.chatLayoutTaskTitle);
             completedText = itemView.findViewById(R.id.chatLayoutCompleted);
             itemView.setOnClickListener(this);

        }

        public void bind(Message message) {

            User otherUser = new User();
            String isMe = User.getCurrentUser().getObjectId();
            if(message.getReceiverIdKey().getUsername().equals(User.getCurrentUser().getUsername())){
                try{
                    otherUser = (User) message.getSenderIdKey().fetchIfNeeded();
                } catch (ParseException e) {

                }
            }
            else{
                try{
                    otherUser = (User) message.getReceiverIdKey().fetchIfNeeded();
                } catch (ParseException e) {

                }
            }
            usernameOfOther.setText(otherUser.getUsername());
            chatTaskTitle.setText(message.getTaskIdPointer().getTaskTitle());
            lastMessageInChat.setText(message.getBody());
            if(message.getTaskIdPointer().getTaskCompleted().equals("true")){
                completedText.setVisibility(View.VISIBLE);
            }
            else{
                completedText.setVisibility(View.INVISIBLE);
            }
            Utilities.roundedImage(mContext,otherUser.getProfilePicture().getUrl(),profileImageOfOther,100);
        }

        @Override
        public void onClick(View v) { // moving from layout to chat upon click
            //including the other user to stratify messages according to both users
            Intent intent = new Intent(mContext, ChatActivity.class);
            User otherUser;
            String userId = ParseUser.getCurrentUser().getObjectId().toString();
            String messageSenderId = mMessages.get(getAdapterPosition()).getSenderIdKey().getObjectId();
            if (userId.equals(messageSenderId))
            {
                otherUser = mMessages.get(getAdapterPosition()).getReceiverIdKey();
            } else {
                otherUser = mMessages.get(getAdapterPosition()).getSenderIdKey();
            }
            intent.putExtra("task", Parcels.wrap(mMessages.get(getAdapterPosition()).getTaskIdPointer()));
            intent.putExtra("otherUser", otherUser);
            mContext.startActivity(intent);
        }
    }
}
