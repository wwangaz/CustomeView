package com.example.wangweimin.customerview;

import android.app.Application;
import android.content.Context;

/**
 * Created by wangweimin on 16/10/14.
 */

public class App extends Application {
    public static Context mContext;

    public App() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
    }
}
