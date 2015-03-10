package com.teachandroid.app;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by rakovskyi on 03.03.15.
 */
public class LoaderApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate(){
        super.onCreate();
        File cacheDir = StorageUtils.getCacheDirectory(this.getApplicationContext());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.getApplicationContext()).writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
