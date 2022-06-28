package com.example.peerrequest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.peerrequest.R;
import com.example.peerrequest.fragments.InProgressFragment;
import com.example.peerrequest.fragments.ProfileFragment;
import com.example.peerrequest.fragments.SearchFragment;
import com.example.peerrequest.fragments.TimelineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {
    public final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment navigationFragment;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setUpBottomNavigationView();
        bottomNavigationView.setSelectedItemId(R.id.action_Home); // sets default navigationFragment to hometimeline


    }

    private void setUpBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem fragmentNavigation) {

                switch (fragmentNavigation.getItemId()) {
                    case R.id.action_Profile:
                        navigationFragment = new ProfileFragment();
                        break;
                    case R.id.action_In_Progress:
                        navigationFragment = new InProgressFragment();
                        break;
                    case R.id.action_Search:
                        navigationFragment = new SearchFragment(HomeActivity.this);
                        break;
                    case R.id.action_Home:
                    default:
                        navigationFragment = new TimelineFragment(HomeActivity.this);
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, navigationFragment).commit();
                return true;
            }
        });
    }

}
