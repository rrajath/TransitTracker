package com.rrajath.transittracker;

import android.app.Application;
import android.content.Context;

import com.rrajath.transittracker.logging.AppLogger;

import javax.inject.Inject;

public class WearApplication extends Application {
    AppComponent mAppComponent;

    @Inject
    AppLogger mAppLogger;

    @Override
    public void onCreate() {
        super.onCreate();

/*
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
*/

        mAppLogger.d("Testing Testing!");

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
