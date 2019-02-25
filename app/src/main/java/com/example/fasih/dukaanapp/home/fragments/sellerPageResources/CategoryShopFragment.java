package com.example.fasih.dukaanapp.home.fragments.sellerPageResources;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.adapter.MyExternalPublicStorageDirectoryAdapter;
import com.example.fasih.dukaanapp.home.interfaces.OnRecyclerImageSelectedListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

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
    private ImageView selectedSharableImage;
    private TextView share;
    private List<String> internalDirectories;
    private MyExternalPublicStorageDirectoryAdapter adapter;

    /**
     * Called Whenever an item on the RecyclerView (Grid) clicked
     *
     * @param position
     * @param view
     */
    @Override
    public void onClickGridImage(int position, View view) {


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_shop, container, false);
        setupFragmentWidgets(view);
        setupSDCardDirectoryFetching();
        setupSpinner();
        return view;
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
                            sortablePicturesSpinner.setItems(urlEndPoints);
                            try {

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
        selectSharableImage.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        adapter = new MyExternalPublicStorageDirectoryAdapter(getActivity(), imageRootUrl, this);
        selectSharableImage.setAdapter(adapter);
    }

    private void setupFragmentWidgets(View view) {
        drawerLayoutCategoryShop = view.findViewById(R.id.drawerLayoutCategoryShop);
        navigationViewCategoryShop = view.findViewById(R.id.navigationViewCategoryShop);
        hamburgerDrawerIcon = view.findViewById(R.id.hamburgerDrawerIcon);
        selectSharableImage = view.findViewById(R.id.selectSharableImage);
        selectedSharableImage = view.findViewById(R.id.selectedSharableImage);
        sortablePicturesSpinner = view.findViewById(R.id.sortablePicturesSpinner);
        share = view.findViewById(R.id.shareImage);


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
    }


}
