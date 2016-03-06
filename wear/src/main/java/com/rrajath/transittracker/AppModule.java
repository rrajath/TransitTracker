package com.rrajath.transittracker;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;
import com.rrajath.transittracker.logging.AppLogger;

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
    GoogleApiClient providesGoogleApiClient(Application application) {
        return new GoogleApiClient.Builder(application)
                .addApi(Wearable.API)
                .build();
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
