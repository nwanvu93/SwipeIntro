package com.nwanvu.example.objectanimator;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Administrator on 027 27/04.
 * Application
 */
public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public synchronized Tracker getTracker() {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        analytics.setDryRun(BuildConfig.DEBUG);
        return analytics.newTracker(R.xml.global_tracker);
    }
}
