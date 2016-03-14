package com.rrajath.transittracker.logging;

import timber.log.Timber;

public class AppLogger {

    public AppLogger() {
        Timber.plant(new Timber.DebugTree());
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
