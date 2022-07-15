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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getApplicationContext());
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        locations = new ArrayList<>();
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
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

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
                mMap.addMarker(new MarkerOptions().position(currentLocation).title("Marker at Current Position"));
                BitmapDescriptor defaultMarker =
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                for (int i = 0; i < locations.size(); i++) {
                    Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(locations.get(i).getKeyLatitude()), Double.parseDouble(locations.get(i).getKeyLongitude()))).title(locations.get(i).getKeyTitle()).icon(defaultMarker));
                    dropPinEffect(marker);
                    Log.i(TAG, "onMapReady: " + locations.get(i));
                    marker.setTag(locations.get(i));
                }

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        com.example.peerrequest.models.Location location = (com.example.peerrequest.models.Location) marker.getTag();
                        mapsTaskDialog((com.example.peerrequest.models.Location) marker.getTag());
                        return false;
                    }
                });

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 4));


            }
        });

    }

    private void dropPinEffect(final Marker marker) {
        // Handler allows us to repeat a code block after a specified delay
        final android.os.Handler handler = new android.os.Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        // Use the bounce interpolator
        final android.view.animation.Interpolator interpolator =
                new BounceInterpolator();

        // Animate marker with a bounce updating its position every 5ms
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                // Calculate t for bounce based on elapsed time
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                // Set the anchor
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    // Post this event again 15ms from now.
                    handler.postDelayed(this, 5);
                } else { // done elapsing, show window
                    marker.showInfoWindow();
                }
            }
        });
    }


    public void mapsTaskDialog(com.example.peerrequest.models.Location location) {
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;

        dialogBuilder = new AlertDialog.Builder(this);
        final View popup = getLayoutInflater().inflate(R.layout.dialog_map_tasks, null);
        ImageView dialogImage = popup.findViewById(R.id.dialogMapProfilePicture);
        TextView name = popup.findViewById(R.id.dialogMapName);
        TextView title = popup.findViewById(R.id.dialogMapTaskTitle);
        TextView description = popup.findViewById(R.id.dialogMapTaskDescription);
        Button cancel = popup.findViewById(R.id.mapDialogCancelBtn);
        Button goToTaskDetail = popup.findViewById(R.id.mapDialogGoToTaskBtn);
        if (location.getProfilePicture() != null) {
            Utilities.roundedImage(this, location.getProfilePicture().getUrl(), dialogImage, 70);
        }
        name.setText(location.getKeyTaskLister());
        title.setText(location.getKeyTitle());
        description.setText(location.getKeyDescription());
        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        //creating a new task to pass into taskDetailActivity method
        goToTaskDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryParticularTask(location);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }

    private void queryParticularTask(com.example.peerrequest.models.Location location) {
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.whereEqualTo(Task.KEY_REQUESTS_TITLE, location.getKeyTitle());
        query.include(Task.KEY_USER);

        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> objects, ParseException e) {
                if (e == null) {
                    task = objects.get(0);
                    Intent intent = new Intent(MapsActivity.this, TaskDetailActivity.class);
                    intent.putExtra(Task.class.getSimpleName(), Parcels.wrap(task));
                    startActivity(intent);

                }
            }
        });
    }


}