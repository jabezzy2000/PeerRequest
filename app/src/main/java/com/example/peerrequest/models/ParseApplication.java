package com.example.peerrequest.models;

import android.app.Application;

import com.example.peerrequest.models.Task;
import com.example.peerrequest.models.User;
import com.parse.Parse;
import com.parse.ParseUser;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseUser.registerSubclass(User.class);
        ParseUser.registerSubclass(Task.class);
        ParseUser.registerSubclass(Requests.class);

        // initializing parse application and registering parse models
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("hUmtd9kBemW1I8liVWh4nTrHMGrEIhsINSODohCh")
                .clientKey("uSjAlaUCwE5Xea1NrATQKL720IX1cBVwjeSYpYbg")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}

