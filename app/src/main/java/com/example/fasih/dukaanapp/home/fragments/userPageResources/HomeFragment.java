package com.example.fasih.dukaanapp.home.fragments.userPageResources;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.categories.actvities.UniqueCategoryActivity;
import com.example.fasih.dukaanapp.home.interfaces.OnRecyclerImageSelectedListener;
import com.example.fasih.dukaanapp.utils.RecyclerGridAdapter;
import com.example.fasih.dukaanapp.utils.RecyclerLinearAdapter;

/**
 * Created by Fasih on 11/17/18.
 */

public class HomeFragment extends Fragment implements OnRecyclerImageSelectedListener {

    private static final int currentFragmentNumber = 0;
    private RecyclerView recyclerView;
    private RecyclerView saleRecyclerVIew;

    /**
     * This method is responsible for
     * keeping track of the click events on the categories items
     *
     * @param position
     * @param view
     */
    @Override
    public void onClickGridImage(int position, View view) {
        startActivity(new Intent(getActivity(), UniqueCategoryActivity.class));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setupFragmentWidgets(view);
        setupRecyclerView();
        return view;
    }

    private void setupRecyclerView() {
        RecyclerGridAdapter adapter = new RecyclerGridAdapter(getActivity(),this);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);


        RecyclerLinearAdapter saleAdapter = new RecyclerLinearAdapter(getActivity());
        LinearLayoutManager manager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        saleRecyclerVIew.setLayoutManager(manager1);
        saleRecyclerVIew.setAdapter(saleAdapter);
    }

    public void setupFragmentWidgets(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        saleRecyclerVIew = view.findViewById(R.id.saleRecyclerView);
    }

}
