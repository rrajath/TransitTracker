package com.rrajath.transittracker.network.interfaces;

import com.rrajath.transittracker.data.api.response.StopsForLocationOutput;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface TransitApiService {
    @GET("/api/where/stops-for-location.json?key=TEST")
    Observable<StopsForLocationOutput> getStopsForLocation(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("radius") int radius
    );
}
