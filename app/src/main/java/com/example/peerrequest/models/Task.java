package com.example.peerrequest.models;

import com.example.peerrequest.User;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

@ParseClassName("Tasks")
public class Task extends ParseObject {
    public static final String KEY_USER = "UserPointer";
    public static final String KEY_REQUESTS = "RequestsTitle";
    public static final String KEY_REQUEST_DESCRIPTION = "RequestDescription";
    public static final Boolean KEY_COMPLETED = null;
    public static final Boolean KEY_TASK_LISTER = null;
    public static final Boolean KEY_TASK_DOER = null;

    public String getDescription() {
        return getString(KEY_REQUEST_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_REQUEST_DESCRIPTION, description);
    }

    public User getUser() {
        return (User) getParseUser(KEY_USER);
    }

    public void setUser(User user) {
        put(KEY_USER, user);
    }


}
