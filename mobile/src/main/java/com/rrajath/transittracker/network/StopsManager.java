package com.rrajath.transittracker.network;

import com.google.common.collect.ImmutableList;
import com.rrajath.transittracker.data.WearStop;
import com.rrajath.transittracker.data.api.model.Data;
import com.rrajath.transittracker.data.api.model.Stop;
import com.rrajath.transittracker.data.api.response.StopsForLocationOutput;
import com.rrajath.transittracker.network.interfaces.TransitApiService;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class StopsManager {
    TransitApiService transitApiService;

    public StopsManager(TransitApiService transitApiService) {
        this.transitApiService = transitApiService;
    }

    public Observable<ImmutableList<WearStop>> getStopsForLocation(final double latitude, final double longitude, final int radius) {
        Observable<StopsForLocationOutput> stopsForLocationOutputObservable = transitApiService.getStopsForLocation(latitude, longitude, radius);
        return stopsForLocationOutputObservable
                .map(stopsForLocationOutput -> {
                    ImmutableList.Builder<WearStop> builder = ImmutableList.builder();
                    Timber.d(stopsForLocationOutput.toString());
                    Data data = stopsForLocationOutput.data;
                    for (Stop stop : data.list) {
                        WearStop wearStop = new WearStop();
                        wearStop.name = stop.name;
                        wearStop.code = stop.code;
                        wearStop.direction = stop.direction;
                        wearStop.routeIds = stop.routeIds;

                        builder.add(wearStop);
                    }
                    return builder.build();
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
