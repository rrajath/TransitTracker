package com.rrajath.transittracker.service;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.rrajath.shared.model.WearStop;
import com.rrajath.transittracker.TransitTrackerApplication;
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
    private String nodeId;

    @Override
    public void onCreate() {
        TransitTrackerApplication.get(this)
                .createTransitTrackerServiceComponent(this)
                .inject(this);
        buildGoogleApiClient();
        retrieveDeviceNode();
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

            if (mLastLocation == null) {
                mLastLocation = mLocationUtils.getCurrentLocation(this);
            }

            Observable.just(mLocationUtils.getCurrentLocation(this))
                    .flatMap(location -> mStopsManager.getStopsForLocation(location.getLatitude(), location.getLongitude(), 150))
                    .map(wearStop -> {
                        String serializedJson = new Gson().toJson(wearStop);
                        Timber.d(serializedJson);
                        return serializedJson.getBytes();
                    })
                    .subscribe(this::sendStopsToWear);
        }

        if (messageEvent.getPath().equals(FAVORITES_PATH)) {
            Handler handler = new Handler(getMainLooper());
            handler.post(() -> Toast.makeText(TransitTrackerService.this, "Favorites clicked on wear", Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
    }

    private void sendStopsToWear(byte[] data) {
        Wearable.MessageApi.sendMessage(mGoogleApiClient, nodeId, "stopsList", data);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Timber.d("Connected");
        Wearable.DataApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Timber.d("Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.d(String.format("Connection Failed. Reason: %s", connectionResult.getErrorMessage()));
    }

    public void retrieveDeviceNode() {
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(
                getConnectedNodesResult -> Observable.from(getConnectedNodesResult.getNodes())
                        .map(node1 -> nodeId = node1.getId())
                        .subscribe(node -> Timber.d("NodeId: " + node))
        );
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
