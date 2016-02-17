package com.rrajath.transittracker.di.component;

import com.rrajath.transittracker.di.module.MainActivityModule;
import com.rrajath.transittracker.ui.MainActivity;

import dagger.Subcomponent;

@Subcomponent(
        modules = MainActivityModule.class
)
public interface MainActivityComponent {
    void inject(MainActivity mainActivity);
}
