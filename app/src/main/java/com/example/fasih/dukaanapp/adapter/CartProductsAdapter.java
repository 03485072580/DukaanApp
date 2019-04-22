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
import com.example.fasih.dukaanapp.home.interfaces.OnRecyclerImageSelectedListener;
import com.example.fasih.dukaanapp.models.Products;
import com.example.fasih.dukaanapp.utils.RecyclerProgressUpdater;
import com.example.fasih.dukaanapp.utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int VIEW_TYPE_PROGRESS_LOADING = 0, VIEW_TYPE_CURRENT_LAYOUT = 1;

    private Context mContext;
    private OnRecyclerImageSelectedListener imageSelected;
    private ArrayList<Products> userViewProductsList;

    public CartProductsAdapter(Context context
            , ArrayList<Products> userViewProductsList) {

        this.mContext = context;
        this.userViewProductsList = userViewProductsList;
        setupUniversalImageLoader(UniversalImageLoader.getConfiguration(context));
    }

    private void setupUniversalImageLoader(ImageLoaderConfiguration config) {
        ImageLoader.getInstance().init(config);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == userViewProductsList.size()) {
            return VIEW_TYPE_PROGRESS_LOADING;
        }
        return VIEW_TYPE_CURRENT_LAYOUT;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_CURRENT_LAYOUT) {

            View view = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.layout_single_cart_row, parent, false);
            return new MyViewHolder(view);
        }
        if (viewType == VIEW_TYPE_PROGRESS_LOADING) {

            View view = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.layout_recycler_progress, parent, false);
            return new RecyclerProgressUpdater(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            ImageLoader.getInstance().displayImage(userViewProductsList.get(position).getProduct_image_url()
                    , viewHolder.productImage);
            viewHolder.productTitle.setText(userViewProductsList.get(position).getProduct_name());
            viewHolder.productDesc.setText(userViewProductsList.get(position).getProduct_description());
            viewHolder.productPrice.setText(userViewProductsList.get(position).getProduct_price());
            viewHolder.simpleRatingBar.setRating(userViewProductsList.get(position).getProduct_rating());

        }
        if (holder instanceof RecyclerProgressUpdater) {
            RecyclerProgressUpdater progressUpdater = (RecyclerProgressUpdater) holder;
        }
    }

    @Override
    public int getItemCount() {
        return userViewProductsList.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage, sellerImage;
        private CircleImageView cartAdd;
        private TextView productTitle, productDesc, productPrice, sellingBy;
        private ScaleRatingBar simpleRatingBar;

        public MyViewHolder(final View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            sellerImage = itemView.findViewById(R.id.sellerImage);
            cartAdd = itemView.findViewById(R.id.cartAdd);
            productTitle = itemView.findViewById(R.id.productTitle);
            productDesc = itemView.findViewById(R.id.productDesc);
            productPrice = itemView.findViewById(R.id.productPrice);
            sellingBy = itemView.findViewById(R.id.sellingBy);
            simpleRatingBar = itemView.findViewById(R.id.simpleRatingBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageSelected.onClickGridImage(getAdapterPosition()
                            , itemView
                            , userViewProductsList.get(getAdapterPosition()));
                }
            });
        }
    }

    public void setupOnItemClickListener(OnRecyclerImageSelectedListener onRecyclerImageSelectedListener) {

        this.imageSelected = onRecyclerImageSelectedListener;

    }
}
