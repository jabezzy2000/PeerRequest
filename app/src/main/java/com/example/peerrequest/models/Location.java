package com.example.peerrequest.models;

import android.app.Application;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Location")
public class Location extends ParseObject {
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_USER_POINTER = "userPointer";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_PROFILE_PICTURE = "locationProfilePicture";
    public static final String KEY_TASK_LISTER = "taskLister";
    public static final String KEY_POINTER_TO_TASK = "pointerToTask";

    public void setKeyLatitude(String latitude){put(KEY_LATITUDE, latitude);}
    public void setKeyLongitude(String longitude){put(KEY_LONGITUDE, longitude);}
    public void setKeyTitle(String title){put(KEY_TITLE, title);}
    public void setKeyUserPointer(User user){put(KEY_USER_POINTER, user);}
    public void setKeyDescription(String description){put(KEY_DESCRIPTION, description);}
    public void setKeyProfilePicture(ParseFile file){put(KEY_PROFILE_PICTURE,file);}
    public void setKeyTaskLister(String name){put(KEY_TASK_LISTER,name);}
    public String getKeyLatitude() {return getString(KEY_LATITUDE);}
    public String getKeyLongitude() {return getString(KEY_LONGITUDE);}
    public String getKeyTitle() {return getString(KEY_TITLE);}
    public String getKeyDescription() {return  getString(KEY_DESCRIPTION);}
    public User getKeyUser(){return (User) getParseUser(KEY_USER_POINTER);}
    public ParseFile getProfilePicture(){return getParseFile(KEY_PROFILE_PICTURE);}
    public String getKeyTaskLister(){return getString(KEY_TASK_LISTER);}
    public void setKeyPointerToTask(Task task){put(KEY_POINTER_TO_TASK, task);}
    public Task getTask(){return (Task) getParseObject(KEY_POINTER_TO_TASK);}
}
