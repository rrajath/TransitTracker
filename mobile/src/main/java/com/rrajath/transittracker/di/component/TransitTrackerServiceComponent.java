package com.rrajath.transittracker.di.component;

import com.rrajath.transittracker.di.module.TransitTrackerServiceModule;
import com.rrajath.transittracker.di.scope.UserScope;
import com.rrajath.transittracker.presenter.TransitTrackerServicePresenter;
import com.rrajath.transittracker.service.TransitTrackerService;

import dagger.Subcomponent;

@UserScope
@Subcomponent (
        modules = {
                TransitTrackerServiceModule.class
        }
)
public interface TransitTrackerServiceComponent {
    TransitTrackerService inject(TransitTrackerService service);

    TransitTrackerServicePresenter presenter();
}
