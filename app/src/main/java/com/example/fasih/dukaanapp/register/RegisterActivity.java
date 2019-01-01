package com.example.fasih.dukaanapp.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.fasih.dukaanapp.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupIntentResources();
    }

    private void setupIntentResources() {
        if (getIntent() != null) {
            Intent intent = getIntent();
            if (intent.hasExtra(getString(R.string.adminFragment))) {
                if (intent.getStringExtra(getString(R.string.adminFragment)).equals(getString(R.string.adminFragment))) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer
                            , new AdminRegistrationFragment()
                            , getString(R.string.adminFragment));
                    transaction.commit();

                }
            }

            if (intent.hasExtra(getString(R.string.shopFragment))) {

                if (intent.getStringExtra(getString(R.string.shopFragment)).equals(getString(R.string.shopFragment))) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer
                            , new ShopRegistrationFragment()
                            , getString(R.string.shopFragment));
                    transaction.commit();
                }
            }
            if (intent.hasExtra(getString(R.string.userFragment))) {

                if (intent.getStringExtra(getString(R.string.userFragment)).equals(getString(R.string.userFragment))) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer
                            , new UserRegistrationFragment()
                            , getString(R.string.userFragment));
                    transaction.commit();
                }
            }

        }
    }
}
