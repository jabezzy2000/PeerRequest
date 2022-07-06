package com.example.peerrequest.models;

import android.app.Application;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Location")
public class Location extends ParseObject {
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_USER_POINTER = "userPointer";
    public static final String KEY_TITLE = "title";

    public void setKeyLatitude(String latitude){put(KEY_LATITUDE, latitude);}
    public void setKeyLongitude(String longitude){put(KEY_LONGITUDE, longitude);}
    public void setKeyTitle(String title){put(KEY_TITLE, title);}
    public String getKeyLatitude() {return getString(KEY_LATITUDE);}
    public String getKeyLongitude() {return getString(KEY_LONGITUDE);}
    public String getKeyTitle() {return getString(KEY_TITLE);}
    public void setKeyUserPointer(User user){put(KEY_USER_POINTER,user);}
}
