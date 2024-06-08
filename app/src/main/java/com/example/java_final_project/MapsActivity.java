package com.example.java_final_project;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import android.Manifest.permission;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMyLocationButtonClickListener,
        OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    /**
     * Flag indicating whether a requested permission has been denied after returning in {@link
     * #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean permissionDenied = false;
    private GoogleMap map;
    /**
     * Used for providing the current location
     */
    private FusedLocationProviderClient fusedLocationClient;
    /**
     * A callback for receiving locations from the FusedLocationProviderClient.
     */
    private LocationCallback locationCallback;
    /**
     * False to disable the startLocationUpdates function
     */
    private boolean isStartLocationUpdates = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    map.animateCamera(CameraUpdateFactory.newLatLng(currentLocation));
                }
            }
        };
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        LatLng NCKUCSIE = new LatLng(22.997292518755387, 120.22107402743946);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(NCKUCSIE, 17));
        map.setOnMyLocationButtonClickListener(this);
        map.setOnMyLocationClickListener(this);
        map.addMarker(new MarkerOptions().position(NCKUCSIE).title("Marker in NCKUCSIE"));
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);
        enableMyLocation();


//        // Create a LatLngBounds that includes the city of Tainan in NCKU.
//        LatLngBounds adelaideBounds = new LatLngBounds(
//                new LatLng(22.991645865499702, 120.2105142391294), // SW bounds
//                new LatLng(23.00447918115659, 120.22771142196524)  // NE bounds
//        );
//        // Constrain the camera target to the Adelaide bounds.
//        mMap.setLatLngBoundsForCameraTarget(adelaideBounds);
    }

    /**
     * start keeping locating current location and moving camera
     */
    private void startLocationUpdates() {
        if(!isStartLocationUpdates){
            return;
        }
        // 1000 millis for 1 second
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMinUpdateIntervalMillis(10000)
                .setWaitForAccurateLocation(false)
                .setMaxUpdateDelayMillis(10000)
                .build();
        // check the permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    /**
     * Enables the myLocation-Related functions if the fine location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // Check if permissions are granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            return;
        }
        // Asked for permissions
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    /**
     *  Dealing with the result of asking for permissions
     *
     * @param requestCode The request code passed in requestPermissions(
     * android.app.Activity, String[], int)
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        boolean isPermissionGranted = false;
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
        for (int i = 0; i < permissions.length; i++) {
            if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[i])) {
                isPermissionGranted = (grantResults[i] == PackageManager.PERMISSION_GRANTED);
            }
        }
        for (int i = 0; i < permissions.length; i++) {
            if (Manifest.permission.ACCESS_COARSE_LOCATION.equals(permissions[i])) {
                isPermissionGranted = (grantResults[i] == PackageManager.PERMISSION_GRANTED);
            }
        }
        if (isPermissionGranted) {
            enableMyLocation();
        } else {
            permissionDenied = true;
        }
        startLocationUpdates();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    /**
     * Toasts the MissingPermissionError message and finished the the mapActivity back to mainActivity.
     */
    private void showMissingPermissionError() {
        Toast.makeText(this, "locationPermissionDenied\nsets manually in settings", Toast.LENGTH_LONG)
                .show();
        finish();

    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG)
                .show();
    }

    /**
     * The camera will animates to the user's current position.
     *
     * @return false
     */
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT)
                .show();
        map.animateCamera(CameraUpdateFactory.zoomTo(17));
        // Return false so that we don't consume the event and the default behavior still occurs
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        }
    }

}