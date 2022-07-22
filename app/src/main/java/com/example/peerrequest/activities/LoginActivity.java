package com.example.peerrequest.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.peerrequest.R;
import com.example.peerrequest.Utilities;
import com.example.peerrequest.models.Ratings;
import com.example.peerrequest.models.User;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.parse.ParseUser;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.parse.facebook.ParseFacebookUtils;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;


public class LoginActivity extends AppCompatActivity {
    public EditText loginUsername;
    public EditText loginPassword;
    public Button loginBtn;
    public LoginButton loginWithFacebook;
    public Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //registering a callback to handle login responses
        CallbackManager callbackManager = CallbackManager.Factory.create();
        loginUsername = findViewById(R.id.etUsername);
        loginPassword = findViewById(R.id.etPassword);
        signUpBtn = findViewById(R.id.btnSignUp);
        loginBtn = findViewById(R.id.btnLogIn);
        loginWithFacebook = findViewById(R.id.fblogin_button);
        loginWithFacebook.setReadPermissions(Arrays.asList( //setting permissions to read
                "public_profile", "email", "user_birthday", "user_friends"));
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = loginUsername.getText().toString();
                String password = loginPassword.getText().toString();
                loginUser(username, password);

            }
        });

        loginWithFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            //new registercallback
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Using GraphRequest to access token profile elements
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
                                try {
                                    String name = jsonObject.getString("name");
                                    //signing Up user with name
                                    userSignUp(name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Utilities.showAlert("Cancelled", "this was cancelled", getApplicationContext());
            }

            @Override
            public void onError(FacebookException exception) {
                Utilities.showAlert("Error", "An error occurred", getApplicationContext());
            }
        });

    }

    private void userSignUp(String name) {
        User user = new User();
        user.setUsername(name);
        user.setPassword(name);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //after sign-up, create rating set
                    createUserRating(user);
                    goMainActivity();
                } else {
                    //if there is an error because the user exists already, Log the User in
                    if (Objects.requireNonNull(e.getMessage()).contains("exists")) {
                        loginUser(name, name);
                    } else {
                        //else if the error is something else, display a dialog for the error
                        Utilities.showAlert("error", "" + e.getMessage(), LoginActivity.this);
                    }
                }
            }
        });

    }
    private void createUserRating (User user){
        //creates a new rating object for new users due to ACL issues with default user class
        Ratings userRatings = new Ratings();
        userRatings.setNumberOfRating(0);
        userRatings.setKeyRating(0.0);
        userRatings.setKeyTotalRating(0.0);
        userRatings.setKeyUserPointer(user);
        userRatings.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    user.setUserRatings(userRatings);
                    user.saveInBackground();
                }
            }
        });

    }

    private void goMainActivity () {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void loginUser (String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Utilities.showAlert("Login Invalid", "Incorrect Username/Password", LoginActivity.this);
                } else {
                    goMainActivity();
                }

            }
        });
    }

}