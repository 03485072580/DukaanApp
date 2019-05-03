package com.example.fasih.dukaanapp.home.activities;


import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.home.fragments.chat.Chat_Fragment;
import com.example.fasih.dukaanapp.home.fragments.userPageResources.HomeFragment;
import com.example.fasih.dukaanapp.home.fragments.userPageResources.UserCartFragment;
import com.example.fasih.dukaanapp.home.fragments.userPageResources.UserOrderFragment;
import com.example.fasih.dukaanapp.login.activity.LoginActivity;
import com.example.fasih.dukaanapp.models.FCMTokens;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserHomePageActivity extends AppCompatActivity {

    private BroadcastReceiver myFcmTokenReceiver;
    private FirebaseAuth mAuth;
    //Firebase Stuff
    private FirebaseAuth.AuthStateListener authStateStateListener;
    private DatabaseReference myRef;
    private String userID;

    ArrayList<String> mylist;
    BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceSupportFragment(new HomeFragment(), getString(R.string.homeFragment));
                    return true;
                case R.id.navigation_notifications:
                    replaceSupportFragment(new Chat_Fragment(), getString(R.string.chatFragment));
                    return true;
                case R.id.cart:
                    replaceSupportFragment(new UserCartFragment(), getString(R.string.userLocationTrackerFragment));
                    return true;
                case R.id.userOrders:
                    replaceSupportFragment(new UserOrderFragment(), getString(R.string.userOrderFragment));
                    return true;
            }
            return false;
        }
    };

    private void replaceSupportFragment(Fragment targetFragment
            , String name) {
        mylist.add(name);


        View view = this.getCurrentFocus();
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        if (view != null) {

            InputMethodManager inputManager = (InputMethodManager)
                    this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken()
                    , InputMethodManager.HIDE_NOT_ALWAYS);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        fm.beginTransaction()
                .replace(R.id.container, targetFragment, name)
                .addToBackStack(name)
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_home);
        setupFirebase();
        mylist = new ArrayList<String>();
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        replaceSupportFragment(new HomeFragment(), getString(R.string.homeFragment));

    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        authStateStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    //Stay Here
                    userID = currentUser.getUid();
                    SharedPreferences preferences = getSharedPreferences(getString(R.string.myPreferences), MODE_PRIVATE);
                    String fcm_token = preferences.getString(getString(R.string.fcm_token), "");
                    if (!fcm_token.equals("")) {
                        sendTokenToServer(fcm_token,userID);
                    }

                } else {
//                    Navigate to the Login Screen
                    Intent intent = new Intent(UserHomePageActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

    }

    private void sendTokenToServer(String fcm_token, String currentUserID) {

        myRef
                .child(getString(R.string.db_FCMToken_node))
                .child(currentUserID)
                .setValue(new FCMTokens(currentUserID,fcm_token));
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

    public static class MyCustomFcmTokenReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() != null)
                if (intent.getAction().equals("com.example.fasih.dukaanapp_FCM_REGISTRATION_TOKEN")) {
                    SharedPreferences preferences = context
                            .getSharedPreferences(context.getString(R.string.myPreferences)
                                    , MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(context.getString(R.string.fcm_token)
                            , intent.getStringExtra(context.getString(R.string.fcm_token)));
                    editor.apply();
                    Log.d("TAG1234", "onReceive: " + intent.getStringExtra(context.getString(R.string.fcm_token)));
                }
        }
    }

}


//    private void setupViewPager(int currentFragmentNumber) throws NullPointerException {
//        SectionsPagerStateAdapter adapter = new SectionsPagerStateAdapter(getSupportFragmentManager());
//        adapter.setFragment(new HomeFragment());
//        container_view.setAdapter(adapter);
//        container_view.setCurrentItem(currentFragmentNumber);
//
////        tabLayout.setupWithViewPager(container_view);
////        tabLayout.getTabAt(0).setText("Beranda");
//    }

//    private void setupToolbar() {
//        try {
//            setSupportActionBar(toolbar);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger);
//        } catch (NullPointerException exc) {
//            exc.printStackTrace();
//        }
//    }


//    private void setupActivityWidgets() {
//        toolbar = findViewById(R.id.toolbar);
//        drawerLayout = findViewById(R.id.drawerLayout);
//        navigationView = findViewById(R.id.home_nav_view);
//        container_view = findViewById(R.id.container_view);
//        tabLayout = findViewById(R.id.tabLayout);
//
//        //Listener's
//        navigationView.setNavigationItemSelectedListener(new NavigationHandler(this));
//        drawerLayout.addDrawerListener(new DrawerHandler(this));
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                drawerLayout.openDrawer(GravityCompat.START);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }