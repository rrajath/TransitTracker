package com.rrajath.transittracker;

import com.rrajath.transittracker.di.component.TransitTrackerServiceComponent;
import com.rrajath.transittracker.di.module.TransitTrackerServiceModule;
import com.rrajath.transittracker.di.module.NetworkModule;
import com.rrajath.transittracker.di.module.TransitApiModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component (
        modules = {
                AppModule.class,
                NetworkModule.class,
                TransitApiModule.class
        }
)
public interface AppComponent {
    TransitTrackerServiceComponent plus(TransitTrackerServiceModule transitTrackerServiceModule);
//    TransitApiComponent plus(TransitApiModule transitApiModule);
}
