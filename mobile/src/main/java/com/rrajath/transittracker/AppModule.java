package com.rrajath.transittracker;

import android.app.Application;
import android.content.Context;
import android.location.LocationManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;
import com.rrajath.transittracker.logging.AppLogger;
import com.rrajath.transittracker.util.LocationUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return application;
    }

    @Provides
    @Singleton
    LocationUtils providesLocationUtils(GoogleApiClient googleApiClient,
                                        LocationManager locationManager,
                                        AppLogger appLogger) {
        return new LocationUtils(googleApiClient, locationManager, appLogger);
    }

    @Provides
    @Singleton
    GoogleApiClient providesGoogleApiClient(Application application) {
        return new GoogleApiClient.Builder(application)
                .addApi(Wearable.API)
                .addApi(LocationServices.API)
                .build();
    }

    @Provides
    @Singleton
    LocationManager providesLocationManager() {
        return (LocationManager) application.getSystemService(Context.LOCATION_SERVICE);
    }

    @Provides
    @Singleton
    Context providesApplicationContext() {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    AppLogger providesAppLogger() {
        return new AppLogger(application);
    }
}
