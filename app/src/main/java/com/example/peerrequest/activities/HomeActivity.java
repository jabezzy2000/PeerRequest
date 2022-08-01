package com.example.peerrequest.activities;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.peerrequest.R;
import com.example.peerrequest.Utilities;
import com.example.peerrequest.fragments.HistoryFragment;
import com.example.peerrequest.fragments.ProfileTasksFragment;
import com.example.peerrequest.fragments.SearchFragment;
import com.example.peerrequest.fragments.TimelineFragment;
import com.example.peerrequest.models.Ratings;
import com.example.peerrequest.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.facebook.login.LoginManager;

public class HomeActivity extends AppCompatActivity {
    public FragmentManager fragmentManager;
    Fragment navigationFragment;
    BottomNavigationView bottomNavigationView;
    private LocationRequest mLocationRequest; //might be wrong import
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private final long UPDATE_INTERVAL = 10 * 2000;  /* 10 secs */
    private final long FASTEST_INTERVAL = 5000; /* 5 sec */
    public double longitude;
    public double latitude;
    public Location location;
    public LatLng latLng;
    private String TAG = "HomeActivity";
    public TimelineFragment timelineFragment = new TimelineFragment();


    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    private FusedLocationProviderClient locationClient;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.messenger) {
            Intent intent = new Intent(HomeActivity.this, ChatLayoutActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startLocationUpdates();
//        User currentUser = (User) User.getCurrentUser();
//        currentUser.setKeyUserCurrentLocationLatitude(String.valueOf(location.getLatitude()));
//        currentUser.setKeyUserCurrentLocationLongitude(String.valueOf(location.getLongitude()));
//        currentUser.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e==null){
//                    Toast.makeText(HomeActivity.this, "save successfull", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Utilities.showAlert("Error", ""+e.getMessage(),getApplicationContext());
//                }
//            }
//        });
        fragmentManager = getSupportFragmentManager();
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
                        navigationFragment = new ProfileTasksFragment();
                        break;
                    case R.id.action_Search:
                        navigationFragment = new SearchFragment(HomeActivity.this);
                        break;
                    case R.id.action_History:
                        navigationFragment = new HistoryFragment();
                        break;
                    case R.id.action_Home:
                    default:
                        navigationFragment = timelineFragment;
                        break;
                }
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.flContainer, navigationFragment).commit();
                return true;
            }
        });
    }

    private void onLocationChanged(Location location) {
        setLatitude(location.getLatitude());
        setLongitude(location.getLongitude());
        Bundle bundle = new Bundle();
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("longitude", longitude);

        TimelineFragment timelineFragment = new TimelineFragment();
        timelineFragment.setArguments(bundle);
    }

    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }

        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            onLocationChanged(location);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }

    public void startLocationUpdates() {
        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        onLocationChanged(locationResult.getLastLocation());
                        location = locationResult.getLastLocation();
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                        latLng = new LatLng(latitude, longitude);
                        User currentUser = (User) User.getCurrentUser();
                        currentUser.setKeyUserCurrentLocationLatitude(String.valueOf(location.getLatitude()));
                        currentUser.setKeyUserCurrentLocationLongitude(String.valueOf(location.getLongitude()));
                        currentUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    return;
                                } else {
                                    Utilities.showAlert("Error", "" + e.getMessage(), HomeActivity.this);
                                }
                            }
                        });
                    }
                },
                Looper.myLooper());
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}


