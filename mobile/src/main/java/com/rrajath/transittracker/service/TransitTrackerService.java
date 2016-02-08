package com.rrajath.transittracker.service;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.common.collect.ImmutableList;
import com.rrajath.transittracker.TransitTrackerApplication;
import com.rrajath.transittracker.data.WearStop;
import com.rrajath.transittracker.network.StopsManager;
import com.rrajath.transittracker.presenter.TransitTrackerServicePresenter;
import com.rrajath.transittracker.util.LocationUtils;

import javax.inject.Inject;

import rx.Observable;
import timber.log.Timber;

public class TransitTrackerService extends WearableListenerService implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Inject
    TransitTrackerServicePresenter presenter;
    @Inject
    StopsManager mStopsManager;
    @Inject
    LocationUtils mLocationUtils;
    @Inject
    GoogleApiClient mGoogleApiClient;

    public static final String NEARBY_PATH = "/nearby";
    public static final String FAVORITES_PATH = "/favorites";

    private Observable<ImmutableList<WearStop>> nearbyStopsForWear;
    private Location mLastLocation;

    @Override
    public void onCreate() {
        TransitTrackerApplication.get(this)
                .createTransitTrackerServiceComponent(this)
                .inject(this);
        buildGoogleApiClient();
        Timber.d("Calling mLocationUtils.getCurrentLocation");
        mLastLocation = mLocationUtils.getCurrentLocation(this);
        super.onCreate();
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient.registerConnectionCallbacks(this);
        mGoogleApiClient.registerConnectionFailedListener(this);
        mGoogleApiClient.connect();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(NEARBY_PATH)) {
/*
            Handler handler = new Handler(getMainLooper());
            handler.post(() -> {
                Toast.makeText(TransitTrackerService.this, "Nearby clicked on wear", Toast.LENGTH_SHORT).show();
                // Call API to get stops for current location
                presenter.handleOnGetStopsForLocation();
                // Send the populated list to wear
                if (!nearbyStopsForWear.isEmpty()) {
                    Toast.makeText(TransitTrackerService.this, "Got the stops", Toast.LENGTH_SHORT).show();
                }
            });
*/

            if (mLastLocation == null) {
                mLastLocation = mLocationUtils.getCurrentLocation(this);
            }
            nearbyStopsForWear = mStopsManager.getStopsForLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 150);
            Timber.d("Nearby stops for wear: " + nearbyStopsForWear);
        }

        if (messageEvent.getPath().equals(FAVORITES_PATH)) {
            Handler handler = new Handler(getMainLooper());
            handler.post(() -> Toast.makeText(TransitTrackerService.this, "Favorites clicked on wear", Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Timber.d("Connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Timber.d("Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.d(String.format("Connection Failed. Reason: %s", connectionResult.getErrorMessage()));
    }

    @Override
    public void onDestroy() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.unregisterConnectionCallbacks(this);
            mGoogleApiClient.unregisterConnectionFailedListener(this);
            mGoogleApiClient.disconnect();
        }
        super.onDestroy();
    }
}
