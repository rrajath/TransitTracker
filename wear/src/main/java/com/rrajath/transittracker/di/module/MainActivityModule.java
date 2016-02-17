package com.rrajath.transittracker.di.module;

import com.rrajath.transittracker.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {
    MainActivity mMainActivity;

    public MainActivityModule(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    @Provides
    @Singleton
    public MainActivity providesMainActivity() {
        return mMainActivity;
    }


}
