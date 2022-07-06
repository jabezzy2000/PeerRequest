package com.example.peerrequest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.peerrequest.R;
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
import com.parse.facebook.ParseFacebookUtils;
import org.json.JSONException;
import java.util.Arrays;
import java.util.Collection;


public class LoginActivity extends AppCompatActivity {
    public final String TAG = "LoginActivity";
    public EditText loginUsername;
    public EditText loginPassword;
    public Button loginBtn;
    public Button loginWithFacebook;
    public Button signUpBtn;
    public Button checkChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginUsername = findViewById(R.id.etUsername);
        loginPassword = findViewById(R.id.etPassword);
        signUpBtn = findViewById(R.id.btnSignUp);
        loginBtn = findViewById(R.id.btnLogIn);
        loginWithFacebook = findViewById(R.id.logInFB);
        checkChat = findViewById(R.id.checkChat);
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

        checkChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ChatActivity.class);
                startActivity(intent);
            }
        });
    }



    private void loginUser(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    return;
                }
                goMainActivity();
            }
        });
    }

    private void goMainActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

}