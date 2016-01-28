package com.rrajath.transittracker.presenter;

import com.google.common.collect.ImmutableList;
import com.rrajath.transittracker.data.WearStop;
import com.rrajath.transittracker.network.StopsManager;
import com.rrajath.transittracker.service.WearableListenerService;
import com.rrajath.transittracker.util.SimpleObserver;

import timber.log.Timber;

public class WearableListenerServicePresenter {
    WearableListenerService service;
    StopsManager mStopsManager;

    public WearableListenerServicePresenter(WearableListenerService service, StopsManager stopsManager) {
        this.service = service;
        this.mStopsManager = stopsManager;
    }

    public void handleOnGetStopsForLocation() {
        mStopsManager.getStopsForLocation(47.5990281, -122.3277492, 150)
                .subscribe(new SimpleObserver<ImmutableList<WearStop>>() {
                    @Override
                    public void onNext(ImmutableList<WearStop> wearStops) {
                        super.onNext(wearStops);
                        Timber.d("Number of wearStops returned: " + wearStops.size());
                        service.setStopsForLocation(wearStops);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Timber.d("Error occurred: " + e.getMessage());
                    }
                });
    }
}
