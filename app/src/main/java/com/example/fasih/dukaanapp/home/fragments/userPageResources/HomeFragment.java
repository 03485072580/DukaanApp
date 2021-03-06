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
import android.widget.ImageView;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.categories.actvities.UniqueCategoryActivity;
import com.example.fasih.dukaanapp.customModels.RecyclerSelectedCategory;
import com.example.fasih.dukaanapp.home.interfaces.OnRecyclerImageSelectedListener;
import com.example.fasih.dukaanapp.models.Products;
import com.example.fasih.dukaanapp.utils.RecyclerGridAdapter;
import com.example.fasih.dukaanapp.utils.RecyclerLinearAdapter;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

/**
 * Created by Fasih on 11/17/18.
 */

public class HomeFragment extends Fragment implements OnRecyclerImageSelectedListener {

    private static final int currentFragmentNumber = 0;
    private static final int CARS_FRAGMENT = 0, CLOTHING_FRAGMENT = 1, JEWELLARY_FRAGMENT = 2, MOBILE_FRAGMENT = 3, ELECTRONICS_FRAGMENT = 4, COSMETICS_FRAGMENT = 5, FRAGRANCES_FRAGMENT = 6;
    private RecyclerView recyclerView;
    private RecyclerView saleRecyclerVIew;

    CarouselView carouselView;

    int[] sampleImages = {R.drawable.promotion2, R.drawable.holidays_sales, R.drawable.sale, R.drawable.promotional_banner, R.drawable.test};

    /**
     * This method is responsible for
     * keeping track of the click events on the categories items
     *
     * @param position
     * @param view
     */
    @Override
    public void onClickGridImage(int position, View view, Products products) {
        if (position == CARS_FRAGMENT) {
            Intent intent = new Intent(getActivity(), UniqueCategoryActivity.class);
            intent.putExtra(getString(R.string.query_CARS), getString(R.string.query_CARS));
            intent.putExtra(getString(R.string.carsFragment), getString(R.string.carsFragment));
            startActivity(intent);
        }
        if (position == CLOTHING_FRAGMENT) {
            Intent intent = new Intent(getActivity(), UniqueCategoryActivity.class);
            intent.putExtra(getString(R.string.query_CLOTHES), getString(R.string.query_CLOTHES));
            intent.putExtra(getString(R.string.clothingFragment), getString(R.string.clothingFragment));
            startActivity(intent);
        }
        if (position == JEWELLARY_FRAGMENT) {
            Intent intent = new Intent(getActivity(), UniqueCategoryActivity.class);
            intent.putExtra(getString(R.string.query_JEWELLERY), getString(R.string.query_JEWELLERY));
            intent.putExtra(getString(R.string.jewellaryFragment), getString(R.string.jewellaryFragment));
            startActivity(intent);
        }
        if (position == MOBILE_FRAGMENT) {
            Intent intent = new Intent(getActivity(), UniqueCategoryActivity.class);
            intent.putExtra(getString(R.string.query_MOBILES), getString(R.string.query_MOBILES));
            intent.putExtra(getString(R.string.mobileFragment), getString(R.string.mobileFragment));
            startActivity(intent);
        }
        if (position == ELECTRONICS_FRAGMENT) {
            Intent intent = new Intent(getActivity(), UniqueCategoryActivity.class);
            intent.putExtra(getString(R.string.query_ELECTRONICS), getString(R.string.query_ELECTRONICS));
            intent.putExtra(getString(R.string.electronicsFragment), getString(R.string.electronicsFragment));
            startActivity(intent);
        }

        if (position == COSMETICS_FRAGMENT) {
            Intent intent = new Intent(getActivity(), UniqueCategoryActivity.class);
            intent.putExtra(getString(R.string.query_COSMETICS), getString(R.string.query_COSMETICS));
            intent.putExtra(getString(R.string.cosmeticsFragment), getString(R.string.cosmeticsFragment));
            startActivity(intent);
        }
        if (position == FRAGRANCES_FRAGMENT) {
            Intent intent = new Intent(getActivity(), UniqueCategoryActivity.class);
            intent.putExtra(getString(R.string.query_FRAGRANCES), getString(R.string.query_FRAGRANCES));
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

        carouselView = (CarouselView) view.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);

        setupFragmentWidgets(view);
        setupRecyclerView();


        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
            }
        });

        return view;
    }


    private void setupRecyclerView() {
        ArrayList<RecyclerSelectedCategory> selectedCategories = new ArrayList<>();
        selectedCategories.add(new RecyclerSelectedCategory(R.drawable.ic_car, "Cars"));
        selectedCategories.add(new RecyclerSelectedCategory(R.drawable.ic_clothing, "Clothing"));
        selectedCategories.add(new RecyclerSelectedCategory(R.drawable.ic_ring, "Jewellary"));
        selectedCategories.add(new RecyclerSelectedCategory(R.drawable.ic_smartphone, "Mobile"));
        selectedCategories.add(new RecyclerSelectedCategory(R.drawable.ic_refrigerator, "Electronics"));
        selectedCategories.add(new RecyclerSelectedCategory(R.drawable.ic_deodorant, "Cosmetics"));
        selectedCategories.add(new RecyclerSelectedCategory(R.drawable.ic_frag, "Fragrances"));
        selectedCategories.add(new RecyclerSelectedCategory(R.drawable.ic_shirt, "Hot Deals"));
        RecyclerGridAdapter adapter = new RecyclerGridAdapter(getActivity(), this, selectedCategories);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);


        RecyclerLinearAdapter saleAdapter = new RecyclerLinearAdapter(getActivity());
        LinearLayoutManager manager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        saleRecyclerVIew.setLayoutManager(manager1);
        saleRecyclerVIew.setAdapter(saleAdapter);
    }

    public void setupFragmentWidgets(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        saleRecyclerVIew = view.findViewById(R.id.saleRecyclerView);
    }


}
