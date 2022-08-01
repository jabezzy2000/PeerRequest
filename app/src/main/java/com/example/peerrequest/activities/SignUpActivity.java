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
import com.example.peerrequest.Utilities;
import com.example.peerrequest.models.Ratings;
import com.example.peerrequest.models.User;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.File;
import java.io.IOException;

import okhttp3.internal.Util;

public class SignUpActivity extends AppCompatActivity {

    public EditText signUpUsername;
    public EditText signUpPassword;
    public Button btnSubmit;
    public ImageButton uploadProfilePictureBtn;
    public ImageView signUpProfilePicture;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final String photoFileName = "photo.jpg";
    public File photoFile;
    private final String fileName = "photo.jpg";
    private final String TAG = "SignUpActivity";

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

        uploadProfilePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfilePicture();
            }
        });


    }

    private void uploadProfilePicture() {
        launchCamera();
    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);
        // wrap File object into a content provider
        Uri fileProvider = FileProvider.getUriForFile(SignUpActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private void saveUserProfile() {
        User user = new User();
        ParseFile photo = new ParseFile(photoFile);
        String username = signUpUsername.getText().toString();
        String password = signUpPassword.getText().toString();
        //setting the username and password to username and password inputted by user
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //creating and linking a new Rating object upon signup
                    createUserRating(user);
                    //saving a user profile picture upon sign up
                    saveUserProfilePictureInBackground(photo, user);
                } else {
                    Utilities.showAlert("Error", e.getMessage(),getApplicationContext());
                }
            }
        });

    }

    private void createUserRating(User user) {
        Ratings userRatings = new Ratings();
        userRatings.setNumberOfRating(0);
        userRatings.setKeyRating(0.0);
        userRatings.setKeyTotalRating(0.0);
        userRatings.setKeyUserPointer(user);
        userRatings.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    user.setUserRatings(userRatings);
                    user.saveInBackground();
                }
            }
        });

    }

    private void saveUserProfilePictureInBackground(ParseFile photo, User user) {
        photo.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                user.setProfilePicture(photo);
                goMainActivity();
            }
        });
    }

    private File getPhotoFileUri(String photoFileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Utilities.showAlert("Error", "Failed to create Directory", getApplicationContext());
        }
        // Return the file target for the photo based on filename
        return (new File(mediaStorageDir.getPath() + File.separator + fileName));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP
                // Load the taken image into a preview
                signUpProfilePicture.setImageBitmap(takenImage);
            } else { // Result was a failure
                Utilities.showAlert("Error", "Picture Wasn't Taken", getApplicationContext());
            }
        }
    }

    private void goMainActivity() {
        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
        startActivity(intent);
    }

}