package com.example.peerrequest.models;

import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

@ParseClassName("_User")


public class User extends ParseUser {
    public static final String KEY_PROFILE_PICTURE = "ProfilePicture";
    public static final String KEY_USER_RATING = "UserRating";
    public static final String KEY_NUMBER_OF_RATING = "numberOfRating";
    public static final String KEY_TOTAL_RATING = "totalRatings";
    public static final String KEY_USER_RATINGS = "userRating";
    public static final String KEY_IS_RATING_SET = "ratingSetBool";
    public static final String KEY_USER_RATINGS_PROPERTIES = "userRatings";
    public static final String KEY_USER_CURRENT_LOCATION_LATITUDE = "userCurrentRatingLatitude";
    public static final String KEY_USER_CURRENT_LOCATION_LONGITUDE = "userCurrentRatingLongitude";
    public ParseFile getProfilePicture() {
        return getParseFile(KEY_PROFILE_PICTURE);
    }

    public void setProfilePicture(ParseFile image){put(KEY_PROFILE_PICTURE,image);}

    public void setKeyRating(String rating) {
        put(KEY_USER_RATING, rating);
    }

    public void setNumberOfRating(int rating){put(KEY_NUMBER_OF_RATING, rating);}

    public int getNumberOfRating(){return getInt(KEY_NUMBER_OF_RATING);}

    public int getKeyTotalRating(){return getInt(KEY_TOTAL_RATING);}

    public void setKeyTotalRating(int rating){put(KEY_TOTAL_RATING, rating);}

    public void setRatingSet(String ratingSet) {put(KEY_IS_RATING_SET, "true");}

    public String  getRatingSet(){return getString(KEY_IS_RATING_SET);}

    public Ratings getKeyUserRatingsProperties(){return (Ratings) getParseObject(KEY_USER_RATINGS_PROPERTIES);}

    public void setUserRatings(Ratings ratings) {put(KEY_USER_RATINGS_PROPERTIES, ratings);}

    public void setKeyUserCurrentLocationLatitude(String latitude) {put(KEY_USER_CURRENT_LOCATION_LATITUDE,latitude);}

    public String getKeyUserCurrentLocationLatitude(){return getString(KEY_USER_CURRENT_LOCATION_LATITUDE);}

    public void setKeyUserCurrentLocationLongitude(String longitude) {put(KEY_USER_CURRENT_LOCATION_LONGITUDE,longitude);}

    public String getKeyUserCurrentLocationLongitude(){return getString(KEY_USER_CURRENT_LOCATION_LONGITUDE);}









}

