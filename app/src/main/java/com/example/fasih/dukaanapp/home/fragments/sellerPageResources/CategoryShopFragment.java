package com.example.fasih.dukaanapp.home.fragments.sellerPageResources;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.fasih.dukaanapp.R;

/**
 * Created by Fasih on 02/15/19.
 */

public class CategoryShopFragment extends Fragment {

    private ImageView hamburgerDrawerIcon;
    private DrawerLayout drawerLayoutCategoryShop;
    private NavigationView navigationViewCategoryShop;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_shop, container, false);
        setupFragmentWidgets(view);
        return view;
    }

    private void setupFragmentWidgets(View view) {
        drawerLayoutCategoryShop = view.findViewById(R.id.drawerLayoutCategoryShop);
        navigationViewCategoryShop = view.findViewById(R.id.navigationViewCategoryShop);
        hamburgerDrawerIcon = view.findViewById(R.id.hamburgerDrawerIcon);

        hamburgerDrawerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayoutCategoryShop.isDrawerOpen(navigationViewCategoryShop)) {
                    drawerLayoutCategoryShop.closeDrawer(navigationViewCategoryShop);
                } else {
                    drawerLayoutCategoryShop.openDrawer(navigationViewCategoryShop, true);
                }
            }
        });
    }
}
