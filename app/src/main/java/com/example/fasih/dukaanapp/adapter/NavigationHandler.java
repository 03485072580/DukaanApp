package com.example.fasih.dukaanapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.home.activities.NavigationActivity;

/**
 * Created by Fasih on 11/08/18.
 */

public class NavigationHandler implements NavigationView.OnNavigationItemSelectedListener {
    private Context mContext;

    public NavigationHandler(Context context) {
        this.mContext = context;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.first_item) {

        }
        if (item.getItemId() == R.id.adminPanel) {
            mContext.startActivity(new Intent(mContext, NavigationActivity.class)
                    .putExtra(mContext.getString(R.string.adminFragment)
                            , mContext.getString(R.string.adminFragment)));
        }
        return false;
    }
}
