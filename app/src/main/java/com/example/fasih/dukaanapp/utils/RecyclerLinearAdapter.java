package com.example.fasih.dukaanapp.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.fasih.dukaanapp.R;

import java.util.ArrayList;

/**
 * Created by Fasih on 11/17/18.
 */

public class RecyclerLinearAdapter extends RecyclerView.Adapter<RecyclerLinearAdapter.MyViewHolder> {

    private Context mContext;

    public RecyclerLinearAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.snippet_bottom_sale_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public void setData(ArrayList list) {

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView saleItem;

        private MyViewHolder(View itemView) {
            super(itemView);
            saleItem = itemView.findViewById(R.id.saleItem);
        }
    }
}
