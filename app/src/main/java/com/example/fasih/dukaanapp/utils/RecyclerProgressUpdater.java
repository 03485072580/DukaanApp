package com.example.fasih.dukaanapp.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.fasih.dukaanapp.R;

/**
 * Created by Fasih on 04/09/19.
 */

public class RecyclerProgressUpdater extends RecyclerView.ViewHolder {

    ProgressBar myRecyclerProgress;

    public RecyclerProgressUpdater(View itemView) {
        super(itemView);
        myRecyclerProgress = itemView.findViewById(R.id.recyclerProgress);
    }
}
