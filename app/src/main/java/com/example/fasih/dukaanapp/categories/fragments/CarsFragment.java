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
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.adapter.CarsFragmentAdapter;
import com.example.fasih.dukaanapp.adapter.SubCategoriesListAdapter;
import com.example.fasih.dukaanapp.categories.actvities.ProductDetailActivity;
import com.example.fasih.dukaanapp.categories.actvities.SubCategoryActivity;
import com.example.fasih.dukaanapp.categories.interfaces.KeepHandleRecyclerList;
import com.example.fasih.dukaanapp.categories.interfaces.LoadDynamicData;
import com.example.fasih.dukaanapp.customModels.RecyclerSelectedCategory;
import com.example.fasih.dukaanapp.home.interfaces.OnRecyclerImageSelectedListener;
import com.example.fasih.dukaanapp.models.Products;
import com.example.fasih.dukaanapp.utils.FirebaseMethods;
import com.example.fasih.dukaanapp.utils.StringManipulations;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

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
    private RecyclerView subCategoriesListRecyclerView;
    private SubCategoriesListAdapter subCategoriesAdapter;

    //Firebase Stuff
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseMethods firebaseMethods;
    private String currentUserID = null;
    private MaterialSpinner selectSearchMethodSpinner;

    @Override
    public boolean onQueryTextSubmit(String newText) {

        if (searchView.getQueryHint().equals("Search Shop")) {
            //do restriction on the entered shop Name (Unique)

            adapter.setInitialLoadingProgress(true);
            if (newText.equals("")) {
                filteredList.clear();
                filteredList.addAll(backupUserViewProductsList);
                adapter.setInitialLoadingProgress(false);
            } else {
                //these products reflects all the current search query items
                // (E,g All XLI cars selling by different vendors)and
                // then needs to get all those products that are available
                // in shop of particular user interest

                ArrayList<Products> shopRestrictedList = new ArrayList<>();
                for (Products product : filteredList) {

                    firebaseMethods.filterInterestedShopProducts(shopRestrictedList
                            , newText
                            , product
                            , adapter);
                }
                shopRestrictedList.clear();
            }
            adapter.setFilteredList(filteredList);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        selectSearchMethodSpinner.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(searchView.getQueryHint())) {
            if (searchView.getQueryHint().equals("Search")) {
                //do general Search Here
                adapter.setInitialLoadingProgress(true);
                filteredList.clear();
                if (newText.equals("")) {
                    selectSearchMethodSpinner.setVisibility(View.GONE);
                    filteredList.addAll(backupUserViewProductsList);
                    adapter.setInitialLoadingProgress(false);
                } else {
                    for (Products product : backupUserViewProductsList) {

                        if (StringManipulations.toLowerCase(product.getProduct_name())
                                .contains(StringManipulations.toLowerCase(newText))) {
                            filteredList.add(product);
                        }
                    }
                }


                adapter.setFilteredList(filteredList);
            }
        }

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
        if (Url != null) {
            if (Url.equals(getString(R.string.subCategoriesListAdapter))) {

                if (subCategoriesAdapter
                        .getRecyclerSelectedCategoryObject(position)
                        .getCategoryImageResource() ==
                        R.drawable.honda_logo) {
                    Intent intent = new Intent(getActivity(), SubCategoryActivity.class);
                    intent.putExtra(getString(R.string.query_type_Honda), getString(R.string.query_type_Honda));
                    intent.putExtra(getString(R.string.carsFragment), getString(R.string.carsFragment));
                    startActivity(intent);
                }

                if (subCategoriesAdapter
                        .getRecyclerSelectedCategoryObject(position)
                        .getCategoryImageResource() ==
                        R.drawable.suzuki) {
                    Intent intent = new Intent(getActivity(), SubCategoryActivity.class);
                    intent.putExtra(getString(R.string.query_type_Suzuki), getString(R.string.query_type_Suzuki));
                    intent.putExtra(getString(R.string.carsFragment), getString(R.string.carsFragment));
                    startActivity(intent);
                }

                if (subCategoriesAdapter
                        .getRecyclerSelectedCategoryObject(position)
                        .getCategoryImageResource() ==
                        R.drawable.toyota) {
                    Intent intent = new Intent(getActivity(), SubCategoryActivity.class);
                    intent.putExtra(getString(R.string.query_type_Toyota), getString(R.string.query_type_Toyota));
                    intent.putExtra(getString(R.string.carsFragment), getString(R.string.carsFragment));
                    startActivity(intent);
                }
                if (subCategoriesAdapter
                        .getRecyclerSelectedCategoryObject(position)
                        .getCategoryImageResource() ==
                        R.drawable.bmw) {
                    Intent intent = new Intent(getActivity(), SubCategoryActivity.class);
                    intent.putExtra(getString(R.string.query_type_BMW), getString(R.string.query_type_BMW));
                    intent.putExtra(getString(R.string.carsFragment), getString(R.string.carsFragment));
                    startActivity(intent);
                }

            }
        }

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

    public void setSubCategoriesResources(RecyclerView subCategoriesListRecyclerView
            , SubCategoriesListAdapter adapter
            , ArrayList<RecyclerSelectedCategory> dataList) {

        this.subCategoriesListRecyclerView = subCategoriesListRecyclerView;
        this.subCategoriesAdapter = adapter;
        adapter.setData(dataList);
        adapter.setupOnItemClickListener(this);
        adapter.notifyDataSetChanged();

    }

    public void setMaterialSpinner(MaterialSpinner selectSearchMethodSpinner) {

        this.selectSearchMethodSpinner = selectSearchMethodSpinner;
        selectSearchMethodSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                searchView.setQueryHint(view.getText());
                searchView.setQuery("", false);
            }
        });
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

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
        this.searchView.setOnQueryTextListener(this);

        filteredList = new ArrayList<>();
    }


}
