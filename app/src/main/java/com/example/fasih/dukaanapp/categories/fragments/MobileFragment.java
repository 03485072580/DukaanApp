package com.example.fasih.dukaanapp.categories.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.adapter.MobileProductsAdapter;
import com.example.fasih.dukaanapp.categories.actvities.ProductDetailActivity;
import com.example.fasih.dukaanapp.home.interfaces.OnRecyclerImageSelectedListener;

/**
 * Created by Fasih on 01/01/19.
 */

public class MobileFragment extends Fragment implements OnRecyclerImageSelectedListener {

    private RecyclerView productsContainer;

    /**
     * This method is responsible for
     * keeping track of the click events on the categories items
     *
     * @param position
     * @param view     Here view helps to determine which item on the
     *                 layout clicked
     */
    @Override
    public void onClickGridImage(int position, View view) {
        startActivity(new Intent(getActivity(), ProductDetailActivity.class));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mobile, container, false);
        setupRecyclerView(view);
        return view;
    }

    private void setupRecyclerView(View view) {

        productsContainer = view.findViewById(R.id.products_container);
        productsContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        productsContainer.setAdapter(new MobileProductsAdapter(getActivity(), this));
    }

}
