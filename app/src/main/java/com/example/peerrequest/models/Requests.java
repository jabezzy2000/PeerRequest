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
    public static final String KEY_ACCEPTED = "accepted";

    public User getUser() {
        return ((User) getParseUser(KEY_USER));
    }

    public Boolean getAccepted() {
        Boolean.parseBoolean(KEY_ACCEPTED);
        return false;
    }

    public void setAccepted(String string) {
        put(KEY_ACCEPTED,Boolean.parseBoolean("True"));
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


}

