package com.rrajath.transittracker.di.module;

import com.rrajath.transittracker.ui.ActivityScope;
import com.rrajath.transittracker.ui.activity.NearbyStopsListActivity;
import com.rrajath.transittracker.ui.presenter.NearbyStopsListActivityPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class NearbyStopsListActivityModule {
    NearbyStopsListActivity mStopsListActivity;

    public NearbyStopsListActivityModule(NearbyStopsListActivity stopsListActivity) {
        mStopsListActivity = stopsListActivity;
    }

    @Provides
    @ActivityScope
    public NearbyStopsListActivity providesNearbyStopsListActivity() {
        return mStopsListActivity;
    }

    @Provides
    @ActivityScope
    public NearbyStopsListActivityPresenter providesNearbyStopsListActivityPresenter() {
        return new NearbyStopsListActivityPresenter(mStopsListActivity);
    }

}
