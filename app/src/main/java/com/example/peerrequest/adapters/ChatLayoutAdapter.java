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
import com.example.peerrequest.models.Message;
import com.example.peerrequest.models.User;
import com.parse.ParseException;

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageOfOther;
        TextView usernameOfOther;
        TextView lastMessageInChat;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             profileImageOfOther = itemView.findViewById(R.id.ivProfilePictureLayout);
             usernameOfOther = itemView.findViewById(R.id.tvNameLayout);
             lastMessageInChat = itemView.findViewById(R.id.tvLastMessageLayout);

        }

        public void bind(Message message) {

            User otherUser = new User();
            String isMe = User.getCurrentUser().getObjectId();
            if(message.getReceiverIdKey()==User.getCurrentUser()){
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
            lastMessageInChat.setText(message.getBody());
            Utilities.roundedImage(mContext,otherUser.getProfilePicture().getUrl(),profileImageOfOther,70);
        }
    }
}
