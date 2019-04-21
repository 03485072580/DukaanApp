package com.example.fasih.dukaanapp.home.activities;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.home.fragments.chat.Chat_Fragment;
import com.example.fasih.dukaanapp.home.fragments.userPageResources.HomeFragment;
import com.example.fasih.dukaanapp.home.fragments.userPageResources.UserLocationTrackerFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class UserHomePageActivity extends AppCompatActivity {
    //Fragment Numbers
    private static final int currentFragmentNumber = 0;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private ViewPager container_view;
    private TabLayout tabLayout;
    //Firebase Stuff
    private FirebaseAuth.AuthStateListener authStateStateListener;
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
//                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
//                    replaceFragment(new Chat_Fragment(), "Profile");
//                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    replaceSupportFragment(new Chat_Fragment(), getString(R.string.chatFragment));
//                    mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.location:
                    replaceSupportFragment(new UserLocationTrackerFragment(), getString(R.string.userLocationTrackerFragment));
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

//    private void replaceFragment(Fragment targetFragment, String name) {
//
////        drawer.closeDrawers();
//
////        titletxt.setText(name);
//        mylist.add(name);
//
//
//        View view = this.getCurrentFocus();
//        FragmentManager fm = getFragmentManager();
//        if (view != null) {
//
//            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//
//
////        Log.d("TAG", targetFragment.getTag());
////        Addparent.setVisibility(View.GONE);
////        ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        fm.beginTransaction()
//                .replace(R.id.container, targetFragment, "fragment")
//                .addToBackStack(name)
//                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                .commit();
//
//        Log.i("MYLIST", mylist.toString());
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_home);
//        setupActivityWidgets();
//        setupToolbar();
//        setupViewPager(currentFragmentNumber);
        setupFirebase();

        mylist = new ArrayList<String>();
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        replaceSupportFragment(new HomeFragment(), getString(R.string.homeFragment));


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
//                    Intent intent = new Intent(UserHomePageActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    finish();
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
