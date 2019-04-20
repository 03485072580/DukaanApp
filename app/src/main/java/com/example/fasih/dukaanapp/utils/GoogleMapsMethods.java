package com.example.fasih.dukaanapp.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.home.activities.NavigationActivity;
import com.example.fasih.dukaanapp.home.fragments.sellerPageResources.CategoryMapsFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
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

    public GoogleMapsMethods(Context mContext
            , Fragment currentFragment
            , String activityOrFragmentName
            , GoogleMap mMap) {

        this.mContext = mContext;
        this.currentFragment = currentFragment;
        this.activityOrFragmentName = activityOrFragmentName;
        this.mMap = mMap;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
        geofencingClient = LocationServices.getGeofencingClient(mContext);
        setupGoogleApiClient();
    }

    private void setupGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {

                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

                                if(activityOrFragmentName.equals(mContext.getString(R.string.categoryMapsFragment))){

                                    double currentLat = currentLocation.getLatitude();
                                    double currentLng = currentLocation.getLongitude();
                                    ((CategoryMapsFragment)currentFragment).notifyUpdateMaps(currentLat, currentLng);
                                }

                            }
                        } else {
                            //Something Went wrong
                        }
                    }
                });
        return true;
    }

    public void createGeofence(){
//        geofenceList = new ArrayList();
//
//        geofenceList.add(new Geofence.Builder()
//                // Set the request ID of the geofence. This is a string to identify this
//                // geofence.
//                .setRequestId(entry.getKey())
//
//                .setCircularRegion(
//                        entry.getValue().latitude,
//                        entry.getValue().longitude,
//                        Constants.GEOFENCE_RADIUS_IN_METERS
//                )
//                .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
//                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
//                        Geofence.GEOFENCE_TRANSITION_EXIT)
//                .build());
    }

}
