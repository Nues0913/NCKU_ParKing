package com.example.java_final_project;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Switch;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
    private final Context mapActivityContext;

    public User(FusedLocationProviderClient fusedLocationClient, GoogleMap map, Context mapActivityContext) {
        this.fusedLocationClient = fusedLocationClient;
        this.map = map;
        this.mapActivityContext = mapActivityContext;
    }

    protected void startTracking() {
        isTracking = true;
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 500)
                .setMinUpdateIntervalMillis(500)
                .setWaitForAccurateLocation(false)
                .setMaxUpdateDelayMillis(500)
                .build();

        if (ContextCompat.checkSelfPermission(mapActivityContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.v("brad", "path locationUpdates start");
        // clear all exist tasks to avoid duplicate tasks
        fusedLocationClient.removeLocationUpdates(locationCallback);
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        handler.postDelayed(trackLocationRunnable, 500);
    }

    protected void stopTracking() {
        isTracking = false;
        locationQueue.clear();
        updateMapPath();
        Log.v("brad", "path locationUpdates killed");
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
                Log.v("brad", "update path with current location");
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

