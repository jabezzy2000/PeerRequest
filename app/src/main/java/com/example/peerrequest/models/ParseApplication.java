package com.example.peerrequest.models;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.peerrequest.R;
import com.example.peerrequest.models.Task;
import com.example.peerrequest.models.User;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.facebook.ParseFacebookUtils;
import com.parse.twitter.ParseTwitterUtils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


public class ParseApplication extends Application {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        ParseUser.registerSubclass(User.class);
        ParseUser.registerSubclass(Task.class);
        ParseUser.registerSubclass(Requests.class);
        ParseUser.registerSubclass(Message.class);
        ParseUser.registerSubclass(Location.class);
        ParseUser.registerSubclass(Ratings.class);
        //monitoring parse network
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        builder.networkInterceptors().add(httpLoggingInterceptor);

        // initializing parse application and registering parse models
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.parse_application_id))
                .clientKey(getString(R.string.parse_push_client_key))
                .server("https://parseapi.back4app.com")
                .build()
        );

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", getString(R.string.google_cloud_messaging_key));
        installation.saveInBackground();

        ParseFacebookUtils.initialize(this);



    }
}

