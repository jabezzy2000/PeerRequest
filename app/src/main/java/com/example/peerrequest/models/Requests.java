package com.example.peerrequest.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.facebook.login.LoginManager;
import com.parse.ParseUser;


@ParseClassName("requests")
public class Requests extends ParseObject {
    public static final String KEY_USER = "userPointer";
    public static final String KEY_TASK = "taskPointer";
    public static final String KEY_COVER_LETTER = "coverLetter";
    public static final String KEY_ACCEPTED = "acceptedStatus";
    public static final String KEY_USER_RATING = "UserRating";
    public static final String KEY_NUMBER_OF_RATING = "numberOfRating";
    public static final String KEY_TOTAL_RATING = "totalRatings";
    public static final String KEY_COMPLETED_LISTER = "completedLister";
    public static final String KEY_COMPLETED_TASKER = "completedTasker";

    public User getUser() {
        return ((User) getParseUser(KEY_USER));
    }

    public String getAccepted() {
        return getString(KEY_ACCEPTED);
    }

    public void setAccepted(String string) {
        put(KEY_ACCEPTED, "true");
    }

    public Task getTask() {
        return ((Task) getParseObject(KEY_TASK));
    }

    public void setTask(Task task) {
        put(KEY_TASK, task);
    }

    public String getKeyCoverLetter() {
        return getString(KEY_COVER_LETTER);
    }

    public void setKeyCoverLetter(String string) {
        put(KEY_COVER_LETTER, string);
    }

    public void setKeyUser(User user) {
        put(KEY_USER, user);
    }

    public String getUserRating() {
        return getString(KEY_USER_RATING);
    }

    public void setKeyRating(String rating) {
        put(KEY_USER_RATING, rating);
    }

    public void setNumberOfRating(int rating){put(KEY_NUMBER_OF_RATING, rating);}

    public int getNumberOfRating(){return getInt(KEY_NUMBER_OF_RATING);}

    public int getKeyTotalRating(){return getInt(KEY_TOTAL_RATING);}

    public void setKeyTotalRating(int rating){put(KEY_TOTAL_RATING, rating);}

    public String  getCompletedLister() {return getString(KEY_COMPLETED_LISTER);}

    public void  setCompletedLister(String string) {put(KEY_COMPLETED_LISTER,"true");}

    public String  getCompletedTasker() {return getString(KEY_COMPLETED_TASKER);}

    public void  setKeyCompletedTasker(String string) {put(KEY_COMPLETED_TASKER,"true");}





}

