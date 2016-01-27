package com.rrajath.transittracker.di.module;

import com.rrajath.transittracker.network.StopsManager;
import com.rrajath.transittracker.network.interfaces.TransitApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class TransitApiModule {

    @Singleton
    @Provides
    StopsManager provideStopsManager(TransitApiService transitApiService) {
        return new StopsManager(transitApiService);
    }

    @Singleton
    @Provides
    TransitApiService provideTransitApiService(Retrofit retrofit) {
        return retrofit.create(TransitApiService.class);
    }
}
