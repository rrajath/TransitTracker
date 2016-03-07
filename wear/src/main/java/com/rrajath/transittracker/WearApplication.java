package com.rrajath.transittracker;

import android.app.Application;
import android.content.Context;

public class WearApplication extends Application {
    AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

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
