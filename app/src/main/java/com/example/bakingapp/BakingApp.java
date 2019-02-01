package com.example.bakingapp;

import android.app.Application;

public class BakingApp extends Application {
    private static BakingApp mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
    public static BakingApp getContext(){return mContext;}
}

