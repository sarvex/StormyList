package com.sarvex.stormy;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

public class StormyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
