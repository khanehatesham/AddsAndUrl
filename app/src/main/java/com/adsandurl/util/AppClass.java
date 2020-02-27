
package com.adsandurl.util;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;


public class AppClass extends Application {

    private static AppClass instance;


    public static AppClass getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();

    }

}