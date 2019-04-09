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
import com.example.fasih.dukaanapp.home.customModels.RecyclerSelectedCategory;
import com.example.fasih.dukaanapp.home.interfaces.OnRecyclerImageSelectedListener;
import com.example.fasih.dukaanapp.utils.RecyclerGridAdapter;
import com.example.fasih.dukaanapp.utils.RecyclerLinearAdapter;

import java.util.ArrayList;

/**
 * Created by Fasih on 11/17/18.
 */

public class HomeFragment extends Fragment implements OnRecyclerImageSelectedListener {

    private static final int currentFragmentNumber = 0;
    private static final int CARS_FRAGMENT = 0, CLOTHING_FRAGMENT = 1, JEWELLARY_FRAGMENT = 2, MOBILE_FRAGMENT = 3, ELECTRONICS_FRAGMENT = 4, COSMETICS_FRAGMENT = 5, FRAGRANCES_FRAGMENT = 6;
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
        if (position == CARS_FRAGMENT) {
            Intent intent = new Intent(getActivity(), UniqueCategoryActivity.class);
            intent.putExtra("CARS", "CARS");
            intent.putExtra(getString(R.string.carsFragment), getString(R.string.carsFragment));
            startActivity(intent);
        }
        if (position == CLOTHING_FRAGMENT) {
            Intent intent = new Intent(getActivity(), UniqueCategoryActivity.class);
            intent.putExtra("CLOTHING", "CLOTHING");
            intent.putExtra(getString(R.string.clothingFragment), getString(R.string.clothingFragment));
            startActivity(intent);
        }
        if (position == JEWELLARY_FRAGMENT) {
            Intent intent = new Intent(getActivity(), UniqueCategoryActivity.class);
            intent.putExtra("JEWELLARY", "JEWELLARY");
            intent.putExtra(getString(R.string.jewellaryFragment), getString(R.string.jewellaryFragment));
            startActivity(intent);
        }
        if (position == MOBILE_FRAGMENT) {
            Intent intent = new Intent(getActivity(), UniqueCategoryActivity.class);
            intent.putExtra("MOBILES", "MOBILES");
            intent.putExtra(getString(R.string.mobileFragment), getString(R.string.mobileFragment));
            startActivity(intent);
        }
        if (position == ELECTRONICS_FRAGMENT) {
            Intent intent = new Intent(getActivity(), UniqueCategoryActivity.class);
            intent.putExtra("ELECTRONICS", "ELECTRONICS");
            intent.putExtra(getString(R.string.electronicsFragment), getString(R.string.electronicsFragment));
            startActivity(intent);
        }

        if (position == COSMETICS_FRAGMENT) {
            Intent intent = new Intent(getActivity(), UniqueCategoryActivity.class);
            intent.putExtra("COSMETICS", "COSMETICS");
            intent.putExtra(getString(R.string.cosmeticsFragment), getString(R.string.cosmeticsFragment));
            startActivity(intent);
        }
        if (position == FRAGRANCES_FRAGMENT) {
            Intent intent = new Intent(getActivity(), UniqueCategoryActivity.class);
            intent.putExtra("FRAGRANCES", "FRAGRANCES");
            intent.putExtra(getString(R.string.fragrancesFragment), getString(R.string.fragrancesFragment));
            startActivity(intent);
        }
    }

    @Override
    public void onClickGridImage(int position, View view, String Url) {

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
        ArrayList<RecyclerSelectedCategory> selectedCategories = new ArrayList<>();
        selectedCategories.add(new RecyclerSelectedCategory(R.drawable.circle_test, "Cars"));
        selectedCategories.add(new RecyclerSelectedCategory(R.drawable.circle_test, "Clothing"));
        selectedCategories.add(new RecyclerSelectedCategory(R.drawable.circle_test, "Jewellary"));
        selectedCategories.add(new RecyclerSelectedCategory(R.drawable.circle_test, "Mobile"));
        selectedCategories.add(new RecyclerSelectedCategory(R.drawable.circle_test, "Electronics"));
        selectedCategories.add(new RecyclerSelectedCategory(R.drawable.circle_test, "Cosmetics"));
        selectedCategories.add(new RecyclerSelectedCategory(R.drawable.circle_test, "Fragrances"));
        selectedCategories.add(new RecyclerSelectedCategory(R.drawable.circle_test, "Hot Deals"));
        RecyclerGridAdapter adapter = new RecyclerGridAdapter(getActivity(), this, selectedCategories);
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
