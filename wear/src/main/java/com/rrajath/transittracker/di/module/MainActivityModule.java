package com.rrajath.transittracker.di.module;

import com.rrajath.transittracker.ui.ActivityScope;
import com.rrajath.transittracker.ui.activity.MainActivity;
import com.rrajath.transittracker.ui.presenter.MainActivityPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {
    MainActivity mMainActivity;

    public MainActivityModule(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    @Provides
    @ActivityScope
    public MainActivity providesMainActivity() {
        return mMainActivity;
    }

    @Provides
    @ActivityScope
    public MainActivityPresenter providesMainActivityPresenter() {
        return new MainActivityPresenter(mMainActivity);
    }
}
