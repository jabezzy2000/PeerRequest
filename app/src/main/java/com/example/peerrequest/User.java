package com.example.peerrequest;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;
@ParseClassName("_User")


public class User extends ParseUser {
        public static final String KEY_PROFILE_IMAGE = "profile_image";

        public ParseFile getProfileImage() {
            return getParseFile(KEY_PROFILE_IMAGE);
        }

        public void setProfileImage(ParseFile image) {
            put(KEY_PROFILE_IMAGE, image);
        }
    }

