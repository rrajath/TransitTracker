package com.rrajath.transittracker;

import android.app.Application;
import android.content.Context;

import com.rrajath.transittracker.di.component.DaggerTransitTrackerServiceComponent;
import com.rrajath.transittracker.di.component.TransitTrackerServiceComponent;
import com.rrajath.transittracker.di.module.NetworkModule;
import com.rrajath.transittracker.di.module.TransitTrackerServiceModule;
import com.rrajath.transittracker.service.TransitTrackerService;

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

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public static TransitTrackerApplication get(Context context) {
        return (TransitTrackerApplication) context.getApplicationContext();
    }

    public TransitTrackerServiceComponent createTransitTrackerServiceComponent(TransitTrackerService service) {
        return DaggerTransitTrackerServiceComponent.builder()
                .appComponent(mAppComponent)
                .transitTrackerServiceModule(new TransitTrackerServiceModule(service))
                .build();
    }
}
