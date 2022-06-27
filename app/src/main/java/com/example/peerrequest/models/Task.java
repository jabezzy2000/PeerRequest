package com.example.peerrequest.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

//@Parcel
@ParseClassName("Tasks")
public class Task extends ParseObject {
    public static final String KEY_USER = "UserPointer";
    public static final String KEY_REQUESTS_TITLE = "RequestsTitle";
    public static final String KEY_REQUEST_DESCRIPTION = "RequestDescription";
    public static final Boolean KEY_COMPLETED = null;
    public static final Boolean KEY_TASK_LISTER = null;
    public static final Boolean KEY_TASK_DOER = null;

    public String getDescription() {
        return getString(KEY_REQUEST_DESCRIPTION);
    }

    public String getTaskTitle() {
        return getString(KEY_REQUESTS_TITLE);
    }

    public void setTaskCompleted() {
        put(String.valueOf(KEY_COMPLETED), true);
    } //come back to this

    public void setTaskTitle(String description) {
        put(KEY_REQUESTS_TITLE, description);
    }

    public void setDescription(String description) {
        put(KEY_REQUEST_DESCRIPTION, description);
    }

    public User getUser() {
        return (User)getParseUser(KEY_USER);
    }

    public void setUser(User user) {
        put(KEY_USER, user);
    }


}
