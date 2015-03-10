package com.teachandroid.app;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by rakovskyi on 03.03.15.
 */
public class LoaderApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        File cacheDir = StorageUtils.getCacheDirectory(this.getApplicationContext());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.getApplicationContext()).writeDebugLogs().build();
        ImageLoader.getInstance().init(config);

    }
}
