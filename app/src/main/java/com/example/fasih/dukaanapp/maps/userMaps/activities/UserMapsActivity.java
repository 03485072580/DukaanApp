package com.example.fasih.dukaanapp.maps.userMaps.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.maps.userMaps.fragments.UserLocationTrackerFragment;
import com.example.fasih.dukaanapp.order.fragments.CheckoutFragment;

public class UserMapsActivity extends AppCompatActivity {


    private UserLocationTrackerFragment userLocationTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_maps);
        setupActivityWidgets();
    }

    private void setupActivityWidgets() {

        addFragment(getIntent());
    }


    private void addFragment(Intent intent) {

        userLocationTracker = new UserLocationTrackerFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.userMapsFragmentContainer
                        , userLocationTracker
                        , getString(R.string.activity_user_maps))

                .commitAllowingStateLoss();
    }
}
