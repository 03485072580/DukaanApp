package com.example.fasih.dukaanapp.categories.actvities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.adapter.SubCategoriesListAdapter;
import com.example.fasih.dukaanapp.categories.fragments.CarsFragment;
import com.example.fasih.dukaanapp.categories.fragments.ClothingFragment;
import com.example.fasih.dukaanapp.categories.fragments.CosmeticsFragment;
import com.example.fasih.dukaanapp.categories.fragments.ElectronicsFragment;
import com.example.fasih.dukaanapp.categories.fragments.FragrancesFragment;
import com.example.fasih.dukaanapp.categories.fragments.JewellaryFragment;
import com.example.fasih.dukaanapp.categories.fragments.MobileFragment;
import com.example.fasih.dukaanapp.customModels.RecyclerSelectedCategory;
import com.example.fasih.dukaanapp.models.Products;
import com.example.fasih.dukaanapp.utils.FirebaseMethods;
import com.example.fasih.dukaanapp.utils.RecyclerDataHelper;
import com.example.fasih.dukaanapp.utils.RecyclerLinearAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UniqueCategoryActivity extends AppCompatActivity {


    private SearchView search;
    private TextView userVisibleSearch;
    private RecyclerView subCategoriesListRecyclerView;
    private SubCategoriesListAdapter adapter;


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
        setupRecyclerView();
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
        subCategoriesListRecyclerView = findViewById(R.id.subCategoriesListRecyclerView);

        //Listener
        search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userVisibleSearch.setVisibility(View.GONE);
            }
        });
    }

    private void setupRecyclerView() {

        adapter = new SubCategoriesListAdapter(this);
        LinearLayoutManager manager1 = new LinearLayoutManager(this
                , LinearLayoutManager.HORIZONTAL
                , false);
        subCategoriesListRecyclerView.setLayoutManager(manager1);
        subCategoriesListRecyclerView.setAdapter(adapter);
    }

    public void setupIntentResources(ArrayList<Products> userViewProductsList) {
        this.userViewProductsList = userViewProductsList;

        if (getIntent() != null) {
            Intent intent = getIntent();
            if (intent.hasExtra(getString(R.string.carsFragment))) {
                if (intent.getStringExtra(getString(R.string.carsFragment)).equals(getString(R.string.carsFragment))) {
                    CarsFragment carsFragment = new CarsFragment();
                    carsFragment.setSearchView(search);
                    carsFragment.setSubCategoriesResources(subCategoriesListRecyclerView
                            , adapter
                            , getClothingSubCategoriesDataList(carsFragment));
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
                    clothingFragment.setSubCategoriesResources(subCategoriesListRecyclerView
                            , adapter
                            , getClothingSubCategoriesDataList(clothingFragment));
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
                    jewellaryFragment.setSubCategoriesResources(subCategoriesListRecyclerView
                            , adapter
                            , getClothingSubCategoriesDataList(jewellaryFragment));
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
                    mobileFragment.setSubCategoriesResources(subCategoriesListRecyclerView
                            , adapter
                            , getClothingSubCategoriesDataList(mobileFragment));
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
                    fragrancesFragment.setSubCategoriesResources(subCategoriesListRecyclerView
                            , adapter
                            , getClothingSubCategoriesDataList(fragrancesFragment));
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
                    cosmeticsFragment.setSubCategoriesResources(subCategoriesListRecyclerView
                            , adapter
                            , getClothingSubCategoriesDataList(cosmeticsFragment));
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
                    electronicsFragment.setSubCategoriesResources(subCategoriesListRecyclerView
                            , adapter
                            , getClothingSubCategoriesDataList(electronicsFragment));
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

    /**
     * This method is responsible for updating the SubCategory Horizontal items List
     *
     * @param fragment
     * @return
     */

    private ArrayList<RecyclerSelectedCategory> getClothingSubCategoriesDataList(Fragment fragment) {

        if( fragment instanceof CarsFragment){
            return RecyclerDataHelper.getSubCategoryCarsData(this);
        }
        if( fragment instanceof ClothingFragment){
            return RecyclerDataHelper.getSubCategoryClothingData(this);
        }
        if( fragment instanceof CosmeticsFragment){
            return RecyclerDataHelper.getSubCategoryCosmeticsData(this);
        }
        if( fragment instanceof ElectronicsFragment){
            return RecyclerDataHelper.getSubCategoryElectronicsData(this);
        }
        if( fragment instanceof FragrancesFragment){
            return RecyclerDataHelper.getSubCategoryFragrancesData(this);
        }
        if( fragment instanceof JewellaryFragment){
            return RecyclerDataHelper.getSubCategoryJewellaryData(this);
        }
        if( fragment instanceof MobileFragment){
            return RecyclerDataHelper.getSubCategoryMobileData(this);
        }

        return null;

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
            if (intent.hasExtra(getString(R.string.query_CARS))) {
                if (getString(R.string.query_CARS).equals(getString(R.string.query_CARS))) {
                    queryString = intent.getStringExtra(getString(R.string.query_CARS));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_MOBILES))) {
                if (intent.getStringExtra(getString(R.string.query_MOBILES)).equals(getString(R.string.query_MOBILES))) {
                    queryString = intent.getStringExtra(getString(R.string.query_MOBILES));
                    firebaseMethods.queryProducts(queryString);
                }
            }

            if (intent.hasExtra(getString(R.string.query_CLOTHES))) {
                if (intent.getStringExtra(getString(R.string.query_CLOTHES)).equals(getString(R.string.query_CLOTHES))) {
                    queryString = intent.getStringExtra(getString(R.string.query_CLOTHES));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_JEWELLERY))) {
                if (intent.getStringExtra(getString(R.string.query_JEWELLERY)).equals(getString(R.string.query_JEWELLERY))) {
                    queryString = intent.getStringExtra(getString(R.string.query_JEWELLERY));
                    firebaseMethods.queryProducts(queryString);
                }
            }
            if (intent.hasExtra(getString(R.string.query_FRAGRANCES))) {
                if (intent.getStringExtra(getString(R.string.query_FRAGRANCES)).equals(getString(R.string.query_FRAGRANCES))) {
                    queryString = intent.getStringExtra(getString(R.string.query_FRAGRANCES));
                    firebaseMethods.queryProducts(queryString);
                }
            }

            if (intent.hasExtra(getString(R.string.query_COSMETICS))) {
                if (intent.getStringExtra(getString(R.string.query_COSMETICS)).equals(getString(R.string.query_COSMETICS))) {
                    queryString = intent.getStringExtra(getString(R.string.query_COSMETICS));
                    firebaseMethods.queryProducts(queryString);
                }
            }

            if (intent.hasExtra(getString(R.string.query_ELECTRONICS))) {
                if (intent.getStringExtra(getString(R.string.query_ELECTRONICS)).equals(getString(R.string.query_ELECTRONICS))) {
                    queryString = intent.getStringExtra(getString(R.string.query_ELECTRONICS));
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
