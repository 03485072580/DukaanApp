package com.example.fasih.dukaanapp.categories.actvities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.categories.fragments.CarsFragment;
import com.example.fasih.dukaanapp.categories.fragments.MobileFragment;

public class UniqueCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unique_category);
        // setupIntentResources();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, new MobileFragment(), getString(R.string.mobileFragment));
        transaction.commit();

    }

    private void setupIntentResources() {
        if (getIntent() != null) {
            Intent intent = getIntent();
            if (intent.hasExtra(getString(R.string.carsFragment))) {
                if (intent.getStringExtra(getString(R.string.carsFragment)).equals(getString(R.string.carsFragment))) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer
                            , new CarsFragment()
                            , getString(R.string.carsFragment));
                    transaction.commit();

                }
            }

            if (intent.hasExtra(getString(R.string.mobileFragment))) {
                if (intent.getStringExtra(getString(R.string.mobileFragment)).equals(getString(R.string.mobileFragment))) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer
                            , new MobileFragment()
                            , getString(R.string.mobileFragment));
                    transaction.commit();

                }
            }
        }
    }
}
