package com.example.peerrequest.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;


@ParseClassName("requests")
public class Requests extends ParseObject {
    public static final String KEY_USER = "userPointer";
    public static final String KEY_TASK = "taskPointer";
    public static final String KEY_COVER_LETTER = "coverLetter";

    public User getUser() {
        return (User) getParseUser(KEY_USER);
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

