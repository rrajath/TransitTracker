package com.rrajath.transittracker.di.component;

import com.rrajath.transittracker.di.module.MainActivityModule;
import com.rrajath.transittracker.ui.ActivityScope;
import com.rrajath.transittracker.ui.activity.MainActivity;
import com.rrajath.transittracker.ui.presenter.MainActivityPresenter;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(
        modules = MainActivityModule.class
)
public interface MainActivityComponent {
    void inject(MainActivity mainActivity);

    MainActivityPresenter presenter();
}
