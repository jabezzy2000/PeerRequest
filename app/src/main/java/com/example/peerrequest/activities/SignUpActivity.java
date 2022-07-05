package com.example.peerrequest.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.peerrequest.R;
import com.example.peerrequest.models.User;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.File;
import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {

    EditText signUpUsername;
    EditText signUpPassword;
    Button btnSubmit;
    ImageButton uploadProfilePictureBtn;
    ImageView signUpProfilePicture;
    private String TAG = "SignUpActivity";
    public ParseFile file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signUpUsername = findViewById(R.id.etSignUpName);
        signUpPassword = findViewById(R.id.etSignUpPasword);
        btnSubmit = findViewById(R.id.btnSubmit);
        signUpProfilePicture = findViewById(R.id.signUpProfilePicture);
        uploadProfilePictureBtn = findViewById(R.id.imageButton);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();

            }
        });


    }

    private void saveUserProfile() {
        User user = new User();
        //getting text from editText
        String username = signUpUsername.getText().toString();
        String password = signUpPassword.getText().toString();
        //setting the username and password to username and password inputted by user
        user.setUsername(username);
        user.setPassword(password);
        // Invoking signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    goMainActivity();
                } else {
                    Toast.makeText(SignUpActivity.this, "Something went wrong: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.getMessage());
                }
            }
        });

    }

    private void goMainActivity() {
        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
        startActivity(intent);
    }

}