package com.example.fasih.dukaanapp.categories.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.adapter.ElectronicsProductsAdapter;
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
 * Created by Fasih on 04/10/19.
 */

public class ElectronicsFragment extends Fragment implements KeepHandleRecyclerList
        , LoadDynamicData
        , OnRecyclerImageSelectedListener
        , SearchView.OnQueryTextListener {

    private SearchView searchView;
    private RecyclerView productsContainer;
    private ElectronicsProductsAdapter electronicsProductsAdapter;
    private ArrayList<Products> userViewProductsList, filteredList, backupUserViewProductsList;
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

            electronicsProductsAdapter.setLoading(true);
            if (newText.equals("")) {
                filteredList.clear();
                filteredList.addAll(backupUserViewProductsList);
                electronicsProductsAdapter.setLoading(false);
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
                            , electronicsProductsAdapter);
                }
                shopRestrictedList.clear();
            }
            electronicsProductsAdapter.setFilteredList(filteredList);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        selectSearchMethodSpinner.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(searchView.getQueryHint())) {
            if (searchView.getQueryHint().equals("Search")) {
                //do general Search Here
                electronicsProductsAdapter.setLoading(true);
                filteredList.clear();
                if (newText.equals("")) {
                    selectSearchMethodSpinner.setVisibility(View.GONE);
                    filteredList.addAll(backupUserViewProductsList);
                    electronicsProductsAdapter.setLoading(false);
                } else {
                    for (Products product : backupUserViewProductsList) {

                        if (StringManipulations.toLowerCase(product.getProduct_name())
                                .contains(StringManipulations.toLowerCase(newText))) {
                            filteredList.add(product);
                        }
                    }
                }


                electronicsProductsAdapter.setFilteredList(filteredList);
            }
        }

        return true;
    }

    @Override
    public void onUpdateRecyclerList(ArrayList<Products> userViewProductsList) {
        electronicsProductsAdapter.notifyDataSetChanged();
        electronicsProductsAdapter.setLoading();
    }

    @Override
    public void onRequestData(ArrayList<Products> userViewProductsList) {
        firebaseMethods.setListenerForUpdatingRecyclerView(this);
        firebaseMethods.queryProducts(getString(R.string.query_ELECTRONICS)
                , userViewProductsList
                , electronicsProductsAdapter);
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
                        R.drawable.ic_deodorant) {
                    Intent intent = new Intent(getActivity(), SubCategoryActivity.class);
                    intent.putExtra(getString(R.string.query_type_Coat), getString(R.string.query_type_Coat));
                    intent.putExtra(getString(R.string.clothingFragment), getString(R.string.clothingFragment));
                    startActivity(intent);
                }

                if (subCategoriesAdapter
                        .getRecyclerSelectedCategoryObject(position)
                        .getCategoryImageResource() ==
                        R.drawable.ic_clothing) {
                    Intent intent = new Intent(getActivity(), SubCategoryActivity.class);
                    intent.putExtra(getString(R.string.query_type_Suits), getString(R.string.query_type_Suits));
                    intent.putExtra(getString(R.string.clothingFragment), getString(R.string.clothingFragment));
                    startActivity(intent);
                }

                if (subCategoriesAdapter
                        .getRecyclerSelectedCategoryObject(position)
                        .getCategoryImageResource() ==
                        R.drawable.ic_car) {
                    Intent intent = new Intent(getActivity(), SubCategoryActivity.class);
                    intent.putExtra(getString(R.string.query_type_Stitched), getString(R.string.query_type_Stitched));
                    intent.putExtra(getString(R.string.clothingFragment), getString(R.string.clothingFragment));
                    startActivity(intent);
                }
                if (subCategoriesAdapter
                        .getRecyclerSelectedCategoryObject(position)
                        .getCategoryImageResource() ==
                        R.drawable.ic_ring) {
                    Intent intent = new Intent(getActivity(), SubCategoryActivity.class);
                    intent.putExtra(getString(R.string.query_type_UnStitched), getString(R.string.query_type_UnStitched));
                    intent.putExtra(getString(R.string.clothingFragment), getString(R.string.clothingFragment));
                    startActivity(intent);
                }

            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_electronics, container, false);
        setupFragmentWidgets(view);
        setupRecyclerView(getBundleData(getArguments()));
        setupFirebase();
        return view;
    }

    private void setupRecyclerView(ArrayList<Products> userViewProductsList) {
        backupUserViewProductsList = new ArrayList<>();
        backupUserViewProductsList.addAll(userViewProductsList);
        productsContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        electronicsProductsAdapter = new ElectronicsProductsAdapter(getActivity()
                , userViewProductsList
                , productsContainer);
        electronicsProductsAdapter.setupOnItemClickListener(this);
        electronicsProductsAdapter.setLoading();
        electronicsProductsAdapter.setLoadDynamicData(this);
        productsContainer.setAdapter(electronicsProductsAdapter);
    }


    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        firebaseMethods = new FirebaseMethods(getActivity(), getString(R.string.electronicsFragment));

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


    private ArrayList<Products> getBundleData(Bundle arguments) {
        ArrayList<Products> userViewProductsList = null;
        if (arguments != null) {
            userViewProductsList = arguments.getParcelableArrayList(getString(R.string.userViewProductsList));
        }
        return userViewProductsList;
    }

    public void setupFragmentWidgets(View view) {
        productsContainer = view.findViewById(R.id.products_container);
    }

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
        this.searchView.setOnQueryTextListener(this);
        filteredList = new ArrayList<>();
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
}
