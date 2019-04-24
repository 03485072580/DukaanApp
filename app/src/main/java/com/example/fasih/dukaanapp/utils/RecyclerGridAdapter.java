package com.example.fasih.dukaanapp.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.customModels.RecyclerSelectedCategory;
import com.example.fasih.dukaanapp.home.fragments.userPageResources.HomeFragment;
import com.example.fasih.dukaanapp.home.interfaces.OnRecyclerImageSelectedListener;
import com.example.fasih.dukaanapp.models.Products;

import java.util.ArrayList;

/**
 * Created by Fasih on 11/17/18.
 */

public class RecyclerGridAdapter extends RecyclerView.Adapter<RecyclerGridAdapter.MyViewHolder> {
    private Context mContext;
    private OnRecyclerImageSelectedListener imageSelected;
    private ArrayList<RecyclerSelectedCategory> currentDataSet;

    public RecyclerGridAdapter(Context mContext
            , HomeFragment homeFragment
            , ArrayList<RecyclerSelectedCategory> currentDataSet) {

        this.mContext = mContext;
        this.imageSelected = homeFragment;
        this.currentDataSet = currentDataSet;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyViewHolder holder = null;
        try {
            View view = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.snippet_middle_category_item, parent, false);
            holder = new MyViewHolder(view);

        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.imgDescription.setText(currentDataSet.get(position).getCategoryTvText());
        holder.recycler_grid_image.setImageResource(currentDataSet.get(position).getCategoryImageResource());
    }

    //provide given dynamic data size here
    @Override
    public int getItemCount() {
        return 8;
    }

    public Fragment getFragmentByNumber(int currentFragmentNumber) {
        return null;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView recycler_grid_image;
        private TextView imgDescription;

        private MyViewHolder(final View itemView) {
            super(itemView);
            recycler_grid_image = itemView.findViewById(R.id.recycler_grid_image);
            imgDescription = itemView.findViewById(R.id.imgDescription);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageSelected.onClickGridImage(getAdapterPosition(), itemView, new Products());
                }
            });
        }
    }

}
