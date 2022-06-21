package com.example.peerrequest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
   BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        final FragmentManager fragmentManager = getSupportFragmentManager();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.action_Profile:
                      fragment = new ProfileFragment();
                        Toast.makeText(MainActivity.this, "profile clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_In_Progress:
                        fragment = new InProgress();
                        Toast.makeText(MainActivity.this, "in progress", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_Search:
                        fragment = new SearchFragment();
                        Toast.makeText(MainActivity.this, "search", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_Home:
                    default:
                        Toast.makeText(MainActivity.this, "home", Toast.LENGTH_SHORT).show();
                        fragment = new TimelineFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        };


    }
