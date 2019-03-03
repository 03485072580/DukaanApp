package com.example.fasih.dukaanapp.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.home.fragments.sellerPageResources.CategoryShopFragment;
import com.example.fasih.dukaanapp.home.interfaces.OnRecyclerImageSelectedListener;
import com.example.fasih.dukaanapp.utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Fasih on 02/17/19.
 */

public class MyExternalPublicStorageDirectoryAdapter extends RecyclerView.Adapter<MyExternalPublicStorageDirectoryAdapter.MyCustomViewHolder> {

    private Context mContext;
    private String imageRootUrl;
    private ArrayList<String> imageRootUrlImages;
    private CategoryShopFragment currentContext;
    private OnRecyclerImageSelectedListener onImageSelectedListener;
    private ImageView selectedSharableImage;

    public MyExternalPublicStorageDirectoryAdapter(Context context
            , String imageRootUrl
            , CategoryShopFragment currentContext
            , ImageView selectedSharableImage) {
        mContext = context;
        this.currentContext = currentContext;
        this.imageRootUrl = imageRootUrl;
        this.selectedSharableImage = selectedSharableImage;
        imageRootUrlImages = new ArrayList<>();
        ImageLoader.getInstance().init(UniversalImageLoader.getConfiguration(mContext.getApplicationContext()));
        setupImageToLoad(imageRootUrl);

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
        if (imageRootUrlImages.get(position) != null)
            ImageLoader.getInstance().displayImage("File://" + imageRootUrlImages.get(position), holder.selectedProduct);
    }

    @Override
    public int getItemCount() {
        return imageRootUrlImages.size();
    }

    private void setupImageToLoad(String imageRootUrl) {
        final File baseImageUrl = new File(imageRootUrl);
        if (baseImageUrl.exists() && baseImageUrl.canRead()) {

            //imageRootUrlImages.addAll(imageRootUrlPNGImages);
            MyCustomAsyncTask task = new MyCustomAsyncTask();
            task.execute(baseImageUrl);

        }
    }

    private void exploreDirectory(File baseImageUrl) {

        File[] listOfFiles = baseImageUrl.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isDirectory()) {
                    exploreDirectory(file);
                }

                if (file.getAbsolutePath().toLowerCase().endsWith(".png")) {
                    imageRootUrlImages.add(file.getAbsolutePath());
                }
                if (file.getAbsolutePath().toLowerCase().endsWith(".jpg")) {
                    imageRootUrlImages.add(file.getAbsolutePath());
                }
                if (file.getAbsolutePath().toLowerCase().endsWith(".jpeg")) {
                    imageRootUrlImages.add(file.getAbsolutePath());
                }
            }
        }
    }

    public class MyCustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView selectedProduct;

        public MyCustomViewHolder(View itemView) {
            super(itemView);
            selectedProduct = itemView.findViewById(R.id.selectedProduct);
            onImageSelectedListener = currentContext;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onImageSelectedListener.onClickGridImage(getAdapterPosition(), view, imageRootUrlImages.get(getAdapterPosition()));
                }
            });
        }
    }

    public class MyCustomAsyncTask extends AsyncTask<File, Void, Void> {


        @Override
        protected Void doInBackground(File... files) {
            exploreDirectory(files[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!imageRootUrlImages.isEmpty())
                ImageLoader.getInstance().displayImage("File://" + imageRootUrlImages.get(0), selectedSharableImage);
//            else{
//                if(Build.VERSION.SDK_INT>=21)
//                selectedSharableImage.setImageDrawable(mContext.getDrawable(R.drawable.test_cart));
//                else{
//                    selectedSharableImage.setImageResource(R.drawable.test_cart);
//                }
//            }

            notifyDataSetChanged();
        }
    }
}

