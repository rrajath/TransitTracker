package com.rrajath.transittracker.di.module;

import com.rrajath.transittracker.di.scope.UserScope;
import com.rrajath.transittracker.logging.AppLogger;
import com.rrajath.transittracker.network.StopsManager;
import com.rrajath.transittracker.presenter.TransitTrackerServicePresenter;
import com.rrajath.transittracker.service.TransitTrackerService;

import dagger.Module;
import dagger.Provides;

@Module
public class TransitTrackerServiceModule {
    TransitTrackerService mService;

    public TransitTrackerServiceModule(TransitTrackerService service) {
        mService = service;
    }

    @UserScope
    @Provides
    TransitTrackerService provideTransitTrackerService() {
        return mService;
    }

    @UserScope
    @Provides
    TransitTrackerServicePresenter provideTransitTrackerServicePresenter(StopsManager api,
                                                                         AppLogger appLogger) {
        return new TransitTrackerServicePresenter(mService, api, appLogger);
    }
}
