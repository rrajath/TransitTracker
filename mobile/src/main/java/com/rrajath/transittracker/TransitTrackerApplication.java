package com.rrajath.transittracker;

import android.app.Application;
import android.content.Context;

import com.rrajath.transittracker.di.module.NetworkModule;

import timber.log.Timber;

public class TransitTrackerApplication extends Application {

    AppComponent mAppComponent;
    private static final String ONEBUSAWAY_API_ENDPOINT = "http://api.pugetsound.onebusaway.org";
    public static final int USER_PERMISSION_LOCATION = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule(ONEBUSAWAY_API_ENDPOINT))
                .build();
    }

    public AppComponent getComponent() {
        return mAppComponent;
    }

    public static TransitTrackerApplication get(Context context) {
        return (TransitTrackerApplication) context.getApplicationContext();
    }
}
