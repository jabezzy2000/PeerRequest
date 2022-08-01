package com.example.peerrequest.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;


@ParseClassName("Tasks")
public class Task extends ParseObject {
    public static final String KEY_USER = "UserPointer";
    public static final String KEY_REQUESTS_TITLE = "RequestsTitle";
    public static final String KEY_REQUEST_DESCRIPTION = "RequestDescription";
    public static final String KEY_COMPLETED = "taskCompleted";
    public static final String KEY_NUMBER_OF_REQUESTS = "numberOfRequests";

    public String getDescription() {
        return getString(KEY_REQUEST_DESCRIPTION);
    }

    public String getTaskTitle() {
        return getString(KEY_REQUESTS_TITLE);
    }

    public String getTaskCompleted() {
        return getString(KEY_COMPLETED);
    }

    public void setTaskCompleted(String string) {
        put(KEY_COMPLETED, "false");
    }

    public void markTaskAsComplete() {
        put(KEY_COMPLETED, "true");
    }

    public Integer getKeyNumberOfRequests(){return getInt(KEY_NUMBER_OF_REQUESTS);}

    public void increaseRequestNumber() {put(KEY_NUMBER_OF_REQUESTS, getKeyNumberOfRequests()+ 1);}

    public void setTaskTitle(String description) {
        put(KEY_REQUESTS_TITLE, description);
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
