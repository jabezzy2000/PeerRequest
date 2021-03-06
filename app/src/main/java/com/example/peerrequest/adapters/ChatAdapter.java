package com.example.peerrequest.adapters;

import android.content.Context;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.example.peerrequest.R;
import com.example.peerrequest.Utilities;
import com.example.peerrequest.models.Message;

import com.example.peerrequest.models.User;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<Message> mMessages;
    private Context mContext;
    private User otherUser;

    public ChatAdapter(Context context, List<Message> messages) {
        mMessages = messages;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_chat, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = mMessages.get(position);
        //creating boolean value to check if message sender is current user to set view holder
        final boolean isMe = message.getSenderIdKey() != null && message.getSenderIdKey().getUsername().equals(User.getCurrentUser().getUsername());
        if (message.getReceiverIdKey().getUsername().equals(User.getCurrentUser().getUsername())) {
            otherUser = message.getSenderIdKey();
        } else {
            otherUser = message.getReceiverIdKey();
        }


        if (isMe) {
            holder.bodyOther.setVisibility(View.INVISIBLE);
            holder.imageMe.setVisibility(View.VISIBLE);
            holder.imageOther.setVisibility(View.INVISIBLE);
            holder.bodyMe.setVisibility(View.VISIBLE);
            holder.bodyMe.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        } else {
            holder.bodyMe.setVisibility(View.INVISIBLE);
            holder.imageOther.setVisibility(View.VISIBLE);
            holder.imageMe.setVisibility(View.INVISIBLE);
            holder.bodyOther.setVisibility(View.VISIBLE);
            holder.bodyOther.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        }

        //if the other User doesnt have a profile picture, His profile picture is replaced with a gravatar
        final ImageView profileView = isMe ? holder.imageMe : holder.imageOther;
        if (isMe) {
            User meUser = (User) User.getCurrentUser();
            Utilities.roundedImage(mContext, meUser.getProfilePicture().getUrl(), profileView, 70);
            holder.bodyMe.setText(message.getBody());
        } else {
            if (otherUser.getProfilePicture() != null) {
                Utilities.roundedImage(mContext, otherUser.getProfilePicture().getUrl(), profileView, 70);
            } else {
                //get gravatar image if user doesn't have an image on parse
                Utilities.roundedImage(mContext, Utilities.getProfileUrl(message.getUserId()), profileView, 70);
            }
            holder.bodyOther.setText(message.getBody());
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageOther;
        ImageView imageMe;
        TextView bodyMe;
        TextView bodyOther;

        public ViewHolder(View itemView) {
            super(itemView);
            imageOther = (ImageView) itemView.findViewById(R.id.ivProfileOther);
            imageMe = (ImageView) itemView.findViewById(R.id.ivProfileMe);
            bodyMe = (TextView) itemView.findViewById(R.id.tvBodyMe);
            bodyOther = (TextView) itemView.findViewById(R.id.tvBodyOtherUser);
        }
    }
}

