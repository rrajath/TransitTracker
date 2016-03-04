package com.rrajath.transittracker.di.module;

import com.rrajath.transittracker.logging.AppLogger;
import com.rrajath.transittracker.network.StopsManager;
import com.rrajath.transittracker.network.interfaces.OneBusAwayApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class OneBusAwayApiModule {

    @Singleton
    @Provides
    StopsManager provideStopsManager(OneBusAwayApiService oneBusAwayApiService, AppLogger appLogger) {
        return new StopsManager(oneBusAwayApiService, appLogger);
    }

    @Singleton
    @Provides
    OneBusAwayApiService provideOneBusAwayApiService(Retrofit retrofit) {
        return retrofit.create(OneBusAwayApiService.class);
    }
}
