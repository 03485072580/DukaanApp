package com.example.fasih.dukaanapp.home.interfaces;

import android.view.View;

import com.example.fasih.dukaanapp.models.Products;

/**
 * Created by Fasih on 01/01/19.
 */

public interface OnRecyclerImageSelectedListener {

    void onClickGridImage(int position, View view, Products currentSelectedProduct);

    void onClickGridImage(int position, View view, String Url);
}
