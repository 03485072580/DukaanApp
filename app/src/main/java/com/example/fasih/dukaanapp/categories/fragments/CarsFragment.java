package com.example.fasih.dukaanapp.categories.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.adapter.CarsFragmentAdapter;
import com.example.fasih.dukaanapp.models.Products;

import java.util.ArrayList;

/**
 * Created by Fasih on 01/01/19.
 */

public class CarsFragment extends Fragment {

    private RecyclerView carsProductsContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cars, container, false);
        setupFragmentWidgets(view);
        setupRecyclerView(getBundleData(getArguments()));
        return view;
    }

    private ArrayList<Products> getBundleData(Bundle arguments) {
        ArrayList<Products> userViewProductsList = null;
        if (arguments != null) {
            userViewProductsList = arguments.getParcelableArrayList(getString(R.string.userViewProductsList));
            Log.d("TAG1234", "getBundleData: " + userViewProductsList.toString());
        }
        return userViewProductsList;
    }

    public void setupRecyclerView(ArrayList<Products> userViewProductsList) {

        if (userViewProductsList != null)
            if (!userViewProductsList.isEmpty()) {
                CarsFragmentAdapter adapter = new CarsFragmentAdapter(userViewProductsList);
                carsProductsContainer.setLayoutManager(new GridLayoutManager(getContext(), 2));
                carsProductsContainer.setAdapter(adapter);

            }
    }

    public void setupFragmentWidgets(View view) {
        carsProductsContainer = view.findViewById(R.id.carsProductsContainer);
    }
}
