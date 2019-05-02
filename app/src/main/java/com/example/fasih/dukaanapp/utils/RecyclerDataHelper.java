package com.example.fasih.dukaanapp.utils;


import android.content.Context;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.customModels.RecyclerSelectedCategory;

import java.util.ArrayList;

public class RecyclerDataHelper {


    public static ArrayList<RecyclerSelectedCategory> getSubCategoryCarsData(Context context)
    {
        ArrayList<RecyclerSelectedCategory> dataList = new ArrayList<>();
        dataList.add(new RecyclerSelectedCategory(R.drawable.honda_logo
                , context.getString(R.string.query_type_Honda)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.suzuki
                , context.getString(R.string.query_type_Suzuki)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.bmw
                , context.getString(R.string.query_type_Toyota)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.toyota
                , context.getString(R.string.query_type_BMW)));
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
        dataList.add(new RecyclerSelectedCategory(R.drawable.lotion
                , context.getString(R.string.query_type_Lotions)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.cream
                , context.getString(R.string.query_type_Creams)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.base
                , context.getString(R.string.query_type_Base)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.makeupkit
                , context.getString(R.string.query_type_MakeupKits)));
        return dataList;
    }

    public static ArrayList<RecyclerSelectedCategory> getSubCategoryElectronicsData(Context context)
    {
        ArrayList<RecyclerSelectedCategory> dataList = new ArrayList<>();
        dataList.add(new RecyclerSelectedCategory(R.drawable.philips
                , context.getString(R.string.query_type_Philips)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.haier
                , context.getString(R.string.query_type_Haier)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.orient
                , context.getString(R.string.query_type_Orient)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.toshiba
                , context.getString(R.string.query_type_Toshiba)));
        return dataList;
    }

    public static ArrayList<RecyclerSelectedCategory> getSubCategoryFragrancesData(Context context)
    {
        ArrayList<RecyclerSelectedCategory> dataList = new ArrayList<>();
        dataList.add(new RecyclerSelectedCategory(R.drawable.j
                , context.getString(R.string.query_type_J_)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.alisbha
                , context.getString(R.string.query_type_Alisha)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.boss
                , context.getString(R.string.query_type_Boss)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.fogg
                , context.getString(R.string.query_type_Fogg)));
        return dataList;
    }

    public static ArrayList<RecyclerSelectedCategory> getSubCategoryJewellaryData(Context context)
    {
        ArrayList<RecyclerSelectedCategory> dataList = new ArrayList<>();
        dataList.add(new RecyclerSelectedCategory(R.drawable.payal
                , context.getString(R.string.query_type_Payal)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.jummer
                , context.getString(R.string.query_type_Jhumar)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.mathapati
                , context.getString(R.string.query_type_Matha_Patti)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.earring
                , context.getString(R.string.query_type_Kanta)));
        return dataList;
    }

    public static ArrayList<RecyclerSelectedCategory> getSubCategoryMobileData(Context context)
    {
        ArrayList<RecyclerSelectedCategory> dataList = new ArrayList<>();
        dataList.add(new RecyclerSelectedCategory(R.drawable.samsung
                , context.getString(R.string.query_type_Samsung)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.oppo
                , context.getString(R.string.query_type_OPPO)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.qmobile
                , context.getString(R.string.query_type_QMobile)));
        dataList.add(new RecyclerSelectedCategory(R.drawable.iphone
                , context.getString(R.string.query_type_IPhone)));
        return dataList;
    }
}
