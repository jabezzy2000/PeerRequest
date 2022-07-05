package com.example.peerrequest;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.peerrequest.activities.HomeActivity;
import com.example.peerrequest.activities.TaskDetailActivity;
import com.example.peerrequest.models.Requests;
import com.example.peerrequest.models.User;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utilities extends TaskDetailActivity {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static void setImage(Context context, String url, ImageView iv) {
        Glide.with(context).load(url).into(iv);
    } // method to setImage

    public static void roundedImage(Context context, String url, ImageView iv, int radius) {
        Glide.with(context).load(url).transform(new RoundedCorners(radius)).into(iv);
    } // method to setImage with rounded corners

    public static String getSimpleTime(Date inputDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        long time = inputDate.getTime();
        long now = System.currentTimeMillis();

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static void createNewSubmitDialog(Requests request, Context context, Activity TaskDetailActivity) {
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;

        dialogBuilder = new AlertDialog.Builder(context);
        final View popup = TaskDetailActivity.getLayoutInflater().inflate(R.layout.accept_request_popup, null);
        ImageView submitRequestProfileImage = popup.findViewById(R.id.submitRequestProfilePicture);
        TextView submitRequestName = popup.findViewById(R.id.acceptRequestName);
        TextView acceptRequestRating = popup.findViewById(R.id.acceptRequestRating);
        TextView acceptRequestCoverLetter = popup.findViewById(R.id.acceptRequestCoverLetter);
        Button acceptRequest = popup.findViewById(R.id.acceptRequestBtn);
        Button popupCancel = popup.findViewById(R.id.cancelRequestBtn);
        submitRequestName.setText(request.getUser().getUsername());
        acceptRequestRating.setText(String.format("rating: %s", request.getUser().getUserRating()));
        acceptRequestCoverLetter.setText(request.getKeyCoverLetter());
        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request.setAccepted("True");
                dialog.dismiss();
                //move from here to the chat screen
            }
        });

        popupCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private static void showAlert(String title, String message, Context context) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();
                });
        android.app.AlertDialog ok = builder.create();
        ok.show();

    }




}
