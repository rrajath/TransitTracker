package com.rrajath.transittracker;

import com.rrajath.transittracker.di.component.TransitTrackerServiceComponent;
import com.rrajath.transittracker.di.module.TransitTrackerServiceModule;
import com.rrajath.transittracker.di.module.NetworkModule;
import com.rrajath.transittracker.di.module.OneBusAwayApiModule;

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
    TransitTrackerServiceComponent plus(TransitTrackerServiceModule transitTrackerServiceModule);
//    OneBusAwayApiComponent plus(OneBusAwayApiModule oneBusAwayApiModule);
}
