package com.xy.hashmaker.activities.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by Xavier Yin on 10/20/16.
 */

public class HashMakerApplication extends Application {
    private static Context context;
    private static Resources resources;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initData();
    }

    public static Context getAppContext() {
        return context;
    }

    public static Resources getAppResources() {
        return resources;
    }

    private void initData() {
        this.context = this;
        this.resources = getResources();
    }
}
