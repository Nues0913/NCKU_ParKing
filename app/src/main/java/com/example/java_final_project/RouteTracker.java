package com.example.java_final_project;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

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

public class RouteTracker {
    private static final int MAX_QUEUE_SIZE = 40;
    private static final int TRACKING_INTERVAL = 500;
    private final Queue<LatLng> locationQueue = new LinkedList<>();
    private final FusedLocationProviderClient fusedLocationClient;
    private final GoogleMap map;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isTracking = true;
    private Polyline polyline;
    private final Context mapActivityContext;
    private Button btnDisco;
    private boolean discoChecked = false;
    private int currentColorIndex = 0;
    private float fraction = 0f;
    private final int[] rainbowColors = {
            0xFFFF0000, // Red
            0xFFFF7F00, // Orange
            0xFFFFFF00, // Yellow
            0xFF00FF00, // Green
            0xFF0000FF, // Blue
            0xFF4B0082, // Indigo
            0xFF8B00FF  // Violet
    };

    public RouteTracker(FusedLocationProviderClient fusedLocationClient, GoogleMap map, Context mapActivityContext, Button btnDisco) {
        this.fusedLocationClient = fusedLocationClient;
        this.map = map;
        this.mapActivityContext = mapActivityContext;
        this.btnDisco = btnDisco;
    }

    protected void startTracking() {
        isTracking = true;
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, TRACKING_INTERVAL)
                .setMinUpdateIntervalMillis(TRACKING_INTERVAL)
                .setWaitForAccurateLocation(false)
                .setMaxUpdateDelayMillis(TRACKING_INTERVAL)
                .build();

        if (ContextCompat.checkSelfPermission(mapActivityContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.v("routeTracker", "path locationUpdates start");
        // clear all exist tasks to avoid duplicate tasks
        fusedLocationClient.removeLocationUpdates(locationCallback);
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

        btnDisco.setOnClickListener(v -> {
            discoChecked = !discoChecked;

            if(discoChecked) {
                Toast toast = Toast.makeText(mapActivityContext, "disco open", Toast.LENGTH_SHORT);
                toast.show();
                handler.postDelayed(toast::cancel, 2000);
            }
            else {
                Toast toast = Toast.makeText(mapActivityContext, "disco close", Toast.LENGTH_SHORT);
                toast.show();
                handler.postDelayed(toast::cancel, 2000);
            }
        });
    }

    protected void stopTracking() {
        isTracking = false;
        locationQueue.clear();
        updateMapPath();
        Log.v("routeTracker", "path locationUpdates killed");
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }


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
                Log.v("routeTracker", "update path with current location");
                updateMapPath();
            }
        }
    };

    private void updateMapPath() {
        int color = getColor();

        if (polyline != null) {
            polyline.remove();
        }
        PolylineOptions polylineOptions = new PolylineOptions()
                .color(color)
                .width(5)
                .addAll(locationQueue);
        polyline = map.addPolyline(polylineOptions);
    }

    private int getColor() {
        if(!discoChecked) {
            return 0xFFFF0000;
        }

        int nextColorIndex = (currentColorIndex + 1) % rainbowColors.length;
        int color = interpolateColor(rainbowColors[currentColorIndex], rainbowColors[nextColorIndex], fraction);

        // update fraction
        fraction += 0.1f;
        if (fraction >= 1f) {
            fraction = 0f;
            currentColorIndex = nextColorIndex;
        }
        return color;
    }

    private int interpolateColor(int color1, int color2, float fraction) {
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int r = (int) (r1 + (r2 - r1) * fraction);
        int g = (int) (g1 + (g2 - g1) * fraction);
        int b = (int) (b1 + (b2 - b1) * fraction);

        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }
}

