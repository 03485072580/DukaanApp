package com.example.fasih.dukaanapp.login;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.adapter.SectionPagerAdapter;

public class LoginActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private int TAB_NUM = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupActivityWidgets();
        setupViewPager();
    }

    private void setupViewPager() {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager(), this);
        adapter.setFragments(new UserFragment());
        adapter.setFragments(new ShopFragment());
        adapter.setFragments(new AdminFragment());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(TAB_NUM).setText(getString(R.string.as_user));
        tabLayout.getTabAt(++TAB_NUM).setText(getString(R.string.as_shop));
        tabLayout.getTabAt(++TAB_NUM).setText(getString(R.string.admin));
    }

    private void setupActivityWidgets() {
        viewPager = findViewById(R.id.container_view);
    }
}
