package com.example.peerrequest.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

@ParseClassName("_User")


public class User extends ParseUser {
    public static final String Key_PROFILE_PICTURE = "ProfilePicture";
    public static final String KEY_USER_RATING = "UserRating";

    public ParseFile getProfilePicture() {
        return getParseFile(Key_PROFILE_PICTURE);
    }

    public String getUserRating() {
        return getString(KEY_USER_RATING);
    }

    public void setKeyRating(String rating) {
        put(KEY_USER_RATING, rating);
    }


}

