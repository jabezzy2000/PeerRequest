package com.example.peerrequest.models;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.peerrequest.R;
import com.example.peerrequest.activities.MainActivity;
import com.parse.ParseException;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    EditText signUpUsername;
    EditText signUpPassword;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signUpUsername = findViewById(R.id.etSignUpName);
        signUpPassword = findViewById(R.id.etSignUpPasword);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                //getting text from editText
                String username = signUpUsername.getText().toString();
                String password = signUpPassword.getText().toString();

                user.setUsername(username); //setting the username to username inputted by user
                user.setPassword(password); // setting the password to password inputted by user

                // Invoking signUpInBackground
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignUpActivity.this, "Something went wrong: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
}