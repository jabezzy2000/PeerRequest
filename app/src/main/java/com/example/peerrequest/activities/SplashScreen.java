package com.example.peerrequest.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.peerrequest.R;
import com.example.peerrequest.models.User;

public class SplashScreen extends Activity {
    Handler handler;
    ImageView splashImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        splashImage = findViewById(R.id.ivSplashImage);
        Animation slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide);
        splashImage.startAnimation(slideAnimation);
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(User.getCurrentUser()!= null){
                    Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                    startActivity(intent);
                }
                else{
                Intent intent=new Intent(SplashScreen.this,LoginActivity.class);
                startActivity(intent);
                finish();}
            }
        },3000);
    }

}
