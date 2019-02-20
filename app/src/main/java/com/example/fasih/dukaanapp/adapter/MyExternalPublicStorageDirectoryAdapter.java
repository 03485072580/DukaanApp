package com.example.fasih.dukaanapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fasih.dukaanapp.R;

import java.util.List;

/**
 * Created by Fasih on 02/17/19.
 */

public class MyExternalPublicStorageDirectoryAdapter extends RecyclerView.Adapter<MyExternalPublicStorageDirectoryAdapter.MyCustomViewHolder> {

    private Context mContext;
    private List<String> internalDirectories;

    public MyExternalPublicStorageDirectoryAdapter(Context context, List<String> internalDirectories) {
        mContext = context;
        this.internalDirectories = internalDirectories;
    }

    @NonNull
    @Override
    public MyCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;

        try {
            view = inflater.inflate(R.layout.layout_single_catergory_shop_grid_row, parent, false);
        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }
        return new MyCustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCustomViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyCustomViewHolder extends RecyclerView.ViewHolder {

        public MyCustomViewHolder(View itemView) {
            super(itemView);
        }
    }
}
