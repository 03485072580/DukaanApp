package com.example.fasih.dukaanapp.home.fragments.sellerPageResources;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.home.interfaces.OnBackButtonPressedListener;
import com.example.fasih.dukaanapp.utils.UniversalImageLoader;
import com.jsibbold.zoomage.ZoomageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Fasih on 03/04/19.
 */

public class ZoomImageViewFragment extends DialogFragment implements OnBackButtonPressedListener {

    private ZoomageView zoomImageView;
    private String Url;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_zoom_image_viewer, container, false);
        zoomImageView = view.findViewById(R.id.zoomImageView);
        try {
            setupUniversalImageLoader(UniversalImageLoader.getConfiguration(getActivity().getApplicationContext()));
        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }
        return view;
    }

    public void setupUniversalImageLoader(ImageLoaderConfiguration upUniversalImageLoader) {
        ImageLoader.getInstance().init(upUniversalImageLoader);
        ImageLoader.getInstance().displayImage(Url, zoomImageView);
    }

    public void setZoomImageViewUri(String Url) {
        this.Url = Url;
    }

    @Override
    public void onBackPressed() {
        try {
            Log.d("TAG1234", "onBackPressed: ZoomImageViewFragment");
            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag(getString(R.string.zoomImageViewFragment))).commitNowAllowingStateLoss();

        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }
    }
}
