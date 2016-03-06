package com.rrajath.transittracker.logging;

import android.app.Application;

import com.rrajath.shared.BuildConfig;

import timber.log.Timber;

public class AppLogger {
    Application mApplication;

    public AppLogger(Application application) {
        mApplication = application;
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public void d(String message) {
        Timber.d(message);
    }

    public void e(String message) {
        Timber.e(message);
    }

    public void w(String message) {
        Timber.w(message);
    }

    public void i(String message) {
        Timber.i(message);
    }
}
