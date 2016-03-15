package com.rrajath.transittracker;

import android.app.Application;
import android.content.Context;

import timber.log.Timber;

public class WearApplication extends Application {
    AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public static WearApplication get(Context context) {
        return (WearApplication) context.getApplicationContext();
    }
}
