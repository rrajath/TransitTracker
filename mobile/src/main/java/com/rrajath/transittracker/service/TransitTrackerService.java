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
import com.rrajath.shared.util.Constants;
import com.rrajath.transittracker.TransitTrackerApplication;
import com.rrajath.transittracker.logging.AppLogger;
import com.rrajath.transittracker.network.StopsManager;
import com.rrajath.transittracker.util.LocationUtils;

import javax.inject.Inject;

import rx.Observable;
import timber.log.Timber;

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

    private Location mLastLocation;
    private String nodeId;

    @Override
    public void onCreate() {
        TransitTrackerApplication.get(this)
                .createTransitTrackerServiceComponent(this)
                .inject(this);

        buildGoogleApiClient();
        retrieveDeviceNode();
        mAppLogger.d("Working?");
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
            case Constants.NEARBY_STOPS_PATH:
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
                        .subscribe(data -> sendDataToWear(Constants.STOPS_LIST_PATH, data));
                break;

            case Constants.FAVORITES_PATH: {
                Handler handler = new Handler(getMainLooper());
                handler.post(() -> Toast.makeText(TransitTrackerService.this, "Favorites clicked on wear", Toast.LENGTH_SHORT).show());
                break;
            }
            case Constants.ARRIVALS_FOR_STOP_PATH: {
                String stopId = new String(messageEvent.getData());
                Timber.d("Reached Arrivals for stop");
                Observable.just(mStopsManager.getArrivalsAndDeparturesForStop(stopId))
                        .map(wearStopSchedule -> {

                            String serializedJson = new Gson().toJson(wearStopSchedule);
                            return serializedJson.getBytes();
                        })
                        .subscribe(data -> sendDataToWear(Constants.STOP_SCHEDULES_PATH, data));
                Handler handler = new Handler(getMainLooper());
                handler.post(() -> Toast.makeText(TransitTrackerService.this, "Arrivals for Path", Toast.LENGTH_SHORT).show());
                break;
            }
        }
    }

    private void sendDataToWear(String path, byte[] data) {
        Wearable.MessageApi.sendMessage(mGoogleApiClient, nodeId, path, data);
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
                        .map(node -> nodeId = node.getId())
                        .subscribe(nodeId -> Timber.d("NodeId: " + nodeId))
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
