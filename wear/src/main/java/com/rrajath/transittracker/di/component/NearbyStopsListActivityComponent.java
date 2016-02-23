package com.rrajath.transittracker.di.component;

import com.rrajath.transittracker.di.module.NearbyStopsListActivityModule;
import com.rrajath.transittracker.ui.ActivityScope;
import com.rrajath.transittracker.ui.activity.NearbyStopsListActivity;
import com.rrajath.transittracker.ui.presenter.NearbyStopsListActivityPresenter;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent (
        modules = NearbyStopsListActivityModule.class
)
public interface NearbyStopsListActivityComponent {
    void inject(NearbyStopsListActivity activity);

    NearbyStopsListActivityPresenter presenter();

}
