package com.example.fasih.dukaanapp.categories.actvities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.categories.fragments.CarsFragment;
import com.example.fasih.dukaanapp.categories.fragments.ClothingFragment;
import com.example.fasih.dukaanapp.categories.fragments.JewellaryFragment;
import com.example.fasih.dukaanapp.categories.fragments.MobileFragment;
import com.example.fasih.dukaanapp.models.Products;
import com.example.fasih.dukaanapp.utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UniqueCategoryActivity extends AppCompatActivity {


    //Firebase Stuff
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseMethods firebaseMethods;
    private String currentUserID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unique_category);
        setupFirebase(getIntent());
    }

    public void setupIntentResources(ArrayList<Products> userViewProductsList) {
        Log.d("TAG1234", "setupIntentResources: ");
        if (getIntent() != null) {
            Intent intent = getIntent();
            if (intent.hasExtra(getString(R.string.carsFragment))) {
                if (intent.getStringExtra(getString(R.string.carsFragment)).equals(getString(R.string.carsFragment))) {
                    CarsFragment carsFragment = new CarsFragment();
                    Bundle myArrayListHolderBundle = new Bundle();
                    myArrayListHolderBundle.putParcelableArrayList(getString(R.string.userViewProductsList)
                            , userViewProductsList);
                    carsFragment.setArguments(myArrayListHolderBundle);
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer
                            , carsFragment
                            , getString(R.string.carsFragment));
                    transaction.commitAllowingStateLoss();

                }
            }

            if (intent.hasExtra(getString(R.string.clothingFragment))) {
                if (intent.getStringExtra(getString(R.string.clothingFragment)).equals(getString(R.string.clothingFragment))) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer
                            , new ClothingFragment()
                            , getString(R.string.clothingFragment));
                    transaction.commit();

                }
            }

            if (intent.hasExtra(getString(R.string.jewellaryFragment))) {
                if (intent.getStringExtra(getString(R.string.jewellaryFragment)).equals(getString(R.string.jewellaryFragment))) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer
                            , new JewellaryFragment()
                            , getString(R.string.jewellaryFragment));
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
        } else {
            Log.d("TAG1234", "setupIntentResources: null -->" + getIntent());
        }
    }


    private void setupFirebase(Intent intent) {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        firebaseMethods = new FirebaseMethods(this, getString(R.string.activity_unique_category));

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    currentUserID = user.getUid();
                }

            }
        };
        if (intent != null) {
            String queryString = null;
            if (intent.hasExtra("CARS")) {
                if (intent.getStringExtra("CARS").equals("CARS")) {
                    queryString = intent.getStringExtra("CARS");
                    firebaseMethods.queryProducts(queryString);
                }
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuth != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

}
