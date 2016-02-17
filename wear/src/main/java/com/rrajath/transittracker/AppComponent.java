package com.rrajath.transittracker;

import com.google.android.gms.common.api.GoogleApiClient;
import com.rrajath.transittracker.di.component.MainActivityComponent;
import com.rrajath.transittracker.di.module.MainActivityModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = AppModule.class
)
public interface AppComponent {
    MainActivityComponent plus(MainActivityModule mainActivityModule);

    GoogleApiClient googleApiClient();
}
