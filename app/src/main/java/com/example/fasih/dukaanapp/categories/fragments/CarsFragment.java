package com.example.fasih.dukaanapp.categories.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.adapter.CarsFragmentAdapter;
import com.example.fasih.dukaanapp.categories.actvities.ProductDetailActivity;
import com.example.fasih.dukaanapp.categories.interfaces.KeepHandleRecyclerList;
import com.example.fasih.dukaanapp.categories.interfaces.LoadDynamicData;
import com.example.fasih.dukaanapp.home.interfaces.OnRecyclerImageSelectedListener;
import com.example.fasih.dukaanapp.models.Products;
import com.example.fasih.dukaanapp.utils.FirebaseMethods;
import com.example.fasih.dukaanapp.utils.StringManipulations;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Fasih on 01/01/19.
 */

public class CarsFragment extends Fragment implements LoadDynamicData
        , KeepHandleRecyclerList
        , SearchView.OnQueryTextListener
        , OnRecyclerImageSelectedListener {
    //Load Data from Firebase Dynamically here

    private SearchView searchView;
    private ArrayList<Products> userViewProductsList, filteredList, backupUserViewProductsList;
    private RecyclerView carsProductsContainer;
    private CarsFragmentAdapter adapter;

    //Firebase Stuff
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseMethods firebaseMethods;
    private String currentUserID = null;

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.setInitialLoadingProgress(true);
        filteredList.clear();

        if (newText.equals("")) {
            filteredList.addAll(backupUserViewProductsList);
            adapter.setInitialLoadingProgress(false);
        }

        for (Products product : backupUserViewProductsList) {

            if (StringManipulations.toLowerCase(product.getProduct_name())
                    .contains(StringManipulations.toLowerCase(newText))) {
                filteredList.add(product);
            }
        }
        adapter.setFilteredList(filteredList);
        return true;
    }

    /**
     * Response for fetching the Cars List from The Firebase DB
     *
     * @param userViewProductsList
     */
    @Override
    public void onUpdateRecyclerList(ArrayList<Products> userViewProductsList) {
        adapter.notifyDataSetChanged();
        adapter.setInitialLoadingProgress();
    }

    @Override
    public void onRequestData(ArrayList<Products> userViewProductsList) {
        firebaseMethods.setListenerForUpdatingRecyclerView(this);
        firebaseMethods.queryProducts(getString(R.string.query_CARS)
                , userViewProductsList
                , adapter);
    }

    /**
     * This method is responsible for
     * keeping track of the click events on the categories items
     *
     * @param position
     * @param view     Here view helps to determine which item on the
     *                 layout clicked
     */
    @Override
    public void onClickGridImage(int position, View view, Products currentSelectedProduct) {

        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
        intent.putExtra(getString(R.string.currentSelectedProduct), currentSelectedProduct);
        startActivity(intent);
    }

    @Override
    public void onClickGridImage(int position, View view, String Url) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cars, container, false);
        setupFragmentWidgets(view);
        setupFirebase();
        setupRecyclerView(getBundleData(getArguments()));
        return view;
    }

    private ArrayList<Products> getBundleData(Bundle arguments) {
        ArrayList<Products> userViewProductsList = null;
        if (arguments != null) {
            userViewProductsList = arguments.getParcelableArrayList(getString(R.string.userViewProductsList));
        }
        return userViewProductsList;
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }


    }


    private int dpToPx(int i) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, r.getDisplayMetrics()));
    }

    public void setupRecyclerView(ArrayList<Products> userViewProductsList) {

        if (userViewProductsList != null)
            if (!userViewProductsList.isEmpty()) {
                backupUserViewProductsList = new ArrayList<>();
                backupUserViewProductsList.addAll(userViewProductsList);
                this.userViewProductsList = userViewProductsList;
                adapter = new CarsFragmentAdapter(getContext()
                        , userViewProductsList
                        , carsProductsContainer);
                adapter.setupOnItemClickListener(this);
                adapter.setInitialLoadingProgress();
                adapter.setLoadDynamicData(this);


                carsProductsContainer.setLayoutManager(new GridLayoutManager(getContext(), 2));
                carsProductsContainer.addItemDecoration(new GridSpacingItemDecoration(10, dpToPx(10), true));
                carsProductsContainer.setItemAnimator(new DefaultItemAnimator());
                carsProductsContainer.setAdapter(adapter);


            }
    }

    public void setupFragmentWidgets(View view) {
        carsProductsContainer = view.findViewById(R.id.carsProductsContainer);
    }


    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        firebaseMethods = new FirebaseMethods(getActivity(), getString(R.string.carsFragment));

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    currentUserID = user.getUid();
                }

            }
        };

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

    public void setSearchView(SearchView searchView ) {
        this.searchView = searchView;
        this.searchView.setOnQueryTextListener(this);

        filteredList = new ArrayList<>();
    }


}
