package com.example.fasih.dukaanapp.home.fragments.sellerPageResources;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.home.fragments.services.FetchAddressIntentService;
import com.example.fasih.dukaanapp.models.ShopProfileSettings;
import com.example.fasih.dukaanapp.utils.Constants;
import com.example.fasih.dukaanapp.utils.FirebaseMethods;
import com.example.fasih.dukaanapp.utils.GeofenceMonitoringHelper;
import com.example.fasih.dukaanapp.utils.GoogleMapsMethods;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CategoryMapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private BroadcastReceiver resultReceiver;

    //Always initialize it when Map gets ready
    private GoogleMapsMethods googleMapsMethods;

    //Firebase Stuff
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseMethods firebaseMethods;
    private String currentUserID = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_maps, container, false);
        setupFragmentWidgets();
        setupBroadcastReceiver();
        setupFirebase();
        return view;
    }

    private void setupBroadcastReceiver() {


        //Responsible for updating the DB city and
        // country fields based on shop Current location

        resultReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                if (intent == null) {
                    return;
                }

                // Display the address string
                // or an error message sent from the intent service.
                String addressLine = intent.getStringExtra(Constants.RESULT_DATA_KEY);
                if (addressLine == null) {
                    addressLine = "";
                }
//            displayAddressOutput();

                // Show a toast message if an address was found.
                if (intent.getIntExtra(Constants.RESULT_CODE, 404) == Constants.SUCCESS_RESULT) {

                    //update the DB with Current Shop Coordinates


                    fetchDBPreviousShopData(intent.getStringExtra(getString(R.string.city))
                            , intent.getStringExtra(getString(R.string.country)));
                }
            }
        };
    }

    private void fetchDBPreviousShopData(final String city, final String country) {

        final ShopProfileSettings[] shopProfileSettings = {null};

        myRef
                .child(getString(R.string.db_shop_profile_settings_node))
                .orderByChild(getString(R.string.db_field_user_id))
                .equalTo(currentUserID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot singleShot : dataSnapshot.getChildren()) {
                            Map<String, Object> map = (HashMap<String, Object>) singleShot.getValue();
                            shopProfileSettings[0] = new ShopProfileSettings();
                            shopProfileSettings[0].setUser_id((String) map.get(getString(R.string.db_field_user_id)));
                            shopProfileSettings[0].setFirst_name((String) map.get(getString(R.string.db_field_first_name)));
                            shopProfileSettings[0].setLast_name((String) map.get(getString(R.string.db_field_last_name)));
                            shopProfileSettings[0].setUser_name((String) map.get(getString(R.string.db_field_user_name)));
                            shopProfileSettings[0].setEmail((String) map.get(getString(R.string.db_field_email)));
                            shopProfileSettings[0].setScope((String) map.get(getString(R.string.db_field_scope)));
                            shopProfileSettings[0].setShop_address((String) map.get(getString(R.string.db_field_shop_address)));
                            shopProfileSettings[0].setCity(city);
                            shopProfileSettings[0].setCountry(country);
                            shopProfileSettings[0].setAdmin_approved((Boolean) map.get(getString(R.string.db_field_admin_approved)));
                            shopProfileSettings[0].setShop_category((String) map.get(getString(R.string.db_field_shop_category)));
                            shopProfileSettings[0].setProfile_image_url((String) map.get(getString(R.string.db_field_profile_image_url)));
                            shopProfileSettings[0].setMall_id((String) map.get(getString(R.string.db_field_mall_id)));

                        }
                        firebaseMethods.updateShopProfileSettingsNode(shopProfileSettings[0]);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void setupFragmentWidgets() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

    }

    private void setupGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
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


    private void setupGoogleMapsFragment() {

        SupportMapFragment mapsFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapsFragment.getMapAsync(this);
    }

    protected void startIntentService(double currentLat, double currentLng) {


        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
        intent.putExtra(getString(R.string.currentLat), currentLat);
        intent.putExtra(getString(R.string.currentLng), currentLng);
        getActivity().startService(intent);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);

        googleMapsMethods = new GoogleMapsMethods(CategoryMapsFragment.this.getActivity()
                , CategoryMapsFragment.this
                , getString(R.string.categoryMapsFragment)
                , mMap);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {


                return googleMapsMethods.getCurrentLocation();
            }
        });
        mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {

            }
        });
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        firebaseMethods = new FirebaseMethods(getActivity(), getString(R.string.categoryMapsFragment));

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    currentUserID = user.getUid();
                    setupGoogleMapsFragment();
                    setupGoogleApiClient();
                }

            }
        };


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);

//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Constants.RECEIVER);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(resultReceiver, new IntentFilter(Constants.RECEIVER));
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuth != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(resultReceiver);


    }



    public void notifyUpdateMaps(double currentLat, double currentLng) {

        startIntentService(currentLat, currentLng);

        LatLng latLng = new LatLng(currentLat, currentLng);

        LatLngBounds bounds = new LatLngBounds(latLng, latLng);
        mMap.setLatLngBoundsForCameraTarget(bounds);
        mMap.addMarker(new MarkerOptions().position(latLng));
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, 0);
        mMap.animateCamera(update);
    }

    public void notifyCreateGeofence(double currentLat, double currentLng) {

        googleMapsMethods.createGeofenceList(currentUserID, currentLat,currentLng);
        googleMapsMethods.addGeofence();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(googleMapsMethods!=null)
        googleMapsMethods.removeGeofence(GeofenceMonitoringHelper.getGeofencePendingIntent(getContext()));
    }
}
