package com.example.fasih.dukaanapp.utils;


import android.content.Context;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.customModels.RecyclerSelectedCategory;

import java.util.ArrayList;

public class RecyclerDataHelper {


    public static ArrayList<RecyclerSelectedCategory> getSubCategoryCarsData(Context context)
    {
        ArrayList<RecyclerSelectedCategory> dataList = new ArrayList<>();
        dataList.add(new RecyclerSelectedCategory(R.drawable.ic_clothing
                , context.getString(R.string.query_type_Coat)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.suit
                , context.getString(R.string.query_type_Suits)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.dress
                , context.getString(R.string.query_type_Stitched)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.scarf
                , context.getString(R.string.query_type_UnStitched)));
        return dataList;
    }

    public static ArrayList<RecyclerSelectedCategory> getSubCategoryClothingData(Context context)
    {
        ArrayList<RecyclerSelectedCategory> dataList = new ArrayList<>();
        dataList.add(new RecyclerSelectedCategory(R.drawable.ic_clothing
                , context.getString(R.string.query_type_Coat)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.suit
                , context.getString(R.string.query_type_Suits)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.dress
                , context.getString(R.string.query_type_Stitched)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.scarf
                , context.getString(R.string.query_type_UnStitched)));
        return dataList;
    }

    public static ArrayList<RecyclerSelectedCategory> getSubCategoryCosmeticsData(Context context)
    {
        ArrayList<RecyclerSelectedCategory> dataList = new ArrayList<>();
        dataList.add(new RecyclerSelectedCategory(R.drawable.ic_clothing
                , context.getString(R.string.query_type_Coat)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.suit
                , context.getString(R.string.query_type_Suits)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.dress
                , context.getString(R.string.query_type_Stitched)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.scarf
                , context.getString(R.string.query_type_UnStitched)));
        return dataList;
    }

    public static ArrayList<RecyclerSelectedCategory> getSubCategoryElectronicsData(Context context)
    {
        ArrayList<RecyclerSelectedCategory> dataList = new ArrayList<>();
        dataList.add(new RecyclerSelectedCategory(R.drawable.ic_clothing
                , context.getString(R.string.query_type_Coat)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.suit
                , context.getString(R.string.query_type_Suits)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.dress
                , context.getString(R.string.query_type_Stitched)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.scarf
                , context.getString(R.string.query_type_UnStitched)));
        return dataList;
    }

    public static ArrayList<RecyclerSelectedCategory> getSubCategoryFragrancesData(Context context)
    {
        ArrayList<RecyclerSelectedCategory> dataList = new ArrayList<>();
        dataList.add(new RecyclerSelectedCategory(R.drawable.ic_clothing
                , context.getString(R.string.query_type_Coat)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.suit
                , context.getString(R.string.query_type_Suits)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.dress
                , context.getString(R.string.query_type_Stitched)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.scarf
                , context.getString(R.string.query_type_UnStitched)));
        return dataList;
    }

    public static ArrayList<RecyclerSelectedCategory> getSubCategoryJewellaryData(Context context)
    {
        ArrayList<RecyclerSelectedCategory> dataList = new ArrayList<>();
        dataList.add(new RecyclerSelectedCategory(R.drawable.ic_clothing
                , context.getString(R.string.query_type_Coat)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.suit
                , context.getString(R.string.query_type_Suits)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.dress
                , context.getString(R.string.query_type_Stitched)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.scarf
                , context.getString(R.string.query_type_UnStitched)));
        return dataList;
    }

    public static ArrayList<RecyclerSelectedCategory> getSubCategoryMobileData(Context context)
    {
        ArrayList<RecyclerSelectedCategory> dataList = new ArrayList<>();
        dataList.add(new RecyclerSelectedCategory(R.drawable.ic_clothing
                , context.getString(R.string.query_type_Coat)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.suit
                , context.getString(R.string.query_type_Suits)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.dress
                , context.getString(R.string.query_type_Stitched)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.scarf
                , context.getString(R.string.query_type_UnStitched)));
        return dataList;
    }
}
