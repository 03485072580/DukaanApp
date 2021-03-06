package com.example.fasih.dukaanapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.categories.interfaces.LoadDynamicData;
import com.example.fasih.dukaanapp.home.interfaces.OnRecyclerImageSelectedListener;
import com.example.fasih.dukaanapp.models.Products;
import com.example.fasih.dukaanapp.utils.FirebaseMethods;
import com.example.fasih.dukaanapp.utils.RecyclerProgressUpdater;
import com.example.fasih.dukaanapp.utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Fasih on 04/13/19.
 */

public class CosmeticsProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_PROGRESS_LOADING = 0, VIEW_TYPE_CURRENT_LAYOUT = 1;
    private int threshHold = 5;
    private Context mContext;
    private OnRecyclerImageSelectedListener imageSelected;
    private ArrayList<Products> userViewProductsList;
    private LoadDynamicData loadDynamicData;
    private Boolean isLoading;

    private int pastVisibleItems = 0, visibleItemCount = 0, totalItemCount = 0;

    private FirebaseMethods firebaseMethods;

    public CosmeticsProductsAdapter(Context context
            , ArrayList<Products> userViewProductsList
            , RecyclerView recyclerView) {
        this.mContext = context;
        this.userViewProductsList = userViewProductsList;
        this.firebaseMethods = new FirebaseMethods(mContext
                , mContext.getString(R.string.mobileProductsAdapter));
        setupUniversalImageLoader(UniversalImageLoader.getConfiguration(context));
        setupScrollListner((LinearLayoutManager) recyclerView.getLayoutManager(), recyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_CURRENT_LAYOUT) {

            View view = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.layout_single_mobile_row, parent, false);
            return new CosmeticsProductsAdapter.MyViewHolder(view);
        }
        if (viewType == VIEW_TYPE_PROGRESS_LOADING) {

            View view = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.layout_recycler_progress, parent, false);
            return new RecyclerProgressUpdater(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == userViewProductsList.size() ) {
            return VIEW_TYPE_PROGRESS_LOADING;
        }
        return VIEW_TYPE_CURRENT_LAYOUT;
    }

    @Override
    public int getItemCount() {
        return userViewProductsList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CosmeticsProductsAdapter.MyViewHolder) {
            CosmeticsProductsAdapter.MyViewHolder viewHolder = (CosmeticsProductsAdapter.MyViewHolder) holder;
            ImageLoader.getInstance().displayImage(userViewProductsList.get(position).getProduct_image_url()
                    , viewHolder.productImage);
            viewHolder.productTitle.setText(userViewProductsList.get(position).getProduct_name());
            viewHolder.productDesc.setText(userViewProductsList.get(position).getProduct_description());
            viewHolder.productPrice.setText(userViewProductsList.get(position).getProduct_price());
            viewHolder.simpleRatingBar.setRating(userViewProductsList.get(position).getProduct_rating());
            setupSellerViews(viewHolder, userViewProductsList.get(position).getShop_id());
        }
        if (holder instanceof RecyclerProgressUpdater) {
            RecyclerProgressUpdater progressUpdater = (RecyclerProgressUpdater) holder;
        }


    }

    private void setupSellerViews(CosmeticsProductsAdapter.MyViewHolder viewHolder , String shop_id) {

        if(shop_id !=null)
            firebaseMethods.setSellerViews(viewHolder, shop_id);
    }

    public void setLoadDynamicData(LoadDynamicData loadDynamicData) {
        this.loadDynamicData = loadDynamicData;
    }

    private void setupScrollListner(final LinearLayoutManager manager, RecyclerView recyclerView) {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = manager.getChildCount();
                totalItemCount = manager.getItemCount();
                pastVisibleItems = manager.findLastVisibleItemPosition();

                threshHold = manager.findLastVisibleItemPosition();
                if (dy > 0) {

                    if (!isLoading && (totalItemCount - 1) == threshHold) {

                        if (loadDynamicData != null) {
                            loadDynamicData.onRequestData(userViewProductsList);
                            isLoading = true;
                        }
                    }
                }
            }
        });

    }

    public void setLoading() {
        isLoading = false;
    }

    private void setupUniversalImageLoader(ImageLoaderConfiguration config) {
        ImageLoader.getInstance().init(config);
    }

    public void setLoading(boolean b) {
        this.isLoading = b;
    }

    public void setFilteredList(ArrayList<Products> filteredList) {
        userViewProductsList.clear();
        userViewProductsList.addAll(filteredList);
        notifyDataSetChanged();
    }

    public void setupOnItemClickListener(OnRecyclerImageSelectedListener onRecyclerImageSelectedListener) {

        this.imageSelected = onRecyclerImageSelectedListener;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        public ImageView sellerImage;
        private CircleImageView cartAdd;
        private TextView productTitle, productDesc, productPrice;
        public TextView sellingBy;
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

            cartAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    firebaseMethods.setupUserWishlistProducts(userViewProductsList.get(getAdapterPosition()));
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageSelected.onClickGridImage(getAdapterPosition(), itemView, userViewProductsList.get(getAdapterPosition()));
                }
            });
        }
    }
}
