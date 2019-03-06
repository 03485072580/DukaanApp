package com.example.fasih.dukaanapp.home.fragments.sellerPageResources;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fasih.dukaanapp.R;

/**
 * Created by Fasih on 03/06/19.
 */

public class ProgressDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress_dialog, container, false);
        return view;
    }
}
