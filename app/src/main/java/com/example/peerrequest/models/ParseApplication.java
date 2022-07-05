package com.example.peerrequest.models;

import android.app.Application;

import com.example.peerrequest.models.Task;
import com.example.peerrequest.models.User;
import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.facebook.ParseFacebookUtils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseUser.registerSubclass(User.class);
        ParseUser.registerSubclass(Task.class);
        ParseUser.registerSubclass(Requests.class);
        ParseUser.registerSubclass(Message.class);
        //monitoring parse network
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        builder.networkInterceptors().add(httpLoggingInterceptor);

        // initializing parse application and registering parse models
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("hUmtd9kBemW1I8liVWh4nTrHMGrEIhsINSODohCh")
                .clientKey("uSjAlaUCwE5Xea1NrATQKL720IX1cBVwjeSYpYbg")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}

