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
    private ArrayList<Products> userViewProductsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unique_category);
        if (savedInstanceState != null) {
            //Coming after the Screen Rotation
            userViewProductsList = savedInstanceState
                    .getParcelableArrayList(getString(R.string.userViewProductsList));
            setupIntentResources(userViewProductsList);
        }
        setupFirebase(getIntent());
    }

    public void setupIntentResources(ArrayList<Products> userViewProductsList) {
        this.userViewProductsList = userViewProductsList;

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
                    MobileFragment mobileFragment = new MobileFragment();
                    Bundle myArrayListHolderBundle = new Bundle();
                    myArrayListHolderBundle.putParcelableArrayList(getString(R.string.userViewProductsList)
                            , userViewProductsList);
                    mobileFragment.setArguments(myArrayListHolderBundle);
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer
                            , mobileFragment
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
        if (intent != null && userViewProductsList.isEmpty()) {
            String queryString = null;
            if (intent.hasExtra("CARS")) {
                if (intent.getStringExtra("CARS").equals("CARS")) {
                    queryString = intent.getStringExtra("CARS");
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra("MOBILES")) {
                if (intent.getStringExtra("MOBILES").equals("MOBILES")) {
                    queryString = intent.getStringExtra("MOBILES");
                    firebaseMethods.queryProducts(queryString);
                }
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.userViewProductsList), userViewProductsList);
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
