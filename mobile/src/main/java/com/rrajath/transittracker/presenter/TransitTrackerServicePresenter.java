package com.rrajath.transittracker.presenter;

import com.google.common.collect.ImmutableList;
import com.rrajath.shared.model.WearStop;
import com.rrajath.transittracker.logging.AppLogger;
import com.rrajath.transittracker.network.StopsManager;
import com.rrajath.transittracker.service.TransitTrackerService;
import com.rrajath.transittracker.util.SimpleObserver;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TransitTrackerServicePresenter {
    TransitTrackerService service;
    StopsManager mStopsManager;
    AppLogger mAppLogger;

    public TransitTrackerServicePresenter(TransitTrackerService service, StopsManager stopsManager,
                                          AppLogger appLogger) {
        this.service = service;
        this.mStopsManager = stopsManager;
        this.mAppLogger = appLogger;
    }

    public void handleOnGetStopsForLocation() {
        mStopsManager.getStopsForLocation(47.5990281, -122.3277492, 150)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<ImmutableList<WearStop>>() {
                    @Override
                    public void onNext(ImmutableList<WearStop> wearStops) {
                        super.onNext(wearStops);
                        mAppLogger.d("Number of wearStops returned: " + wearStops.size());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mAppLogger.e("Error occurred: " + e.getMessage());
                    }
                });
    }
}
