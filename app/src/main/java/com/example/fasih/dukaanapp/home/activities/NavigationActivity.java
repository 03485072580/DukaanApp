package com.example.fasih.dukaanapp.home.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.home.fragments.sellerPageResources.CategoryShopHelpFragment;
import com.example.fasih.dukaanapp.home.fragments.sellerPageResources.CategoryShopOrderFragment;
import com.example.fasih.dukaanapp.home.fragments.sellerPageResources.CategoryShopProfileFragment;
import com.example.fasih.dukaanapp.home.fragments.sellerPageResources.CategoryShopSupportFragment;
import com.example.fasih.dukaanapp.home.fragments.userPageResources.AdminFragment;

public class NavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        setupNavigation(getIntent());
    }

    private void setupNavigation(Intent intent) {
        //Dynamically Replace the Fragment to the NavigationActivity Screen

        if (intent != null) {
            if (intent.hasExtra(getString(R.string.adminFragment)))
                if (intent.getStringExtra(getString(R.string.adminFragment)).equals(getString(R.string.adminFragment))) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.navigationFragmentContainer
                                    , new AdminFragment()
                                    , getString(R.string.adminFragment))
                            .commit();
                }
            if (intent.hasExtra(getString(R.string.categoryShopProfileFragment)))
                if (intent.getStringExtra(getString(R.string.categoryShopProfileFragment)).equals(getString(R.string.categoryShopProfileFragment))) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.navigationFragmentContainer
                                    , new CategoryShopProfileFragment()
                                    , getString(R.string.categoryShopProfileFragment))
                            .commitAllowingStateLoss();
                }
            if (intent.hasExtra(getString(R.string.categoryShopOrderFragment)))
                if (intent.getStringExtra(getString(R.string.categoryShopOrderFragment)).equals(getString(R.string.categoryShopOrderFragment))) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.navigationFragmentContainer
                                    , new CategoryShopOrderFragment()
                                    , getString(R.string.categoryShopOrderFragment))
                            .commitAllowingStateLoss();
                }
            if (intent.hasExtra(getString(R.string.categoryShopHelpFragment)))
                if (intent.getStringExtra(getString(R.string.categoryShopHelpFragment)).equals(getString(R.string.categoryShopHelpFragment))) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.navigationFragmentContainer
                                    , new CategoryShopHelpFragment()
                                    , getString(R.string.categoryShopHelpFragment))
                            .commitAllowingStateLoss();
                }

            if (intent.hasExtra(getString(R.string.categoryShopSupportFragment)))
                if (intent.getStringExtra(getString(R.string.categoryShopSupportFragment)).equals(getString(R.string.categoryShopSupportFragment))) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.navigationFragmentContainer
                                    , new CategoryShopSupportFragment()
                                    , getString(R.string.categoryShopSupportFragment))
                            .commitAllowingStateLoss();
                }
        }
    }

}
