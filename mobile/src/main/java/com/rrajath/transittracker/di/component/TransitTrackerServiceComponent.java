package com.rrajath.transittracker.di.component;

import com.rrajath.transittracker.AppComponent;
import com.rrajath.transittracker.di.module.TransitTrackerServiceModule;
import com.rrajath.transittracker.di.scope.UserScope;
import com.rrajath.transittracker.service.TransitTrackerService;

import dagger.Component;

@UserScope
@Component(
        dependencies = AppComponent.class,
        modules = {
                TransitTrackerServiceModule.class
        }
)
public interface TransitTrackerServiceComponent {
    void inject(TransitTrackerService service);
}
