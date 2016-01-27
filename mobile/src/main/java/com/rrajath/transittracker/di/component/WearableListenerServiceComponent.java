package com.rrajath.transittracker.di.component;

import com.rrajath.transittracker.di.module.WearableListenerServiceModule;
import com.rrajath.transittracker.di.scope.UserScope;
import com.rrajath.transittracker.presenter.WearableListenerServicePresenter;
import com.rrajath.transittracker.service.WearableListenerService;

import dagger.Subcomponent;

@UserScope
@Subcomponent (
        modules = {
                WearableListenerServiceModule.class
        }
)
public interface WearableListenerServiceComponent {
    WearableListenerService inject(WearableListenerService service);

    WearableListenerServicePresenter presenter();
}
