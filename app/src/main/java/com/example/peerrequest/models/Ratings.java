package com.example.peerrequest.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Ratings")
public class Ratings extends ParseObject {
    public static final String KEY_USER_RATING = "userRating";
    public static final String KEY_NUMBER_OF_RATING = "numberOfRating";
    public static final String KEY_TOTAL_RATING = "totalRatings";
    public static final String KEY_USER_POINTER = "pointerToUser";

    public Double getUserRating() {
        return getDouble((KEY_USER_RATING));
    }

    public void setKeyRating(Double rating) {
        put(KEY_USER_RATING, rating);
    }

    public void setNumberOfRating(double rating){put(KEY_NUMBER_OF_RATING, rating);}

    public double getNumberOfRating(){return getInt(KEY_NUMBER_OF_RATING);}

    public double getKeyTotalRating(){return getDouble(KEY_TOTAL_RATING);}

    public void setKeyTotalRating(Double rating){put(KEY_TOTAL_RATING, rating);}

    public void setKeyUserPointer(User user){put(KEY_USER_POINTER,user);}

    public User getKeyUserPointer(){return (User) getParseUser(KEY_USER_POINTER);}
}
