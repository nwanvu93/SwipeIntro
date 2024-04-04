package com.nwanvu.example.objectanimator;

import android.app.Application;

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
}
