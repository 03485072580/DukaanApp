package com.example.fasih.dukaanapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.models.Products;
import com.example.fasih.dukaanapp.utils.SquareImageView;
import com.example.fasih.dukaanapp.utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;

/**
 * Created by Fasih on 04/03/19.
 */

public class CarsFragmentAdapter extends RecyclerView.Adapter<CarsFragmentAdapter.MyViewHolder> {
    private ArrayList<Products> userViewProductsList;

    public CarsFragmentAdapter(ArrayList<Products> userViewProductsList) {
        this.userViewProductsList = userViewProductsList;
    }

    @NonNull
    @Override
    public CarsFragmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_cars_fragment_single_row, parent, false);
        setupUniversalImageLoader(view.getContext());
        return new MyViewHolder(view);
    }

    private void setupUniversalImageLoader(Context context) {
        ImageLoader.getInstance().init(UniversalImageLoader.getConfiguration(context));
    }

    @Override
    public void onBindViewHolder(@NonNull CarsFragmentAdapter.MyViewHolder holder, int position) {

        ImageLoader.getInstance().displayImage(userViewProductsList.get(position).getProduct_image_url()
                , holder.perfectlySquareImage);
        //sellerImage
        holder.productTitle.setText(userViewProductsList.get(position).getProduct_name());
        holder.productDesc.setText(userViewProductsList.get(position).getProduct_description());
        holder.productPrice.setText(userViewProductsList.get(position).getProduct_price());
        //sellingBY
        if (userViewProductsList.get(position).getProduct_rating() == -1) {
            holder.simpleRatingBar.setRating(0f);
        } else {
            holder.simpleRatingBar.setRating(userViewProductsList.get(position).getProduct_rating());
        }
    }

    @Override
    public int getItemCount() {
        return userViewProductsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private SquareImageView perfectlySquareImage;
        private ImageView sellerImage;
        private TextView productTitle, productDesc, productPrice, sellingBy;
        private ScaleRatingBar simpleRatingBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            perfectlySquareImage = itemView.findViewById(R.id.perfectlySquareImage);
            sellerImage = itemView.findViewById(R.id.sellerImage);
            productTitle = itemView.findViewById(R.id.productTitle);
            productDesc = itemView.findViewById(R.id.productDesc);
            productPrice = itemView.findViewById(R.id.productPrice);
            sellingBy = itemView.findViewById(R.id.sellingBy);
            simpleRatingBar = itemView.findViewById(R.id.simpleRatingBar);
        }
    }
}
