package com.rrajath.transittracker;

import com.google.android.gms.common.api.GoogleApiClient;
import com.rrajath.transittracker.di.component.MainActivityComponent;
import com.rrajath.transittracker.di.component.NearbyStopsListActivityComponent;
import com.rrajath.transittracker.di.module.MainActivityModule;
import com.rrajath.transittracker.di.module.NearbyStopsListActivityModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = AppModule.class
)
public interface AppComponent {
    MainActivityComponent plus(MainActivityModule mainActivityModule);

    NearbyStopsListActivityComponent plus(NearbyStopsListActivityModule nearbyStopsListActivityModule);

    GoogleApiClient googleApiClient();
}
