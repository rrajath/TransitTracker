package com.rrajath.transittracker.di.module;

import com.rrajath.transittracker.di.scope.UserScope;
import com.rrajath.transittracker.network.StopsManager;
import com.rrajath.transittracker.presenter.WearableListenerServicePresenter;
import com.rrajath.transittracker.service.WearableListenerService;

import dagger.Module;
import dagger.Provides;

@Module
public class WearableListenerServiceModule {
    WearableListenerService mService;

    public WearableListenerServiceModule(WearableListenerService service) {
        mService = service;
    }

    @UserScope
    @Provides
    WearableListenerService provideWearableListenerService() {
        return mService;
    }

    @UserScope
    @Provides
    WearableListenerServicePresenter provideWearableListenerServicePresenter(StopsManager api) {
        return new WearableListenerServicePresenter(mService, api);
    }
}
