package com.example.fasih.dukaanapp.adapter;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

/**
 * Created by Fasih on 11/08/18.
 */

public class DrawerHandler implements DrawerLayout.DrawerListener {
    private Context mContext;

    public DrawerHandler(Context context) {
        this.mContext = context;
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    //also add functions here to define some logic in case of need
}
