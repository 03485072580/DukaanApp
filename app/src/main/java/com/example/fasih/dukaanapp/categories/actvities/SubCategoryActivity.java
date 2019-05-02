package com.example.fasih.dukaanapp.categories.actvities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.categories.fragments.CarsFragment;
import com.example.fasih.dukaanapp.categories.fragments.ClothingFragment;
import com.example.fasih.dukaanapp.categories.fragments.CosmeticsFragment;
import com.example.fasih.dukaanapp.categories.fragments.ElectronicsFragment;
import com.example.fasih.dukaanapp.categories.fragments.FragrancesFragment;
import com.example.fasih.dukaanapp.categories.fragments.JewellaryFragment;
import com.example.fasih.dukaanapp.categories.fragments.MobileFragment;
import com.example.fasih.dukaanapp.models.Products;
import com.example.fasih.dukaanapp.utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

public class SubCategoryActivity extends AppCompatActivity {


    private SearchView search;
    private TextView userVisibleSearch;
    private MaterialSpinner selectSearchMethodSpinner;


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
        setupActivityWidgets();
        if (savedInstanceState != null) {
            //Coming after the Screen Rotation
            userViewProductsList = savedInstanceState
                    .getParcelableArrayList(getString(R.string.userViewProductsList));
            setupIntentResources(userViewProductsList);
        }
        setupFirebase(getIntent());
    }

    private void setupActivityWidgets() {
        search = findViewById(R.id.search);
        userVisibleSearch = findViewById(R.id.userVisibleSearch);
        selectSearchMethodSpinner = findViewById(R.id.materialSpinner);

        selectSearchMethodSpinner.setItems(getResources().getStringArray(R.array.search_type));
        selectSearchMethodSpinner.setVisibility(View.GONE);
        //Listener
        search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userVisibleSearch.setVisibility(View.GONE);
            }
        });
    }


    public void setupIntentResources(ArrayList<Products> userViewProductsList) {
        this.userViewProductsList = userViewProductsList;

        if (getIntent() != null) {
            Intent intent = getIntent();
            if (intent.hasExtra(getString(R.string.carsFragment))) {
                if (intent.getStringExtra(getString(R.string.carsFragment)).equals(getString(R.string.carsFragment))) {
                    CarsFragment carsFragment = new CarsFragment();
                    carsFragment.setSearchView(search);
                    carsFragment.setMaterialSpinner(selectSearchMethodSpinner);
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
                    ClothingFragment clothingFragment = new ClothingFragment();
                    clothingFragment.setSearchView(search);
                    clothingFragment.setMaterialSpinner(selectSearchMethodSpinner);
                    Bundle myArrayListHolder = new Bundle();
                    myArrayListHolder.putParcelableArrayList(getString(R.string.userViewProductsList)
                            , userViewProductsList);
                    clothingFragment.setArguments(myArrayListHolder);
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer
                            , clothingFragment
                            , getString(R.string.clothingFragment));
                    transaction.commitAllowingStateLoss();

                }
            }

            if (intent.hasExtra(getString(R.string.jewellaryFragment))) {
                if (intent.getStringExtra(getString(R.string.jewellaryFragment)).equals(getString(R.string.jewellaryFragment))) {
                    JewellaryFragment jewellaryFragment = new JewellaryFragment();
                    jewellaryFragment.setSearchView(search);
                    jewellaryFragment.setMaterialSpinner(selectSearchMethodSpinner);
                    Bundle myArrayListHolder = new Bundle();
                    myArrayListHolder.putParcelableArrayList(getString(R.string.userViewProductsList)
                            , userViewProductsList);
                    jewellaryFragment.setArguments(myArrayListHolder);
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer
                            , jewellaryFragment
                            , getString(R.string.jewellaryFragment));
                    transaction.commit();

                }
            }

            if (intent.hasExtra(getString(R.string.mobileFragment))) {
                if (intent.getStringExtra(getString(R.string.mobileFragment)).equals(getString(R.string.mobileFragment))) {
                    FragmentManager manager = getSupportFragmentManager();
                    MobileFragment mobileFragment = new MobileFragment();
                    mobileFragment.setSearchView(search);
                    mobileFragment.setMaterialSpinner(selectSearchMethodSpinner);
                    Bundle myArrayListHolderBundle = new Bundle();
                    myArrayListHolderBundle.putParcelableArrayList(getString(R.string.userViewProductsList)
                            , userViewProductsList);
                    mobileFragment.setArguments(myArrayListHolderBundle);
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer
                            , mobileFragment
                            , getString(R.string.mobileFragment));
                    transaction.commitAllowingStateLoss();

                }
            }

            if (intent.hasExtra(getString(R.string.fragrancesFragment))) {
                if (intent.getStringExtra(getString(R.string.fragrancesFragment)).equals(getString(R.string.fragrancesFragment))) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragrancesFragment fragrancesFragment = new FragrancesFragment();
                    fragrancesFragment.setSearchView(search);
                    fragrancesFragment.setMaterialSpinner(selectSearchMethodSpinner);
                    Bundle myArrayListHolderBundle = new Bundle();
                    myArrayListHolderBundle.putParcelableArrayList(getString(R.string.userViewProductsList)
                            , userViewProductsList);
                    fragrancesFragment.setArguments(myArrayListHolderBundle);
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer
                            , fragrancesFragment
                            , getString(R.string.fragrancesFragment));
                    transaction.commitAllowingStateLoss();

                }
            }

            if (intent.hasExtra(getString(R.string.cosmeticsFragment))) {
                if (intent.getStringExtra(getString(R.string.cosmeticsFragment)).equals(getString(R.string.cosmeticsFragment))) {
                    FragmentManager manager = getSupportFragmentManager();
                    CosmeticsFragment cosmeticsFragment = new CosmeticsFragment();
                    cosmeticsFragment.setSearchView(search);
                    cosmeticsFragment.setMaterialSpinner(selectSearchMethodSpinner);
                    Bundle myArrayListHolderBundle = new Bundle();
                    myArrayListHolderBundle.putParcelableArrayList(getString(R.string.userViewProductsList)
                            , userViewProductsList);
                    cosmeticsFragment.setArguments(myArrayListHolderBundle);
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer
                            , cosmeticsFragment
                            , getString(R.string.cosmeticsFragment));
                    transaction.commitAllowingStateLoss();

                }
            }

            if (intent.hasExtra(getString(R.string.electronicsFragment))) {
                if (intent.getStringExtra(getString(R.string.electronicsFragment)).equals(getString(R.string.electronicsFragment))) {
                    FragmentManager manager = getSupportFragmentManager();
                    ElectronicsFragment electronicsFragment = new ElectronicsFragment();
                    electronicsFragment.setSearchView(search);
                    electronicsFragment.setMaterialSpinner(selectSearchMethodSpinner);
                    Bundle myArrayListHolderBundle = new Bundle();
                    myArrayListHolderBundle.putParcelableArrayList(getString(R.string.userViewProductsList)
                            , userViewProductsList);
                    electronicsFragment.setArguments(myArrayListHolderBundle);
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer
                            , electronicsFragment
                            , getString(R.string.electronicsFragment));
                    transaction.commitAllowingStateLoss();

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
        firebaseMethods = new FirebaseMethods(this, getString(R.string.activity_sub_category));

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
            if (intent.hasExtra(getString(R.string.query_type_Suits))) {
                if (getString(R.string.query_type_Suits).equals(getString(R.string.query_type_Suits))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_Suits));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_type_Coat))) {
                if (intent.getStringExtra(getString(R.string.query_type_Coat)).equals(getString(R.string.query_type_Coat))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_Coat));
                    firebaseMethods.queryProducts(queryString);
                }
            }

            if (intent.hasExtra(getString(R.string.query_type_Stitched))) {
                if (intent.getStringExtra(getString(R.string.query_type_Stitched)).equals(getString(R.string.query_type_Stitched))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_Stitched));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_type_UnStitched))) {
                if (intent.getStringExtra(getString(R.string.query_type_UnStitched)).equals(getString(R.string.query_type_UnStitched))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_UnStitched));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_type_Honda))) {
                if (intent.getStringExtra(getString(R.string.query_type_Honda)).equals(getString(R.string.query_type_Honda))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_Honda));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_type_Suzuki))) {
                if (intent.getStringExtra(getString(R.string.query_type_Suzuki)).equals(getString(R.string.query_type_Suzuki))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_Suzuki));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_type_Toyota))) {
                if (intent.getStringExtra(getString(R.string.query_type_Toyota)).equals(getString(R.string.query_type_Toyota))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_Toyota));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_type_BMW))) {
                if (intent.getStringExtra(getString(R.string.query_type_BMW)).equals(getString(R.string.query_type_BMW))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_BMW));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_type_Payal))) {
                if (intent.getStringExtra(getString(R.string.query_type_Payal)).equals(getString(R.string.query_type_Payal))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_Payal));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_type_Jhumar))) {
                if (intent.getStringExtra(getString(R.string.query_type_Jhumar)).equals(getString(R.string.query_type_Jhumar))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_Jhumar));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_type_Matha_Patti))) {
                if (intent.getStringExtra(getString(R.string.query_type_Matha_Patti)).equals(getString(R.string.query_type_Matha_Patti))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_Matha_Patti));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_type_Kanta))) {
                if (intent.getStringExtra(getString(R.string.query_type_Kanta)).equals(getString(R.string.query_type_Kanta))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_Kanta));
                    firebaseMethods.queryProducts(queryString);
                }
            }

            if (intent.hasExtra(getString(R.string.query_type_Samsung))) {
                if (intent.getStringExtra(getString(R.string.query_type_Samsung)).equals(getString(R.string.query_type_Samsung))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_Samsung));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_type_OPPO))) {
                if (intent.getStringExtra(getString(R.string.query_type_OPPO)).equals(getString(R.string.query_type_OPPO))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_OPPO));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_type_QMobile))) {
                if (intent.getStringExtra(getString(R.string.query_type_QMobile)).equals(getString(R.string.query_type_QMobile))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_QMobile));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_type_IPhone))) {
                if (intent.getStringExtra(getString(R.string.query_type_IPhone)).equals(getString(R.string.query_type_IPhone))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_IPhone));
                    firebaseMethods.queryProducts(queryString);
                }
            }

            if (intent.hasExtra(getString(R.string.query_type_Philips))) {
                if (intent.getStringExtra(getString(R.string.query_type_Philips)).equals(getString(R.string.query_type_Philips))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_Philips));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_type_Haier))) {
                if (intent.getStringExtra(getString(R.string.query_type_Haier)).equals(getString(R.string.query_type_Haier))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_Haier));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_type_Orient))) {
                if (intent.getStringExtra(getString(R.string.query_type_Orient)).equals(getString(R.string.query_type_Orient))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_Orient));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_type_Toshiba))) {
                if (intent.getStringExtra(getString(R.string.query_type_Toshiba)).equals(getString(R.string.query_type_Toshiba))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_Toshiba));
                    firebaseMethods.queryProducts(queryString);
                }
            }

            if (intent.hasExtra(getString(R.string.query_type_Lotions))) {
                if (intent.getStringExtra(getString(R.string.query_type_Lotions)).equals(getString(R.string.query_type_Lotions))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_Lotions));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_type_Creams))) {
                if (intent.getStringExtra(getString(R.string.query_type_Creams)).equals(getString(R.string.query_type_Creams))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_Creams));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_type_Base))) {
                if (intent.getStringExtra(getString(R.string.query_type_Base)).equals(getString(R.string.query_type_Base))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_Base));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_type_MakeupKits))) {
                if (intent.getStringExtra(getString(R.string.query_type_MakeupKits)).equals(getString(R.string.query_type_MakeupKits))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_MakeupKits));
                    firebaseMethods.queryProducts(queryString);
                }
            }

            if (intent.hasExtra(getString(R.string.query_type_J_))) {
                if (intent.getStringExtra(getString(R.string.query_type_J_)).equals(getString(R.string.query_type_J_))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_J_));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_type_Alisha))) {
                if (intent.getStringExtra(getString(R.string.query_type_Alisha)).equals(getString(R.string.query_type_Alisha))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_Alisha));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_type_Boss))) {
                if (intent.getStringExtra(getString(R.string.query_type_Boss)).equals(getString(R.string.query_type_Boss))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_Boss));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_type_Fogg))) {
                if (intent.getStringExtra(getString(R.string.query_type_Fogg)).equals(getString(R.string.query_type_Fogg))) {
                    queryString = intent.getStringExtra(getString(R.string.query_type_Fogg));
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
