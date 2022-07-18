package com.example.peerrequest;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.peerrequest.activities.ChatActivity;
import com.example.peerrequest.activities.HomeActivity;
import com.example.peerrequest.activities.MapsActivity;
import com.example.peerrequest.activities.TaskDetailActivity;
import com.example.peerrequest.models.Location;
import com.example.peerrequest.models.Message;
import com.example.peerrequest.models.Requests;
import com.example.peerrequest.models.Task;
import com.example.peerrequest.models.User;
import com.google.android.gms.maps.model.Marker;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utilities extends TaskDetailActivity {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    static LocationManager locationManager;
    public static final int REQUEST_LOCATION = 1;

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

    public static void createNewSubmitDialog(Requests request, Context context, Activity TaskDetailActivity, User user, String rating) {
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;

        dialogBuilder = new AlertDialog.Builder(context);
        final View popup = TaskDetailActivity.getLayoutInflater().inflate(R.layout.accept_request_popup, null);
        User requester = request.getUser();
        ImageView submitRequestProfileImage = popup.findViewById(R.id.submitRequestProfilePicture);
        TextView submitRequestName = popup.findViewById(R.id.acceptRequestName);
        TextView acceptRequestRating = popup.findViewById(R.id.acceptRequestRating);
        TextView acceptRequestCoverLetter = popup.findViewById(R.id.acceptRequestCoverLetter);
        Button acceptRequest = popup.findViewById(R.id.acceptRequestBtn);
        Button popupCancel = popup.findViewById(R.id.cancelRequestBtn);
        submitRequestName.setText(request.getUser().getUsername());
        acceptRequestRating.setText(rating);
        Utilities.roundedImage(context,requester.getProfilePicture().getUrl(),submitRequestProfileImage,70);
        acceptRequestCoverLetter.setText(request.getKeyCoverLetter());
        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request.setAccepted("True");
                request.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            dialog.dismiss();
                            //move from here to the chat screen
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra("request", Parcels.wrap(request));
                            intent.putExtra("user", Parcels.wrap(user));
                            intent.putExtra("requester", Parcels.wrap(requester));
                            context.startActivity(intent);
                        }
                        else{
                            Utilities.showAlert("Error", "" + e.getMessage(), context);
                        }
                    }
                });

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

    public static void showAlert(String title, String message, Context context) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();
                });
        android.app.AlertDialog ok = builder.create();
        ok.show();

    }


    public static void dropPinEffect(final Marker marker) {
        // Handler allows us to repeat a code block after a specified delay
        final android.os.Handler handler = new android.os.Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        // Use the bounce interpolator
        final android.view.animation.Interpolator interpolator =
                new BounceInterpolator();

        // Animate marker with a bounce updating its position every 5ms
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                // Calculate t for bounce based on elapsed time
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                // Set the anchor
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    // Post this event again 15ms from now.
                    handler.postDelayed(this, 5);
                } else { // done elapsing, show window
                    marker.showInfoWindow();
                }
            }
        });
    }

    private static void queryParticularTask(Location location,Context context, Activity MapsActivity) {
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.whereEqualTo(Task.KEY_REQUESTS_TITLE, location.getKeyTitle());
        query.include(Task.KEY_USER);

        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> objects, ParseException e) {
                if (e==null){
                    Task task = objects.get(0);
                    Intent intent = new Intent(context, com.example.peerrequest.activities.MapsActivity.class);
                    intent.putExtra(Task.class.getSimpleName(), Parcels.wrap(task));
                    context.startActivity(intent);

                }
            }
        });
    }



    public static void createAddTaskDialog(Context context, Activity MainActivity, HomeActivity homeActivity) {
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;

        dialogBuilder = new AlertDialog.Builder(context);
        final View popup = MainActivity.getLayoutInflater().inflate(R.layout.popup, null);
        TextView popupTaskTitle = popup.findViewById(R.id.taskTitle);
        TextView popupTaskDescription = popup.findViewById(R.id.taskDescription);
        Button popupSave = popup.findViewById(R.id.btnOk);
        Button popupCancel = popup.findViewById(R.id.btnCancel);
        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        popupSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();
                String title = popupTaskTitle.getText().toString();
                String description = popupTaskDescription.getText().toString();

                task.setUser((User) ParseUser.getCurrentUser());
                task.setTaskTitle(title);
                task.setDescription(description);
                task.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Utilities.showAlert("Error", "" + e.getMessage(), context);
                        } else {
                            dialog.dismiss();
//                           HomeActivity homeActivity = (HomeActivity) MainActivity();
                            User user = (User) User.getCurrentUser();
                            Location location = new Location();
                            location.setKeyTaskLister(user.getUsername());
                            location.setKeyProfilePicture(user.getProfilePicture());
                            location.setKeyUserPointer(user);
                            location.setKeyLongitude(homeActivity.getLongitude() + "");
                            location.setKeyLatitude(homeActivity.getLatitude() + "");
                            location.setKeyTitle(title);
                            location.setKeyDescription(description);
                            location.setKeyPointerToTask(task);
                            location.saveInBackground();
                        }
                    }
                });
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

}
