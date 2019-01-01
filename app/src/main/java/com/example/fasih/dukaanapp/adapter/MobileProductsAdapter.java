package com.example.fasih.dukaanapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.categories.fragments.MobileFragment;
import com.example.fasih.dukaanapp.home.OnRecyclerImageSelectedListener;

/**
 * Created by Fasih on 01/02/19.
 */

public class MobileProductsAdapter extends RecyclerView.Adapter<MobileProductsAdapter.MyViewHolder> {


    private Context mContext;
    private OnRecyclerImageSelectedListener imageSelected;

    public MobileProductsAdapter(Context context, MobileFragment mobileFragment) {
        this.mContext = context;
        this.imageSelected = mobileFragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.layout_single_mobile_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageSelected.onClickGridImage(getAdapterPosition(), itemView);
                }
            });
        }
    }
}
