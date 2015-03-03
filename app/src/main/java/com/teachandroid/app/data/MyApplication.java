package com.teachandroid.app.data;

import android.app.Application;
import android.content.Context;

/**
 * Created by George on 03.03.2015.
 */
public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
