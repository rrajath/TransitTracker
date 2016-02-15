package com.rrajath.transittracker.network;

import com.google.common.collect.ImmutableList;
import com.rrajath.shared.model.WearStop;
import com.rrajath.transittracker.data.api.model.Data;
import com.rrajath.transittracker.data.api.model.Stop;
import com.rrajath.transittracker.data.api.response.StopsForLocationOutput;
import com.rrajath.transittracker.network.interfaces.OneBusAwayApiService;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class StopsManager {
    OneBusAwayApiService mOneBusAwayApiService;

    public StopsManager(OneBusAwayApiService oneBusAwayApiService) {
        this.mOneBusAwayApiService = oneBusAwayApiService;
    }

    public Observable<ImmutableList<WearStop>> getStopsForLocation(final double latitude, final double longitude, final int radius) {
        Observable<StopsForLocationOutput> stopsForLocationOutputObservable = mOneBusAwayApiService.getStopsForLocation(latitude, longitude, radius);
        return stopsForLocationOutputObservable
                .map(stopsForLocationOutput -> {
                    ImmutableList.Builder<WearStop> builder = ImmutableList.builder();
                    Data data = stopsForLocationOutput.data;
                    Timber.d("List of stops: " + data.list.size());
                    for (Stop stop : data.list) {
                        WearStop wearStop = new WearStop(stop.name, stop.direction, stop.routeIds, stop.code);

                        builder.add(wearStop);
                    }
                    return builder.build();
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
