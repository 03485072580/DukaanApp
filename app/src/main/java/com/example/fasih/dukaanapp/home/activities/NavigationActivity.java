package com.example.fasih.dukaanapp.home.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.home.fragments.AdminFragment;

public class NavigationActivity extends AppCompatActivity {

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
        }
    }

}
