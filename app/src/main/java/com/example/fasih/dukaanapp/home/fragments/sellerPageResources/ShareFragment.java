package com.example.fasih.dukaanapp.home.fragments.sellerPageResources;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fasih.dukaanapp.R;

/**
 * Created by Fasih on 02/25/19.
 */

public class ShareFragment extends Fragment {
    private RelativeLayout fragmentFrameHolder, shareFragmentFrameHolder;

    private ImageView backArrow, testImage;
    private TextView shareImage;
    private String capturedImage;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        setupFragmentWidgets(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            Bundle contentUriBundle = getArguments();
            capturedImage = contentUriBundle.getString(MediaStore.EXTRA_OUTPUT);
            Log.d("TAG1234", "onActivityCreated: " + capturedImage);
        }
    }

    private void setupFragmentWidgets(View view) {
        shareImage = view.findViewById(R.id.shareImage);
        backArrow = view.findViewById(R.id.backArrow);
        testImage = view.findViewById(R.id.testImage);
//        testImage.setImageURI(Uri.parse(capturedImage));

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    fragmentFrameHolder.setVisibility(View.VISIBLE);
                    shareFragmentFrameHolder.setVisibility(View.GONE);
                    getFragmentManager().popBackStack();

                } catch (NullPointerException exc) {
                    exc.printStackTrace();
                }
            }
        });

    }

    public void setXmlResources(RelativeLayout fragmentFrameHolder, RelativeLayout shareFragmentFrameHolder) {
        this.fragmentFrameHolder = fragmentFrameHolder;
        this.shareFragmentFrameHolder = shareFragmentFrameHolder;
    }
}
