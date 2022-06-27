package com.example.peerrequest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.peerrequest.R;
import com.example.peerrequest.fragments.InProgress;
import com.example.peerrequest.fragments.ProfileFragment;
import com.example.peerrequest.fragments.SearchFragment;
import com.example.peerrequest.fragments.TimelineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    public final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment fragment;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem fragmentNavigation) {

                switch (fragmentNavigation.getItemId()) {
                    case R.id.action_Profile:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.action_In_Progress:
                        fragment = new InProgress();
                        break;
                    case R.id.action_Search:
                        fragment = new SearchFragment(MainActivity.this);
                        break;
                    case R.id.action_Home:
                    default:
                        fragment = new TimelineFragment(MainActivity.this);
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_Home); // sets default fragment to hometimeline


    }

}
