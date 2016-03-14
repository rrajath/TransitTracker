package com.rrajath.transittracker.network.interfaces;

import com.rrajath.transittracker.data.api.response.ArrivalsAndDeparturesForStopOutput;
import com.rrajath.transittracker.data.api.response.StopsForLocationOutput;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface OneBusAwayApiService {
    @GET("/api/where/stops-for-location.json?key=TEST")
    Observable<StopsForLocationOutput> getStopsForLocation(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("radius") int radius
    );

    @GET("/api/where/arrivals-and-departures-for-stop/{stopId}.json?key=TEST")
    Observable<ArrivalsAndDeparturesForStopOutput> getArrivalsAndDeparturesForStop(
            @Path("stopId") String stopId
    );
}
