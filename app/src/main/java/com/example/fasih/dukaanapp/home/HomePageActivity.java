package com.example.fasih.dukaanapp.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.adapter.DrawerHandler;
import com.example.fasih.dukaanapp.adapter.NavigationHandler;
import com.example.fasih.dukaanapp.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePageActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private FirebaseAuth.AuthStateListener authStateStateListener;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_home);
        setupActivityWidgets();
        setupToolbar();
        setupFirebase();
    }

    private void setupToolbar() {
        try {
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger);
        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }
    }


    private void setupActivityWidgets() {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.home_nav_view);

        //Listener's
        navigationView.setNavigationItemSelectedListener(new NavigationHandler(this));
        drawerLayout.addDrawerListener(new DrawerHandler(this));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        authStateStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    //Stay Here
                    userID = currentUser.getUid();
                } else {
                    //Navigate to the Login Screen
                    Intent intent = new Intent(HomePageActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth != null)
            mAuth.addAuthStateListener(authStateStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuth != null) {
            mAuth.removeAuthStateListener(authStateStateListener);
        }
    }
}
