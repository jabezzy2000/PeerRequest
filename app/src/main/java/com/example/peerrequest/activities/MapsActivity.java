package com.example.peerrequest.activities;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peerrequest.R;
import com.example.peerrequest.Utilities;
import com.example.peerrequest.models.Task;
import com.example.peerrequest.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.peerrequest.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.internal.Util;


public class MapsActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LocationRequest mLocationRequest;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private final long UPDATE_INTERVAL = 1000000 * 2000;
    private final long FASTEST_INTERVAL = 50000000;
    public double latitude;
    public double longitude;
    public static LatLng currentLocation;
    private String TAG = "MapsActivity";
    public Task task;
    public List<com.example.peerrequest.models.Location> locations;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView bottomSheetTaskTitle;
    private TextView bottomSheetUsername;
    private ImageView bottomSheetProfilePicture;
    private TextView bottomSheetTaskDescription;
    private TextView bottomSheetTime;
    private Button bottomSheetGoToTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getApplicationContext());
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        locations = new ArrayList<>();
        View bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetTaskTitle = findViewById(R.id.bottom_sheet_task_title);
        bottomSheetUsername = findViewById(R.id.bottom_sheet_name);
        bottomSheetProfilePicture = findViewById(R.id.bottom_sheet_profile_picture);
        bottomSheetTaskDescription = findViewById(R.id.bottomSheetDescription);
        bottomSheetTime = findViewById(R.id.bottomSheetTime);
        bottomSheetGoToTask = findViewById(R.id.bottomSheetGoToTask);
        bottomSheetTaskTitle.setText("No Task Selected");
        bottomSheetTaskDescription.setText("No Task Selected");
        User currentUser = (User) User.getCurrentUser();
        if(currentUser.getProfilePicture()!=null) { //null check for profile image
            Utilities.roundedImage(getApplicationContext(), currentUser.getProfilePicture().getUrl(), bottomSheetProfilePicture, 80);
        }
        bottomSheetUsername.setText(User.getCurrentUser().getUsername());
        ParseQuery<com.example.peerrequest.models.Location> locationParseQuery = new ParseQuery(com.example.peerrequest.models.Location.class);
        locationParseQuery.findInBackground(new FindCallback<com.example.peerrequest.models.Location>() {
            @Override
            public void done(List<com.example.peerrequest.models.Location> objects, ParseException e) {
                locations.addAll(objects);
                startLocationUpdates();
            }
        });


    }


    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // Trigger new location updates at interval
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
                    }
                },
                Looper.myLooper());
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

    // sets map location in the situation the users location has changed
    private void onLocationChanged(Location location) {
        setLatitude(location.getLatitude());
        setLongitude(location.getLongitude());
        setMapToLocation();
    }

    public void requestPermission() { // requesting permission
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION_PERMISSION);
    }


    private void setMapToLocation() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;
                //creating marker at current position
                currentLocation = new LatLng(latitude, longitude);
                Log.i(TAG, "onMapReady: current location" + currentLocation);
                Marker currentLocationMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("Marker at Current Position"));
                for (int i = 0; i < locations.size(); i++) {
                    //Iterating through queried list and passing each list item through addMarkerToMap method to create various markers for each task
                    //Used path because there is a built in class called Location by Google which has a name conflict with location class on Parse
                    if (locations.get(i) != null) {
                        latitude = Double.parseDouble(locations.get(i).getKeyLatitude());
                        longitude = Double.parseDouble(locations.get(i).getKeyLongitude());
                        com.example.peerrequest.models.Location locationObj = locations.get(i);
                        String title = locations.get(i).getKeyTitle();
                        addMarkerToMap(latitude, longitude, title, locationObj);
                    } else {
                        Utilities.showAlert("Error", "An Error Has Occurred", getApplicationContext());
                    }
                }

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        //Adding if statement to handle the situation the user clicks on his location marker
                        if (!marker.getPosition().equals(currentLocationMarker.getPosition())) {
                            com.example.peerrequest.models.Location location = (com.example.peerrequest.models.Location) marker.getTag();
                            bottomSheetTaskTitle.setText(location.getKeyTitle());
                            bottomSheetUsername.setText(location.getKeyTaskLister());
                            bottomSheetTaskDescription.setText(location.getKeyDescription());
                            Utilities.getSimpleTime(location.getUpdatedAt());
                            bottomSheetGoToTask.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    queryParticularTask(location);
                                }
                            });
                            if (location.getProfilePicture() != null) {
                                Utilities.roundedImage(getApplicationContext(), location.getProfilePicture().getUrl(), bottomSheetProfilePicture, 80);
                            }

                        }
                        return false;
                    }
                });

                //moves camera to location with a zoom of 5
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 5));


            }
        });

    }

    private void addMarkerToMap(double latitude, double longitude, String title, com.example.peerrequest.models.Location locationObj) {
        BitmapDescriptor defaultMarker =
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(title).icon(defaultMarker));
        Utilities.dropPinEffect(marker);
        marker.setTag(locationObj);

    }

    //queries information about task
    private void queryParticularTask(com.example.peerrequest.models.Location location) {
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.whereEqualTo(Task.KEY_REQUESTS_TITLE, location.getKeyTitle());
        query.include(Task.KEY_USER);

        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> objects, ParseException e) {
                if (e == null) {
                    task = objects.get(0);
                    goToTaskDetail(task);
                }
                else{
                    Utilities.showAlert("Error", ""+e.getMessage(),getApplicationContext());
                }
            }
        });
    }

    // sends object to task detail page
    private void goToTaskDetail(Task task) {
        Intent intent = new Intent(MapsActivity.this, TaskDetailActivity.class);
        intent.putExtra(Task.class.getSimpleName(), Parcels.wrap(task));
        startActivity(intent);
    }
}