package com.rrajath.transittracker;

import com.rrajath.transittracker.di.component.WearableListenerServiceComponent;
import com.rrajath.transittracker.di.module.WearableListenerServiceModule;
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
    WearableListenerServiceComponent plus(WearableListenerServiceModule wearableListenerServiceModule);
//    TransitApiComponent plus(TransitApiModule transitApiModule);
}
