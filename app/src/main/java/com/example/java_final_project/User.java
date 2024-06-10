package com.example.java_final_project;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Switch;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.LinkedList;
import java.util.Queue;

public class User {
    private static final int MAX_QUEUE_SIZE = 40;
    private final Queue<LatLng> locationQueue = new LinkedList<>();
    private final FusedLocationProviderClient fusedLocationClient;
    private final GoogleMap map;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isTracking = true;
    private Polyline polyline;
    private final Switch switchButton;

    public User(FusedLocationProviderClient fusedLocationClient, GoogleMap map, Switch switchButton) {
        this.fusedLocationClient = fusedLocationClient;
        this.map = map;
        this.switchButton = switchButton;

        if(switchButton.isChecked()) {
            startTracking();
        }
        else {
            stopTracking();
        }

        this.switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                startTracking();
            } else {
                stopTracking();
            }
        });
    }

    private void startTracking() {
        isTracking = true;
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 500)
                .setMinUpdateIntervalMillis(500)
                .setWaitForAccurateLocation(false)
                .setMaxUpdateDelayMillis(500)
                .build();

        if (ActivityCompat.checkSelfPermission(switchButton.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(switchButton.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        handler.postDelayed(trackLocationRunnable, 500);
    }

    private void stopTracking() {
        isTracking = false;
        locationQueue.clear();
        updateMapPath();
        fusedLocationClient.removeLocationUpdates(locationCallback);
        handler.removeCallbacks(trackLocationRunnable);
    }

    private final Runnable trackLocationRunnable = new Runnable() {
        @Override
        public void run() {
            if (isTracking) {
                handler.postDelayed(this, 500);
            }
        }
    };

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                if (locationQueue.size() >= MAX_QUEUE_SIZE) {
                    locationQueue.poll();
                }
                locationQueue.add(latLng);
                updateMapPath();
            }
        }
    };

    private void updateMapPath() {
        if (polyline != null) {
            polyline.remove();
        }
        PolylineOptions polylineOptions = new PolylineOptions()
                .color(0xFFFF0000)
                .width(5)
                .addAll(locationQueue);
        polyline = map.addPolyline(polylineOptions);
    }
}

