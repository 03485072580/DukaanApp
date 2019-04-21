package com.example.fasih.dukaanapp.utils;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.home.fragments.sellerPageResources.CategoryMapsFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class GoogleMapsMethods {

    private Context mContext;
    private Fragment currentFragment;
    private String activityOrFragmentName;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GeofencingClient geofencingClient;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<Geofence> geofenceList;
    private GoogleMap mMap;
    private android.app.Fragment currentAppFragment;

    public GoogleMapsMethods(Context mContext
            , Fragment currentFragment
            , String activityOrFragmentName
            , GoogleMap mMap) {

        this.mContext = mContext;
        this.currentFragment = currentFragment;
        this.activityOrFragmentName = activityOrFragmentName;
        this.mMap = mMap;
        this.currentAppFragment = currentAppFragment;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
        geofencingClient = LocationServices.getGeofencingClient(mContext);
        setupGoogleApiClient();
    }

    private void setupGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {

                        Log.d("TAG1234", "onConnected: ");
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                        Log.d("TAG1234", "onConnectionSuspended: ");
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        Log.d("TAG1234", "onConnectionFailed: ");
                    }
                })
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();
    }

    public Boolean getCurrentShopLocation() {

        if (ActivityCompat.checkSelfPermission(mContext
                , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext
                        , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return false;
        }
        fusedLocationProviderClient
                .getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {

                        if (task.isSuccessful()) {

                            Location currentLocation = task.getResult();
                            if (currentLocation != null) {

                                if (activityOrFragmentName.equals(mContext.getString(R.string.categoryMapsFragment))) {

                                    double currentLat = currentLocation.getLatitude();
                                    double currentLng = currentLocation.getLongitude();

                                    ((CategoryMapsFragment) currentFragment).notifyCreateGeofence(currentLat, currentLng);
                                    ((CategoryMapsFragment) currentFragment).notifyUpdateMaps(currentLat, currentLng);
                                }

                            }
                        } else {
                            //Something Went wrong
                        }
                    }
                });
        return true;
    }

    public void createGeofenceList(String currentUserID
            , double currentLat
            , double currentLng) {


        geofenceList = new ArrayList();

        geofenceList.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(currentUserID)

                .setCircularRegion(
                        currentLat,
                        currentLng,
                        Constants.GEOFENCE_RADIUS_IN_METERS
                )
//                .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

    }

    /**
     * First call createGeofenceList(...) method, then call this method
     */

    public void addGeofence() {

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        geofencingClient.addGeofences(getGeofencingRequest()
                , GeofenceMonitoringHelper.getGeofencePendingIntent(mContext))
                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
                        // Geofences added
                        // ...
                        Log.d("TAG1234", "onSuccess: addGeofence()");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add geofences
                        // ...
                        e.printStackTrace();
                        Log.d("TAG1234", "onFailure: addGeofence()");
                    }
                });


    }

    public void removeGeofence(PendingIntent pendingIntent) {

        geofencingClient.removeGeofences(pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Geofences removed
                        // ...
                        Log.d("TAG1234", "onSuccess: Removed Geofence");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to remove geofences
                        // ...
                        Log.d("TAG1234", "onFailure: Error Removing Geofence");
                    }
                });
    }

    /**
     * This example shows the use of two geofence triggers.
     * The GEOFENCE_TRANSITION_ENTER transition triggers when a device enters a geofence,
     * and the GEOFENCE_TRANSITION_EXIT transition triggers when a device exits a geofence.
     * Specifying INITIAL_TRIGGER_ENTER tells Location services that GEOFENCE_TRANSITION_ENTER
     * should be triggered if the device is already inside the geofence.
     * <p>
     * In many cases, it may be preferable to use instead INITIAL_TRIGGER_DWELL,
     * which triggers events only when the user stops for a defined duration within a geofence.
     * This approach can help reduce "alert spam" resulting from large numbers notifications
     * when a device briefly enters and exits geofences.
     * Another strategy for getting best results from your geofences is to set a minimum radius of 100 meters.
     * This helps account for the location accuracy of typical Wi-Fi networks,
     * and also helps reduce device power consumption.
     *
     * @return
     */
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }


}
