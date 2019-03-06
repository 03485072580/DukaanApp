package com.example.fasih.dukaanapp.home.fragments.sellerPageResources;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.adapter.MyExternalPublicStorageDirectoryAdapter;
import com.example.fasih.dukaanapp.home.interfaces.OnRecyclerImageSelectedListener;
import com.example.fasih.dukaanapp.utils.MessageEvent;
import com.example.fasih.dukaanapp.utils.UniversalImageLoader;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jsibbold.zoomage.ZoomageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fasih on 02/15/19.
 */

public class CategoryShopFragment extends Fragment implements OnRecyclerImageSelectedListener {


    private ImageView hamburgerDrawerIcon;
    private DrawerLayout drawerLayoutCategoryShop;
    private NavigationView navigationViewCategoryShop;
    private RecyclerView selectSharableImage;
    private MaterialSpinner sortablePicturesSpinner;
    private ZoomageView selectedSharableImage;
    private TextView share;
    private List<String> internalDirectories;
    private MyExternalPublicStorageDirectoryAdapter adapter;
    private BottomNavigationViewEx categoryShopBottomNavigation;
    private Uri capturedImageUri;
    private String completeImageUrlFromGallery;
    private RelativeLayout fragmentFrameHolder, shareFragmentFrameHolder;

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {/* Do something */
        capturedImageUri = event.getCapturedImageUri();
    }

    /**
     * Called Whenever an item on the RecyclerView (Grid) clicked
     *
     * @param position
     * @param view
     */
    @Override
    public void onClickGridImage(int position, View view) {

    }

    /**
     * Called Whenever an item on the RecyclerView (Grid) clicked with providing the complete Image URL
     *
     * @param position
     * @param view
     */
    @Override
    public void onClickGridImage(int position, View view, String Url) {
        ImageLoader.getInstance().displayImage("File://" + Url, selectedSharableImage);
        completeImageUrlFromGallery = "File://" + Url;
        capturedImageUri = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_shop, container, false);
        EventBus.getDefault().register(this);
        setupFragmentWidgets(view);
        setupSDCardDirectoryFetching();
        setupSpinner();
        setupUniversalImageLoader();
        setupCameraPhoto();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void setupCameraPhoto() {
        if (capturedImageUri != null) {
            ImageLoader.getInstance().displayImage(capturedImageUri.toString(), selectedSharableImage);
        }
    }

    private void setupUniversalImageLoader() {
        ImageLoader.getInstance().init(UniversalImageLoader.getConfiguration(getActivity()));
    }

    private void setupSDCardDirectoryFetching() {
        internalDirectories = new ArrayList<>();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
        if (file.exists()) {

            File[] filesList = file.listFiles();
            if (filesList != null)
                for (int i = 0; i < filesList.length; i++) {
                    if (filesList[i].isDirectory() && !filesList[i].isHidden()) {
                        internalDirectories.add(filesList[i].getAbsolutePath());
                    }
                }
        }
    }

    private void setupSpinner() {

        try {
            final List<String> urlEndPoints = new ArrayList<>();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < internalDirectories.size(); i++) {
                            String url = internalDirectories.get(i);
                            urlEndPoints.add(url.substring(url.lastIndexOf("/"), internalDirectories.get(i).length()));
                        }
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    } finally {
                        if (!urlEndPoints.isEmpty()) {
                            try {
                                sortablePicturesSpinner.setItems(urlEndPoints);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        setupRecyclerGrid(internalDirectories.get(sortablePicturesSpinner.getSelectedIndex()));
                                    }
                                });
                            } catch (NullPointerException exc) {
                                exc.printStackTrace();
                            }
                        }
                    }

                }
            }).start();


        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }

    }


    private void setupRecyclerGrid(String imageRootUrl) {
        try {
            selectSharableImage.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            adapter = new MyExternalPublicStorageDirectoryAdapter(getActivity(), imageRootUrl, this, selectedSharableImage);
            selectSharableImage.setAdapter(adapter);

        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }
    }

    private void setupFragmentWidgets(View view) {
        drawerLayoutCategoryShop = view.findViewById(R.id.drawerLayoutCategoryShop);
        navigationViewCategoryShop = view.findViewById(R.id.navigationViewCategoryShop);
        hamburgerDrawerIcon = view.findViewById(R.id.hamburgerDrawerIcon);
        selectSharableImage = view.findViewById(R.id.selectSharableImage);
        selectedSharableImage = view.findViewById(R.id.selectedSharableImage);
        sortablePicturesSpinner = view.findViewById(R.id.sortablePicturesSpinner);
        share = view.findViewById(R.id.shareImage);
        categoryShopBottomNavigation = view.findViewById(R.id.categoryShopBottomNavigation);

        hamburgerDrawerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayoutCategoryShop.isDrawerOpen(navigationViewCategoryShop)) {
                    drawerLayoutCategoryShop.closeDrawer(navigationViewCategoryShop);
                } else {
                    drawerLayoutCategoryShop.openDrawer(navigationViewCategoryShop, true);
                }
            }
        });
        sortablePicturesSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                //calling here assure that internalDirectories is not null
                if (!internalDirectories.isEmpty()) {

                    setupRecyclerGrid(internalDirectories.get(position));
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    fragmentFrameHolder.setVisibility(View.GONE);
                    shareFragmentFrameHolder.setVisibility(View.VISIBLE);
                    if (capturedImageUri != null) {
                        //it's comming from the camera Intent todo content uri
                        ShareFragment shareFragment = new ShareFragment();
                        shareFragment.setImageLoadingUrl(capturedImageUri.toString());
                        shareFragment.setXmlResources(fragmentFrameHolder, shareFragmentFrameHolder);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.add(R.id.fragmentContainer2
                                , shareFragment
                                , getActivity().getString(R.string.shareFragment));
                        transaction.addToBackStack(getString(R.string.shareFragment));
                        transaction.commitAllowingStateLoss();

                    } else {
                        //it's coming from the Gallery todo File uri
                        ShareFragment shareFragment = new ShareFragment();
                        shareFragment.setImageLoadingUrl(completeImageUrlFromGallery);
                        shareFragment.setXmlResources(fragmentFrameHolder, shareFragmentFrameHolder);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.add(R.id.fragmentContainer2
                                , shareFragment
                                , getActivity().getString(R.string.shareFragment));
                        transaction.addToBackStack(getString(R.string.shareFragment));
                        transaction.commitAllowingStateLoss();
                    }


                } catch (NullPointerException exc) {
                    exc.printStackTrace();
                }
            }
        });
        categoryShopBottomNavigation
                .setOnNavigationItemSelectedListener
                        (new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.camera) {
                    try {
                        CameraFragment cameraFragment = new CameraFragment();
                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragmentContainer, cameraFragment, getString(R.string.cameraFragment))
                                .addToBackStack(getString(R.string.cameraFragment))
                                .commitAllowingStateLoss();
                    } catch (NullPointerException exc) {
                        exc.printStackTrace();
                    }
                    return true;
                } else if (item.getItemId() == R.id.sellerHome) {
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("TAG1234", "onSaveInstanceState: ");
    }

    public void setXmlResources(RelativeLayout fragmentFrameHolder
            , RelativeLayout shareFragmentFrameHolder) {
        this.fragmentFrameHolder = fragmentFrameHolder;
        this.shareFragmentFrameHolder = shareFragmentFrameHolder;
    }
}
