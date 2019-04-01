package com.example.fasih.dukaanapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import com.example.fasih.dukaanapp.R;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * Created by Fasih on 02/21/19.
 */

public class UniversalImageLoader {

    public static ImageLoaderConfiguration getConfiguration(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(0)
                .showImageOnFail(R.drawable.ic_show_person)
                .showImageForEmptyUri(R.drawable.ic_show_person)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .imageScaleType(ImageScaleType.EXACTLY) // default
                .bitmapConfig(Bitmap.Config.RGB_565) // default
                .displayer(new SimpleBitmapDisplayer())
                .considerExifParams(false)
                .handler(new Handler())// default
                .build();

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.memoryCacheExtraOptions(480, 800); // default = device screen dimensions
        config.writeDebugLogs();// Remove for release app
        config.memoryCache(new LruMemoryCache(2 * 1024 * 1024));
        config.memoryCacheSize(2 * 1024 * 1024);
        config.memoryCacheSizePercentage(13); // default
        config.imageDownloader(new BaseImageDownloader(context)); // default
        config.imageDecoder(new BaseImageDecoder(true)); // default
        config.defaultDisplayImageOptions(options);
        // Initialize ImageLoader with configuration.
        return config.build();
    }

}
