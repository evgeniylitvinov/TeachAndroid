package com.teachandroid.app.util;

import android.util.Log;

public class Logger {

    private static final String TAG = "TeachAndroid";

    public static void log(String tag, String format, Object... args){
        Log.d(String.format("%s - %s", TAG, tag), String.format(format, args));
    }
}
