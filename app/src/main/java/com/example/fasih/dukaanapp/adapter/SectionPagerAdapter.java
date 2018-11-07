package com.example.fasih.dukaanapp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Fasih on 11/08/18.
 */

public class SectionPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private ArrayList<Fragment> fragmentList;

    public SectionPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
        fragmentList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void setFragments(Fragment fragment) {
        if (fragment != null)
            fragmentList.add(fragment);
    }
}
