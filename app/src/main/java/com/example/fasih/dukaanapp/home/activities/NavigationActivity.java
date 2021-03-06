package com.example.fasih.dukaanapp.home.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.home.fragments.chat.Chat_Fragment;
import com.example.fasih.dukaanapp.home.fragments.sellerPageResources.CategoryMapsFragment;
import com.example.fasih.dukaanapp.home.fragments.sellerPageResources.CategoryShopHelpFragment;
import com.example.fasih.dukaanapp.home.fragments.sellerPageResources.CategoryShopOrderFragment;
import com.example.fasih.dukaanapp.home.fragments.sellerPageResources.CategoryShopProfileFragment;
import com.example.fasih.dukaanapp.home.fragments.sellerPageResources.CategoryShopSupportFragment;
import com.example.fasih.dukaanapp.home.fragments.userPageResources.AdminFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class NavigationActivity extends AppCompatActivity {

    private static final int MAPS_PERMISSIONS_CODE = 109;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        setupNavigation(getIntent());

    }



    private void setupNavigation(Intent intent) {
        //Dynamically Replace the Fragment to the NavigationActivity Screen

        if (intent != null) {
            if (intent.hasExtra(getString(R.string.adminFragment)))
                if (intent.getStringExtra(getString(R.string.adminFragment)).equals(getString(R.string.adminFragment))) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.navigationFragmentContainer
                                    , new AdminFragment()
                                    , getString(R.string.adminFragment))
                            .commit();
                }
            if (intent.hasExtra(getString(R.string.categoryShopProfileFragment)))
                if (intent.getStringExtra(getString(R.string.categoryShopProfileFragment)).equals(getString(R.string.categoryShopProfileFragment))) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.navigationFragmentContainer
                                    , new CategoryShopProfileFragment()
                                    , getString(R.string.categoryShopProfileFragment))
                            .commitAllowingStateLoss();
                }
            if (intent.hasExtra(getString(R.string.categoryShopOrderFragment)))
                if (intent.getStringExtra(getString(R.string.categoryShopOrderFragment)).equals(getString(R.string.categoryShopOrderFragment))) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.navigationFragmentContainer
                                    , new CategoryShopOrderFragment()
                                    , getString(R.string.categoryShopOrderFragment))
                            .commitAllowingStateLoss();
                }
            if (intent.hasExtra(getString(R.string.categoryShopHelpFragment)))
                if (intent.getStringExtra(getString(R.string.categoryShopHelpFragment)).equals(getString(R.string.categoryShopHelpFragment))) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.navigationFragmentContainer
                                    , new CategoryShopHelpFragment()
                                    , getString(R.string.categoryShopHelpFragment))
                            .commitAllowingStateLoss();
                }

            if (intent.hasExtra(getString(R.string.categoryShopSupportFragment)))
                if (intent.getStringExtra(getString(R.string.categoryShopSupportFragment)).equals(getString(R.string.categoryShopSupportFragment))) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.navigationFragmentContainer
                                    , new Chat_Fragment()
                                    , getString(R.string.categoryShopSupportFragment))
                            .commitAllowingStateLoss();
                }

            if (intent.hasExtra(getString(R.string.categoryMapsFragment)))
                if (intent.getStringExtra(getString(R.string.categoryMapsFragment)).equals(getString(R.string.categoryMapsFragment))) {

                    String[] permissions = new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                    };


                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(this
                                , permissions[0]) == PackageManager.PERMISSION_GRANTED) {

                            FragmentManager manager = getSupportFragmentManager();
                            manager.beginTransaction()
                                    .replace(R.id.navigationFragmentContainer
                                            , new CategoryMapsFragment()
                                            , getString(R.string.categoryMapsFragment))
                                    .commitAllowingStateLoss();

                        } else {
                            ActivityCompat.requestPermissions(this, permissions, MAPS_PERMISSIONS_CODE);
                        }
                    } else {
                        FragmentManager manager = getSupportFragmentManager();
                        manager.beginTransaction()
                                .replace(R.id.navigationFragmentContainer
                                        , new CategoryMapsFragment()
                                        , getString(R.string.categoryMapsFragment))
                                .commitAllowingStateLoss();

                    }

                }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MAPS_PERMISSIONS_CODE) {

            if (permissions.length == 1 &&
                    permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION )
                    &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED ) {

                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.navigationFragmentContainer
                                , new CategoryMapsFragment()
                                , getString(R.string.categoryMapsFragment))
                        .commitAllowingStateLoss();

            } else {
                Toast.makeText(this, "Permissions Required for this app to work Further", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
