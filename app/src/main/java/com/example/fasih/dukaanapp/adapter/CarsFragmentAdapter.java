package com.example.fasih.dukaanapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.categories.fragments.CarsFragment;
import com.example.fasih.dukaanapp.categories.interfaces.LoadDynamicData;
import com.example.fasih.dukaanapp.home.interfaces.OnRecyclerImageSelectedListener;
import com.example.fasih.dukaanapp.models.Products;
import com.example.fasih.dukaanapp.utils.FirebaseMethods;
import com.example.fasih.dukaanapp.utils.RecyclerProgressUpdater;
import com.example.fasih.dukaanapp.utils.SquareImageView;
import com.example.fasih.dukaanapp.utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Fasih on 04/03/19.
 */

public class CarsFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_CURRENT = 0, VIEW_TYPE_RECYCLER_PROGRESS_BAR = 1;
    private Boolean isLoading;
    private int visibleThreshHold = 1;
    private Context mContext;
    private LoadDynamicData loadData;
    private ArrayList<Products> userViewProductsList;
    private OnRecyclerImageSelectedListener imageSelected;

    private FirebaseMethods firebaseMethods;


    public CarsFragmentAdapter(Context context
            , final ArrayList<Products> userViewProductsList
            , RecyclerView recyclerView) {
        this.userViewProductsList = userViewProductsList;
        this.mContext = context;
        this.firebaseMethods = new FirebaseMethods(mContext
                , mContext.getString(R.string.mobileProductsAdapter));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalListItems = recyclerView.getLayoutManager().getItemCount();
                int lastVisibleItem = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                if (!isLoading && totalListItems <= lastVisibleItem + visibleThreshHold) {

                    if (loadData != null) {

                        loadData.onRequestData(userViewProductsList);
                        isLoading = true;
                    }
                }

            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return userViewProductsList.get(position) == null ? VIEW_TYPE_RECYCLER_PROGRESS_BAR : VIEW_TYPE_CURRENT;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_CURRENT) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_cars_fragment_single_row, parent, false);
            setupUniversalImageLoader(view.getContext());
            return new ViewHolder(view);
        }
        if (viewType == VIEW_TYPE_RECYCLER_PROGRESS_BAR) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_recycler_progress, parent, false);
            setupUniversalImageLoader(view.getContext());
            return new RecyclerProgressUpdater(view);
        }
        return null;
    }


    private void setupUniversalImageLoader(Context context) {
        ImageLoader.getInstance().init(UniversalImageLoader.getConfiguration(context));
    }


    public void setInitialLoadingProgress() {
        isLoading = false;
    }

    public void setInitialLoadingProgress(Boolean isLoading) {
        this.isLoading = isLoading;
    }


    public void setLoadDynamicData(CarsFragment carsFragmentInstance) {
        this.loadData = carsFragmentInstance;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof CarsFragmentAdapter.ViewHolder) {
            //Down Casting Example ex holder (parent)--> CarsFragmentAdapter.ViewHolder ==>
            ViewHolder myViewHolder = (ViewHolder) holder;

            ImageLoader.getInstance().displayImage(userViewProductsList.get(position).getProduct_image_url()
                    , myViewHolder.perfectlySquareImage);
            //sellerImage
            myViewHolder.productTitle.setText(userViewProductsList.get(position).getProduct_name());
            myViewHolder.productDesc.setText(userViewProductsList.get(position).getProduct_description());
            myViewHolder.productPrice.setText(userViewProductsList.get(position).getProduct_price());
            myViewHolder.simpleRatingBar.setRating(userViewProductsList.get(position).getProduct_rating());
            setupSellerViews(myViewHolder, userViewProductsList.get(position).getShop_id());
            //sellingBY
//            if (userViewProductsList.get(position).getProduct_rating() == -1) {
//                myViewHolder.simpleRatingBar.setRating(0f);
//            } else {
//                myViewHolder.simpleRatingBar.setRating(userViewProductsList.get(position).getProduct_rating());
//            }

        }
        //do Progress Updating Stuff Here
        if (holder instanceof RecyclerProgressUpdater) {

            RecyclerProgressUpdater progressUpdater = (RecyclerProgressUpdater) holder;
        }


    }

    private void setupSellerViews(ViewHolder viewHolder, String shop_id) {

        if (shop_id != null)
            firebaseMethods.setSellerViews(viewHolder, shop_id);
    }

    @Override
    public int getItemCount() {
        return userViewProductsList.size();
    }

    public void setFilteredList(ArrayList<Products> filteredList) {
        userViewProductsList.clear();
        userViewProductsList.addAll(filteredList);
        notifyDataSetChanged();
    }

    public void setupOnItemClickListener(OnRecyclerImageSelectedListener onRecyclerImageSelectedListener) {

        this.imageSelected = onRecyclerImageSelectedListener;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private SquareImageView perfectlySquareImage;
        public ImageView sellerImage;
        private TextView productTitle, productDesc, productPrice;
        private CircleImageView addCart;
        public TextView sellingBy;
        private ScaleRatingBar simpleRatingBar;

        public ViewHolder(final View itemView) {
            super(itemView);
            perfectlySquareImage = itemView.findViewById(R.id.perfectlySquareImage);
            sellerImage = itemView.findViewById(R.id.sellerImage);
            productTitle = itemView.findViewById(R.id.productTitle);
            productDesc = itemView.findViewById(R.id.productDesc);
            productPrice = itemView.findViewById(R.id.productPrice);
            sellingBy = itemView.findViewById(R.id.sellingBy);
            addCart = itemView.findViewById(R.id.addCart);
            simpleRatingBar = itemView.findViewById(R.id.simpleRatingBar);

            addCart.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public long getItemId(int position) {
        return position;
    }


}
