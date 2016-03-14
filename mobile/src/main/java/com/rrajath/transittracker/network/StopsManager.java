package com.rrajath.transittracker.network;

import com.google.common.collect.ImmutableList;
import com.rrajath.shared.model.WearStop;
import com.rrajath.shared.model.WearStopSchedule;
import com.rrajath.transittracker.data.api.model.ArrivalsAndDepartures;
import com.rrajath.transittracker.data.api.model.Data;
import com.rrajath.transittracker.data.api.model.Stop;
import com.rrajath.transittracker.data.api.response.ArrivalsAndDeparturesForStopOutput;
import com.rrajath.transittracker.data.api.response.StopsForLocationOutput;
import com.rrajath.transittracker.network.interfaces.OneBusAwayApiService;

import java.util.List;

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

    public Observable<ImmutableList<WearStopSchedule>> getArrivalsAndDeparturesForStop(String stopId) {
        Observable<ArrivalsAndDeparturesForStopOutput> outputObservable = mOneBusAwayApiService.getArrivalsAndDeparturesForStop(stopId);
        return outputObservable
                .map(output -> {
                    ImmutableList.Builder<WearStopSchedule> builder = ImmutableList.builder();
                    Timber.d("Reached StopsManager");
                    List<ArrivalsAndDepartures> arrivalsAndDeparturesList = output.data.entry.mArrivalsAndDepartures;
                    for (ArrivalsAndDepartures arrivalsAndDepartures : arrivalsAndDeparturesList) {
                        WearStopSchedule wearStopSchedule = new WearStopSchedule();
                        wearStopSchedule.setRouteShortName(arrivalsAndDepartures.routeShortName);
                        wearStopSchedule.setScheduledArrivalTime(arrivalsAndDepartures.scheduledArrivalTime);
                        wearStopSchedule.setPredictedArrivalTime(arrivalsAndDepartures.predictedArrivalTime);
                        wearStopSchedule.setTripHeadSign(arrivalsAndDepartures.tripHeadsign);

                        builder.add(wearStopSchedule);
                    }
                    return builder.build();
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
