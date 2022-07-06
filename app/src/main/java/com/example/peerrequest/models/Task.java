package com.example.peerrequest.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;


@ParseClassName("Tasks")
public class Task extends ParseObject {
    public static final String KEY_USER = "UserPointer";
    public static final String KEY_REQUESTS_TITLE = "RequestsTitle";
    public static final String KEY_REQUEST_DESCRIPTION = "RequestDescription";
    public static final Boolean KEY_COMPLETED = null;
    public static final Boolean KEY_TASK_LISTER = null;
    public static final Boolean KEY_TASK_DOER = null;
    public static final String KEY_USER_RATING = "UserRating";
    public static final String KEY_NUMBER_OF_RATING = "numberOfRating";
    public static final String KEY_TOTAL_RATING = "totalRatings";

    public String getDescription() {
        return getString(KEY_REQUEST_DESCRIPTION);
    }

    public String getTaskTitle() {
        return getString(KEY_REQUESTS_TITLE);
    }

    public void setTaskCompleted() {
        put(String.valueOf(KEY_COMPLETED), true);
    }

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



}
