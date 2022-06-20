package com.example.peerrequest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SignUpCallback;

public class SignUp extends AppCompatActivity {

    EditText SignUpUsername;
    EditText SignUpPassword;
    Button BtnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        SignUpUsername = findViewById(R.id.etSignUpName);
        SignUpPassword = findViewById(R.id.etSignUpPasword);
        BtnSubmit = findViewById(R.id.btnSubmit);

        BtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                //getting text from editText
                String username = SignUpUsername.getText().toString();
                String password = SignUpPassword.getText().toString();

                user.setUsername(username); //setting the username to username inputted by user
                user.setPassword(password); // setting the password to password inputted by user

                // Invoke signUpInBackground
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            //navigate to timeline
                        } else {
                            Toast.makeText(SignUp.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                            // Sign up didn't succeed.
                        }
                    }
                });
            }
        });


    }
}