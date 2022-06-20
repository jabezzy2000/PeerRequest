package com.example.peerrequest;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        // initializing parse application and registering parse models
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("hUmtd9kBemW1I8liVWh4nTrHMGrEIhsINSODohCh")
                .clientKey("uSjAlaUCwE5Xea1NrATQKL720IX1cBVwjeSYpYbg")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
    }

