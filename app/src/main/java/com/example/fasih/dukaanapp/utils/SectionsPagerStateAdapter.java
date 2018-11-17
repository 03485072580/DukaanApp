package com.example.fasih.dukaanapp.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fasih on 11/17/18.
 */

public class SectionsPagerStateAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> list;

    private Map<Integer, Fragment> queryFragmnetsByNumber;
    private Map<String, Fragment> queryFragmentByNames;

    public SectionsPagerStateAdapter(FragmentManager fm) {
        super(fm);
        queryFragmentByNames = new HashMap<>();
        queryFragmnetsByNumber = new HashMap<>();
        list = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void setFragment(Fragment fragment) {
        list.add(fragment);
    }

    public void setList(List list, int currentFragmentNumber) {

    }
}
