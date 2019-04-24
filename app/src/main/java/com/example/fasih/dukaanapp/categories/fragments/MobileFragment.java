package com.example.fasih.dukaanapp.categories.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.adapter.MobileProductsAdapter;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Fasih on 01/01/19.
 */

public class MobileFragment extends Fragment implements OnRecyclerImageSelectedListener
        , LoadDynamicData
        , KeepHandleRecyclerList
        , SearchView.OnQueryTextListener {

    private SearchView searchView;
    private ArrayList<Products> userViewProductsList, filteredList, backupUserViewProductsList;
    private RecyclerView productsContainer;
    private MobileProductsAdapter mobileProductsAdapter;
    private RecyclerView subCategoriesListRecyclerView;
    private SubCategoriesListAdapter subCategoriesAdapter;
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
        mobileProductsAdapter.setLoading(true);
        filteredList.clear();

        if (newText.equals("")) {
            filteredList.addAll(backupUserViewProductsList);
            mobileProductsAdapter.setLoading(false);
        }

        for (Products product : backupUserViewProductsList) {

            if (StringManipulations.toLowerCase(product.getProduct_name())
                    .contains(StringManipulations.toLowerCase(newText))) {
                filteredList.add(product);
            }
        }
        mobileProductsAdapter.setFilteredList(filteredList);
        return true;
    }

    @Override
    public void onUpdateRecyclerList(ArrayList<Products> userViewProductsList) {
        mobileProductsAdapter.notifyDataSetChanged();
        mobileProductsAdapter.setLoading();
    }

    @Override
    public void onRequestData(ArrayList<Products> userViewProductsList) {
        firebaseMethods.setListenerForUpdatingRecyclerView(this);
        firebaseMethods.queryProducts(getString(R.string.query_MOBILES)
                , userViewProductsList
                , mobileProductsAdapter);
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

                if(subCategoriesAdapter
                        .getRecyclerSelectedCategoryObject(position)
                        .getCategoryImageResource() ==
                        R.drawable.ic_deodorant)
                {
                    Intent intent = new Intent(getActivity(), SubCategoryActivity.class);
                    intent.putExtra(getString(R.string.query_type_Coat), getString(R.string.query_type_Coat));
                    intent.putExtra(getString(R.string.clothingFragment), getString(R.string.clothingFragment));
                    startActivity(intent);
                }

                if(subCategoriesAdapter
                        .getRecyclerSelectedCategoryObject(position)
                        .getCategoryImageResource() ==
                        R.drawable.ic_clothing)
                {
                    Intent intent = new Intent(getActivity(), SubCategoryActivity.class);
                    intent.putExtra(getString(R.string.query_type_Suits), getString(R.string.query_type_Suits));
                    intent.putExtra(getString(R.string.clothingFragment), getString(R.string.clothingFragment));
                    startActivity(intent);
                }

                if(subCategoriesAdapter
                        .getRecyclerSelectedCategoryObject(position)
                        .getCategoryImageResource() ==
                        R.drawable.ic_car)
                {
                    Intent intent = new Intent(getActivity(), SubCategoryActivity.class);
                    intent.putExtra(getString(R.string.query_type_Stitched), getString(R.string.query_type_Stitched));
                    intent.putExtra(getString(R.string.clothingFragment), getString(R.string.clothingFragment));
                    startActivity(intent);
                }
                if(subCategoriesAdapter
                        .getRecyclerSelectedCategoryObject(position)
                        .getCategoryImageResource() ==
                        R.drawable.ic_ring)
                {
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
        View view = inflater.inflate(R.layout.fragment_mobile, container, false);
        setupFragmentWidgets(view);
        setupRecyclerView(getBundleData(getArguments()));
        setupFirebase();
        return view;
    }

    private void setupRecyclerView(ArrayList<Products> userViewProductsList) {

        backupUserViewProductsList = new ArrayList<>();
        backupUserViewProductsList.addAll(userViewProductsList);
        productsContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        mobileProductsAdapter = new MobileProductsAdapter(getActivity()
                , userViewProductsList
                , productsContainer);
        mobileProductsAdapter.setupOnItemClickListener(this);
        mobileProductsAdapter.setLoading();
        mobileProductsAdapter.setLoadDynamicData(this);
        productsContainer.setAdapter(mobileProductsAdapter);
    }


    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        firebaseMethods = new FirebaseMethods(getActivity(), getString(R.string.mobileFragment));

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
}
