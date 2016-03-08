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
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.gson.Gson;
import com.rrajath.transittracker.TransitTrackerApplication;
import com.rrajath.transittracker.logging.AppLogger;
import com.rrajath.transittracker.network.StopsManager;
import com.rrajath.transittracker.util.LocationUtils;

import javax.inject.Inject;

import rx.Observable;

public class TransitTrackerService extends WearableListenerService implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Inject
    StopsManager mStopsManager;
    @Inject
    LocationUtils mLocationUtils;
    @Inject
    GoogleApiClient mGoogleApiClient;
    @Inject
    AppLogger mAppLogger;

    public static final String NEARBY_PATH = "/nearby";
    public static final String FAVORITES_PATH = "/starred";
    public static final String ARRIVALS_FOR_STOP_PATH = "/arrivalsForStop";

    private Location mLastLocation;
    private String nodeId;

    @Override
    public void onCreate() {
        TransitTrackerApplication.get(this)
                .createTransitTrackerServiceComponent(this)
                .inject(this);

        buildGoogleApiClient();
        retrieveDeviceNode();
        mAppLogger.d("Calling mLocationUtils.getCurrentLocation");
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
        switch (messageEvent.getPath()) {
            case NEARBY_PATH:
                if (mLastLocation == null) {
                    mLastLocation = mLocationUtils.getCurrentLocation(this);
                }

                Observable.just(mLocationUtils.getCurrentLocation(this))
                        .flatMap(location -> mStopsManager.getStopsForLocation(location.getLatitude(), location.getLongitude(), 150))
                        .map(wearStop -> {
                            String serializedJson = new Gson().toJson(wearStop);
                            mAppLogger.d(serializedJson);
                            return serializedJson.getBytes();
                        })
                        .subscribe(this::sendStopsToWear);
                break;

            case FAVORITES_PATH: {
                Handler handler = new Handler(getMainLooper());
                handler.post(() -> Toast.makeText(TransitTrackerService.this, "Favorites clicked on wear", Toast.LENGTH_SHORT).show());
                break;
            }
            case ARRIVALS_FOR_STOP_PATH: {
                Handler handler = new Handler(getMainLooper());
                handler.post(() -> Toast.makeText(TransitTrackerService.this, "Arrivals for Path", Toast.LENGTH_SHORT).show());
                break;
            }
        }
    }

    private void sendStopsToWear(byte[] data) {
        Wearable.MessageApi.sendMessage(mGoogleApiClient, nodeId, "stopsList", data);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mAppLogger.d("Connected");
        Wearable.DataApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mAppLogger.d("Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mAppLogger.d(String.format("Connection Failed. Reason: %s", connectionResult.getErrorMessage()));
    }

    public void retrieveDeviceNode() {
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(
                getConnectedNodesResult -> Observable.from(getConnectedNodesResult.getNodes())
                        .map(node -> nodeId = node.getId())
                        .subscribe(nodeId -> mAppLogger.d("NodeId: " + nodeId))
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
