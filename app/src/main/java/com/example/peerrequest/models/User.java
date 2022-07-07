package com.example.peerrequest.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

@ParseClassName("_User")


public class User extends ParseUser {
    public static final String KEY_PROFILE_PICTURE = "ProfilePicture";
    public static final String KEY_USER_RATING = "UserRating";
    public static final String KEY_NUMBER_OF_RATING = "numberOfRating";
    public static final String KEY_TOTAL_RATING = "totalRatings";

    public ParseFile getProfilePicture() {
        return getParseFile(KEY_PROFILE_PICTURE);
    }

    public void setProfilePicture(ParseFile image){put(KEY_PROFILE_PICTURE,image);}

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

    public String getTotalRating(){return getString(KEY_TOTAL_RATING);}


}

