package com.example.fasih.dukaanapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.categories.interfaces.LoadDynamicData;
import com.example.fasih.dukaanapp.models.Products;
import com.example.fasih.dukaanapp.utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

/**
 * Created by Fasih on 04/11/19.
 */

public class ClothingProductsAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_EVEN_LAYOUT = 0, VIEW_TYPE_ODD_LAYOUT = 1;
    private final int threshHold = 5;
    private Context mContext;
    private ArrayList<Products> userViewProductsList;
    private LoadDynamicData loadDynamicData;
    private Boolean isLoading;
    private int currentPositionParsed = -1;

    public ClothingProductsAdapter(Context context
            , ArrayList<Products> userViewProductsList
            , RecyclerView recyclerView) {

        this.userViewProductsList = userViewProductsList;
        this.mContext = context;
        setupUniversalImageLoader(UniversalImageLoader.getConfiguration(context));
        setupScrollListener((StaggeredGridLayoutManager) recyclerView.getLayoutManager(), recyclerView);

    }

    @Override
    public int getItemViewType(int position) {
        Log.d("TAG1234", "getItemViewType: " + position);
        if (position % 2 == 0) {
            return VIEW_TYPE_EVEN_LAYOUT;
        }
        return VIEW_TYPE_ODD_LAYOUT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (VIEW_TYPE_EVEN_LAYOUT == viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_clothing_row, parent, false);
            return new ClothingViewHolder(view);
        }

        if (VIEW_TYPE_ODD_LAYOUT == viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_clothing_invert_row, parent, false);
            return new ClothingInvertViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        try {

            if (holder instanceof ClothingViewHolder) {
                ClothingViewHolder viewHolder = (ClothingViewHolder) holder;
                updateRecyclerViews(currentPositionParsed + 1, viewHolder);

            }

            if (holder instanceof ClothingInvertViewHolder) {
                ClothingInvertViewHolder viewHolder = (ClothingInvertViewHolder) holder;
                updateRecyclerViews(currentPositionParsed + 1, viewHolder);
            }
        } catch (IndexOutOfBoundsException exc) {
            exc.printStackTrace();
        }


    }

    private void updateRecyclerViews(int position, ClothingViewHolder viewHolder) {
        if (userViewProductsList.get(position) != null && position % 4 == 0) {

            ImageLoader.getInstance().displayImage(userViewProductsList
                            .get(position)
                            .getProduct_image_url()
                    , viewHolder.productImage0);
            currentPositionParsed = position;
        }
        if (userViewProductsList.get(position + 1) != null && (position + 1) % 4 == 1) {

            ImageLoader.getInstance().displayImage(userViewProductsList
                            .get(position + 1)
                            .getProduct_image_url()
                    , viewHolder.productImage1);
            currentPositionParsed = position + 1;
        }
        if (userViewProductsList.get(position + 2) != null && (position + 2) % 4 == 2) {

            ImageLoader.getInstance().displayImage(userViewProductsList
                            .get(position + 2)
                            .getProduct_image_url()
                    , viewHolder.productImage2);
            currentPositionParsed = position + 2;
        }
        if (userViewProductsList.get(position + 3) != null && (position + 3) % 4 == 3) {

            ImageLoader.getInstance().displayImage(userViewProductsList
                            .get(position + 3)
                            .getProduct_image_url()
                    , viewHolder.productImage3);
            currentPositionParsed = position + 3;
        }
    }

    private void updateRecyclerViews(int position, ClothingInvertViewHolder viewHolder) {
        Log.d("TAG1234", "updateRecyclerViews: " + position);
        if (userViewProductsList.get(position) != null && position % 4 == 0) {

            ImageLoader.getInstance().displayImage(userViewProductsList
                            .get(position)
                            .getProduct_image_url()
                    , viewHolder.productImage0);
            currentPositionParsed = position;
        }
        if (userViewProductsList.get(position + 1) != null && (position + 1) % 4 == 1) {

            ImageLoader.getInstance().displayImage(userViewProductsList
                            .get(position + 1)
                            .getProduct_image_url()
                    , viewHolder.productImage1);
            currentPositionParsed = position + 1;
        }
        if (userViewProductsList.get(position + 2) != null && (position + 2) % 4 == 2) {

            ImageLoader.getInstance().displayImage(userViewProductsList
                            .get(position + 2)
                            .getProduct_image_url()
                    , viewHolder.productImage2);
            currentPositionParsed = position + 2;
        }
        if (userViewProductsList.get(position + 3) != null && (position + 3) % 4 == 3) {

            ImageLoader.getInstance().displayImage(userViewProductsList
                            .get(position + 3)
                            .getProduct_image_url()
                    , viewHolder.productImage3);
            currentPositionParsed = position + 3;
        }
    }

    @Override
    public int getItemCount() {
        return (int) Math.ceil(userViewProductsList.size() / 4.0);
    }

    private void setupUniversalImageLoader(ImageLoaderConfiguration config) {
        ImageLoader.getInstance().init(config);
    }

    private void setupScrollListener(final StaggeredGridLayoutManager manager, RecyclerView recyclerView) {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                if (!isLoading && manager.getItemCount() >= manager.findLastVisibleItemPosition() + threshHold) {
//                    if (loadDynamicData != null) {
//                        loadDynamicData.onRequestData(userViewProductsList);
//                        isLoading = true;
//                    }
//                }
            }
        });

    }

    public void setLoadDynamicData(LoadDynamicData loadDynamicData) {
        this.loadDynamicData = loadDynamicData;
    }

    public void setLoading() {
        isLoading = false;
    }

    public class ClothingViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage0, productImage1, productImage2, productImage3;

        public ClothingViewHolder(View itemView) {
            super(itemView);
            productImage0 = itemView.findViewById(R.id.productImage0);
            productImage1 = itemView.findViewById(R.id.productImage1);
            productImage2 = itemView.findViewById(R.id.productImage2);
            productImage3 = itemView.findViewById(R.id.productImage3);
        }
    }

    public class ClothingInvertViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage0, productImage1, productImage2, productImage3;

        public ClothingInvertViewHolder(View itemView) {
            super(itemView);
            productImage0 = itemView.findViewById(R.id.productImage0);
            productImage1 = itemView.findViewById(R.id.productImage1);
            productImage2 = itemView.findViewById(R.id.productImage2);
            productImage3 = itemView.findViewById(R.id.productImage3);
        }
    }
}
