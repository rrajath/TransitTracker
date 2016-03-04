package com.rrajath.transittracker;

import com.google.android.gms.common.api.GoogleApiClient;
import com.rrajath.transittracker.di.module.NetworkModule;
import com.rrajath.transittracker.di.module.OneBusAwayApiModule;
import com.rrajath.transittracker.logging.AppLogger;
import com.rrajath.transittracker.network.StopsManager;
import com.rrajath.transittracker.util.LocationUtils;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component (
        modules = {
                AppModule.class,
                NetworkModule.class,
                OneBusAwayApiModule.class
        }
)
public interface AppComponent {
//    TransitTrackerServiceComponent plus(TransitTrackerServiceModule transitTrackerServiceModule);
//    OneBusAwayApiComponent plus(OneBusAwayApiModule oneBusAwayApiModule);
    StopsManager stopsManager();
    LocationUtils locationUtils();
    GoogleApiClient googleApiClient();
    AppLogger appLogger();
}
