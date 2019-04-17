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
import android.widget.TextView;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.categories.interfaces.LoadDynamicData;
import com.example.fasih.dukaanapp.home.interfaces.OnRecyclerImageSelectedListener;
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
    private int threshHold = 2;
    private int pastVisibleItems = 0, visibleItemCount = 0, totalItemCount = 0, previousTotal = 0;
    private Context mContext;
    private ArrayList<Products> userViewProductsList;
    private LoadDynamicData loadDynamicData;
    private Boolean isLoading;
    private int currentPositionParsed = -1;


    private OnRecyclerImageSelectedListener imageSelected;

    public ClothingProductsAdapter(Context context
            , ArrayList<Products> userViewProductsList
            , RecyclerView recyclerView) {

        this.userViewProductsList = userViewProductsList;
        this.mContext = context;
        setupUniversalImageLoader(UniversalImageLoader.getConfiguration(context));
        setupScrollListener(recyclerView);

    }

    @Override
    public int getItemViewType(int position) {
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
//                updateRecyclerViews(currentPositionParsed + 1, viewHolder);
                updateRecyclerViews(position * 4, viewHolder);

            }

            if (holder instanceof ClothingInvertViewHolder) {
                ClothingInvertViewHolder viewHolder = (ClothingInvertViewHolder) holder;
//                updateRecyclerViews(currentPositionParsed + 1, viewHolder);
                updateRecyclerViews(position * 4, viewHolder);
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
            viewHolder.tvTitle0.setText(userViewProductsList.get(currentPositionParsed).getProduct_name());
            viewHolder.tvPrice0.setText(userViewProductsList.get(currentPositionParsed).getProduct_price());
        }
        if (userViewProductsList.get(position + 1) != null && (position + 1) % 4 == 1) {

            ImageLoader.getInstance().displayImage(userViewProductsList
                            .get(position + 1)
                            .getProduct_image_url()
                    , viewHolder.productImage1);
            currentPositionParsed = position + 1;
            viewHolder.tvTitle1.setText(userViewProductsList.get(currentPositionParsed).getProduct_name());
            viewHolder.tvPrice1.setText(userViewProductsList.get(currentPositionParsed).getProduct_price());

        }
        if (userViewProductsList.get(position + 2) != null && (position + 2) % 4 == 2) {

            ImageLoader.getInstance().displayImage(userViewProductsList
                            .get(position + 2)
                            .getProduct_image_url()
                    , viewHolder.productImage2);
            currentPositionParsed = position + 2;
            viewHolder.tvTitle2.setText(userViewProductsList.get(currentPositionParsed).getProduct_name());
            viewHolder.tvPrice2.setText(userViewProductsList.get(currentPositionParsed).getProduct_price());
        }
        if (userViewProductsList.get(position + 3) != null && (position + 3) % 4 == 3) {

            ImageLoader.getInstance().displayImage(userViewProductsList
                            .get(position + 3)
                            .getProduct_image_url()
                    , viewHolder.productImage3);
            currentPositionParsed = position + 3;
            viewHolder.tvTitle3.setText(userViewProductsList.get(currentPositionParsed).getProduct_name());
            viewHolder.tvPrice3.setText(userViewProductsList.get(currentPositionParsed).getProduct_price());
        }
    }

    private void updateRecyclerViews(int position, ClothingInvertViewHolder viewHolder) {
        if (userViewProductsList.get(position) != null && position % 4 == 0) {

            ImageLoader.getInstance().displayImage(userViewProductsList
                            .get(position)
                            .getProduct_image_url()
                    , viewHolder.productImage0);
            currentPositionParsed = position;
            viewHolder.tvTitle0.setText(userViewProductsList.get(currentPositionParsed).getProduct_name());
            viewHolder.tvPrice0.setText(userViewProductsList.get(currentPositionParsed).getProduct_price());
        }
        if (userViewProductsList.get(position + 1) != null && (position + 1) % 4 == 1) {

            ImageLoader.getInstance().displayImage(userViewProductsList
                            .get(position + 1)
                            .getProduct_image_url()
                    , viewHolder.productImage1);
            currentPositionParsed = position + 1;
            viewHolder.tvTitle1.setText(userViewProductsList.get(currentPositionParsed).getProduct_name());
            viewHolder.tvPrice1.setText(userViewProductsList.get(currentPositionParsed).getProduct_price());
        }
        if (userViewProductsList.get(position + 2) != null && (position + 2) % 4 == 2) {

            ImageLoader.getInstance().displayImage(userViewProductsList
                            .get(position + 2)
                            .getProduct_image_url()
                    , viewHolder.productImage2);
            currentPositionParsed = position + 2;
            viewHolder.tvTitle2.setText(userViewProductsList.get(currentPositionParsed).getProduct_name());
            viewHolder.tvPrice2.setText(userViewProductsList.get(currentPositionParsed).getProduct_price());
        }
        if (userViewProductsList.get(position + 3) != null && (position + 3) % 4 == 3) {

            ImageLoader.getInstance().displayImage(userViewProductsList
                            .get(position + 3)
                            .getProduct_image_url()
                    , viewHolder.productImage3);
            currentPositionParsed = position + 3;
            viewHolder.tvTitle3.setText(userViewProductsList.get(currentPositionParsed).getProduct_name());
            viewHolder.tvPrice3.setText(userViewProductsList.get(currentPositionParsed).getProduct_price());
        }
    }

    @Override
    public int getItemCount() {
        return (int) Math.ceil(userViewProductsList.size() / 4.0);
    }

    private void setupUniversalImageLoader(ImageLoaderConfiguration config) {
        ImageLoader.getInstance().init(config);
    }

    private void setupScrollListener(RecyclerView recyclerView) {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                totalItemCount = recyclerView.getLayoutManager().getItemCount();
                pastVisibleItems = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager())
                        .findFirstVisibleItemPositions(null)[0];
                threshHold = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPositions(null)[0];
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

    public void setLoadDynamicData(LoadDynamicData loadDynamicData) {
        this.loadDynamicData = loadDynamicData;
    }

    public void setLoading() {
        isLoading = false;
    }

    public void setLoading(boolean b) {
        isLoading = b;
    }

    public void setFilteredList(ArrayList<Products> filteredList) {
        userViewProductsList.clear();
        userViewProductsList.addAll(filteredList);
        notifyDataSetChanged();
    }

    public void setupOnItemClickListener(OnRecyclerImageSelectedListener onRecyclerImageSelectedListener) {

        this.imageSelected = onRecyclerImageSelectedListener;

    }

    public class ClothingViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage0, productImage1, productImage2, productImage3;
        private TextView tvTitle0, tvTitle1, tvTitle2, tvTitle3, tvPrice0, tvPrice1, tvPrice2, tvPrice3;

        public ClothingViewHolder(final View itemView) {
            super(itemView);
            productImage0 = itemView.findViewById(R.id.productImage0);
            productImage1 = itemView.findViewById(R.id.productImage1);
            productImage2 = itemView.findViewById(R.id.productImage2);
            productImage3 = itemView.findViewById(R.id.productImage3);
            tvTitle0 = itemView.findViewById(R.id.tvTitle0);
            tvTitle1 = itemView.findViewById(R.id.tvTitle1);
            tvTitle2 = itemView.findViewById(R.id.tvTitle2);
            tvTitle3 = itemView.findViewById(R.id.tvTitle3);
            tvPrice0 = itemView.findViewById(R.id.tvPrice0);
            tvPrice1 = itemView.findViewById(R.id.tvPrice1);
            tvPrice2 = itemView.findViewById(R.id.tvPrice2);
            tvPrice3 = itemView.findViewById(R.id.tvPrice3);

            try {

                setupImageClickListeners(itemView);
            } catch (IndexOutOfBoundsException exc) {
                exc.printStackTrace();
            }
        }

        private void setupImageClickListeners(final View itemView) {
            productImage0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("TAG1234", "onClick: " + getAdapterPosition());

                    try {

                        imageSelected.onClickGridImage(getAdapterPosition()
                                , itemView
                                , userViewProductsList.get(getAdapterPosition() * 4));

                    } catch (IndexOutOfBoundsException exc) {
                        exc.printStackTrace();
                    }
                }
            });

            productImage1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {

                        imageSelected.onClickGridImage(getAdapterPosition()
                                , itemView
                                , userViewProductsList.get(getAdapterPosition() * 4 + 1));

                    } catch (IndexOutOfBoundsException exc) {
                        exc.printStackTrace();
                    }
                }
            });

            productImage2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {

                        imageSelected.onClickGridImage(getAdapterPosition()
                                , itemView
                                , userViewProductsList.get(getAdapterPosition() * 4 + 2));
                    } catch (IndexOutOfBoundsException exc) {
                        exc.printStackTrace();
                    }
                }
            });

            productImage3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {

                        imageSelected.onClickGridImage(getAdapterPosition()
                                , itemView
                                , userViewProductsList.get(getAdapterPosition() * 4 + 3));
                    } catch (IndexOutOfBoundsException exc) {
                        exc.printStackTrace();
                    }
                }
            });
        }
    }

    public class ClothingInvertViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage0, productImage1, productImage2, productImage3;
        private TextView tvTitle0, tvTitle1, tvTitle2, tvTitle3, tvPrice0, tvPrice1, tvPrice2, tvPrice3;

        public ClothingInvertViewHolder(View itemView) {
            super(itemView);
            productImage0 = itemView.findViewById(R.id.productImage0);
            productImage1 = itemView.findViewById(R.id.productImage1);
            productImage2 = itemView.findViewById(R.id.productImage2);
            productImage3 = itemView.findViewById(R.id.productImage3);
            tvTitle0 = itemView.findViewById(R.id.tvTitle0);
            tvTitle1 = itemView.findViewById(R.id.tvTitle1);
            tvTitle2 = itemView.findViewById(R.id.tvTitle2);
            tvTitle3 = itemView.findViewById(R.id.tvTitle3);
            tvPrice0 = itemView.findViewById(R.id.tvPrice0);
            tvPrice1 = itemView.findViewById(R.id.tvPrice1);
            tvPrice2 = itemView.findViewById(R.id.tvPrice2);
            tvPrice3 = itemView.findViewById(R.id.tvPrice3);

            setupImageClickListeners(itemView);

        }

        private void setupImageClickListeners(final View itemView) {
            productImage0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("TAG1234", "onClick: " + getAdapterPosition());

                    try {

                        imageSelected.onClickGridImage(getAdapterPosition()
                                , itemView
                                , userViewProductsList.get(getAdapterPosition() * 4));

                    } catch (IndexOutOfBoundsException exc) {
                        exc.printStackTrace();
                    }
                }
            });

            productImage1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {

                        imageSelected.onClickGridImage(getAdapterPosition()
                                , itemView
                                , userViewProductsList.get(getAdapterPosition() * 4 + 1));

                    } catch (IndexOutOfBoundsException exc) {
                        exc.printStackTrace();
                    }
                }
            });

            productImage2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {

                        imageSelected.onClickGridImage(getAdapterPosition()
                                , itemView
                                , userViewProductsList.get(getAdapterPosition() * 4 + 2));
                    } catch (IndexOutOfBoundsException exc) {
                        exc.printStackTrace();
                    }
                }
            });

            productImage3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {

                        imageSelected.onClickGridImage(getAdapterPosition()
                                , itemView
                                , userViewProductsList.get(getAdapterPosition() * 4 + 3));
                    } catch (IndexOutOfBoundsException exc) {
                        exc.printStackTrace();
                    }
                }
            });
        }
    }
}
