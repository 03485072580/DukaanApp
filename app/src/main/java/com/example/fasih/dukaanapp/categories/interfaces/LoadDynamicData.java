package com.example.fasih.dukaanapp.categories.interfaces;

import com.example.fasih.dukaanapp.models.Products;

import java.util.ArrayList;

/**
 * Created by Fasih on 04/09/19.
 */

public interface LoadDynamicData {

    void onRequestData(ArrayList<Products> userViewProductsList);
}
