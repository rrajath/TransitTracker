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
import com.rrajath.transittracker.TransitTrackerApplication;
import com.rrajath.transittracker.data.WearStop;
import com.rrajath.transittracker.network.StopsManager;
import com.rrajath.transittracker.presenter.TransitTrackerServicePresenter;
import com.rrajath.transittracker.util.LocationUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
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
                    .flatMap(new Func1<List<WearStop>, Observable<WearStop>>() {
                        @Override
                        public Observable<WearStop> call(List<WearStop> wearStops) {
                            return Observable.from(wearStops);
                        }
                    })
                    .subscribe(wearStop -> Timber.d("WearStopObject: " + wearStop), error -> Timber.e("Error: " + error));

            // TODO: Figure out how to send complex objects to wear
/*
            nearbyStopsForWear = mStopsManager.getStopsForLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 150);
            nearbyStopsForWear
                    .map(wearStops -> {
                        PutDataMapRequest dataMapRequest = PutDataMapRequest.create("/returned-nearby-wear-stops");
                        dataMapRequest.setUrgent();
                        DataMap dataMap = dataMapRequest.getDataMap();
                        for (WearStop wearStop : wearStops) {
                            dataMap.clear();
                            dataMap = wearStop.putToDataMap();
                            dataMap.putLong("timestamp", new Date().getTime());
                            Wearable.DataApi.putDataItem(mGoogleApiClient, dataMapRequest.asPutDataRequest())
                            .setResultCallback(dataItemResult -> Timber.d(dataItemResult.getStatus().getStatusMessage()));
                        }
                        return wearStops;
                    })
                    .subscribe();
*/

            // iterate through all wear stops
            // set path and count of wear stops and send this data packet
            // call putDataItem() for each so that they all go to the wear device
            // update wear device. live??

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

    private void sendStopsToWear() {
        Wearable.MessageApi.sendMessage(mGoogleApiClient, nodeId, "stopsList", null);
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
