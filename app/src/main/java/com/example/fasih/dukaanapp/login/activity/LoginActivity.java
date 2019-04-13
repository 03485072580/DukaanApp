package com.example.fasih.dukaanapp.login.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.adapter.SectionPagerAdapter;
import com.example.fasih.dukaanapp.login.fragment.ForgotPasswordFragment;
import com.example.fasih.dukaanapp.login.fragment.ShopFragment;
import com.example.fasih.dukaanapp.login.fragment.UserFragment;
import com.example.fasih.dukaanapp.login.interfaces.AttachedFragment;
import com.example.fasih.dukaanapp.login.interfaces.ResolveContainerConflicts;
import com.google.android.gms.ads.MobileAds;

public class LoginActivity extends AppCompatActivity implements AttachedFragment {

    private ViewPager viewPager;
    private int TAB_NUM = 0;
    private ResolveContainerConflicts resolver;
    private SectionPagerAdapter adapter;
    private ForgotPasswordFragment forgotPassword;
    private FrameLayout containerFrameLayout;
    private RelativeLayout containerFragmentLayout;

    @Override
    public void onFragmentAttached(Fragment fragment) {
        this.forgotPassword = (ForgotPasswordFragment) fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MobileAds.initialize(this, getString(R.string.ADMOB_APP_ID));
        setupActivityWidgets();
        setupViewPager();
        keepTrackOfFragments();
    }

    private void keepTrackOfFragments() {
        if (viewPager.getCurrentItem() == 0) {
            //UserFragment
            resolver = (UserFragment) adapter.getItem(0);
            resolver.setContainerLayout(containerFrameLayout, containerFragmentLayout);
        }
        if (viewPager.getCurrentItem() == 1) {
            //CategoryShopFragment
            resolver = (ShopFragment) adapter.getItem(1);
            resolver.setContainerLayout(containerFrameLayout, containerFragmentLayout);
        }

    }

    private void setupViewPager() {
        adapter = new SectionPagerAdapter(getSupportFragmentManager(), this);
        adapter.setFragments(new UserFragment());
        adapter.setFragments(new ShopFragment());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(TAB_NUM).setText(getString(R.string.as_user));
        tabLayout.getTabAt(++TAB_NUM).setText(getString(R.string.as_shop));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                keepTrackOfFragments();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupActivityWidgets() {
        viewPager = findViewById(R.id.container_view);
        containerFrameLayout = findViewById(R.id.forgotPasswordContainer);
        containerFragmentLayout = findViewById(R.id.container_viewPager_Fragments);
    }

    @Override
    public void onBackPressed() {
        if (forgotPassword != null) {
            containerFragmentLayout.setVisibility(View.VISIBLE);
            containerFrameLayout.setVisibility(View.GONE);
        }
        super.onBackPressed();
    }
}
