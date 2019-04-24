package com.example.fasih.dukaanapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.customModels.RecyclerSelectedCategory;
import com.example.fasih.dukaanapp.home.interfaces.OnRecyclerImageSelectedListener;

import java.util.ArrayList;

public class SubCategoriesListAdapter extends RecyclerView.Adapter<SubCategoriesListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<RecyclerSelectedCategory> currentDataSet = new ArrayList<>();

    private OnRecyclerImageSelectedListener imageSelected;

    public SubCategoriesListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_sub_categories_single_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tvSubCategoryItem.setText(currentDataSet.get(position).getCategoryTvText());
        holder.subCategoryItemImage.setImageResource(currentDataSet.get(position).getCategoryImageResource());

    }

    @Override
    public int getItemCount() {
        return currentDataSet.size();
    }

    public void setData(ArrayList<RecyclerSelectedCategory> list) {

        this.currentDataSet = list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView subCategoryItemImage;
        private TextView tvSubCategoryItem;

        private MyViewHolder(final View itemView) {
            super(itemView);

            subCategoryItemImage = itemView.findViewById(R.id.subCategoryItemImage);
            tvSubCategoryItem = itemView.findViewById(R.id.tvSubCategoryItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageSelected.onClickGridImage(getAdapterPosition()
                            , itemView
                            , mContext.getString(R.string.subCategoriesListAdapter));
                }
            });
        }
    }


    public void setupOnItemClickListener(OnRecyclerImageSelectedListener onRecyclerImageSelectedListener) {

        this.imageSelected = onRecyclerImageSelectedListener;

    }

    public RecyclerSelectedCategory getRecyclerSelectedCategoryObject (int position) {

        return currentDataSet.get(position);
    }
}