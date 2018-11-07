package com.example.fasih.dukaanapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.example.fasih.dukaanapp.R;

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
            Log.d("TAG1234", "first_item");
        }
        return false;
    }
}
