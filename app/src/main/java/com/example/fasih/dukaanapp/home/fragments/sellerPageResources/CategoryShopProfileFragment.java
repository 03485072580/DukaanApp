package com.example.fasih.dukaanapp.home.fragments.sellerPageResources;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fasih.dukaanapp.R;

/**
 * Created by Fasih on 02/16/19.
 */

public class CategoryShopProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_shop_profile, container, false);
        return view;
    }
}
