package com.rrajath.transittracker.service;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.common.collect.ImmutableList;
import com.rrajath.transittracker.TransitTrackerApplication;
import com.rrajath.transittracker.data.WearStop;
import com.rrajath.transittracker.di.module.WearableListenerServiceModule;
import com.rrajath.transittracker.presenter.WearableListenerServicePresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class WearableListenerService extends com.google.android.gms.wearable.WearableListenerService {

    @Inject
    WearableListenerServicePresenter presenter;

    public static final String TAG = "DataLayer";
    public static final String NEARBY_PATH = "/nearby";
    public static final String FAVORITES_PATH = "/favorites";

    private ImmutableList<WearStop> nearbyStopsForWear = new ImmutableList.Builder<WearStop>().build();

    @Override
    public void onCreate() {
        super.onCreate();
        TransitTrackerApplication.get(this)
                .getComponent()
                .plus(new WearableListenerServiceModule(this))
                .inject(this);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "onMessageReceived: " + messageEvent);
        }

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        ConnectionResult connectionResult = googleApiClient.blockingConnect(30, TimeUnit.SECONDS);

        if (!connectionResult.isSuccess()) {
            Log.e(TAG, "Failed to connect to GoogleApiClient");
            return;
        }

        if (messageEvent.getPath().equals(NEARBY_PATH)) {
            Handler handler = new Handler(getMainLooper());
            handler.post(() -> {
                Toast.makeText(WearableListenerService.this, "Nearby clicked on wear", Toast.LENGTH_SHORT).show();
                // Call API to get stops for current location
                presenter.handleOnGetStopsForLocation();
                // Send the populated list to wear
                if (!nearbyStopsForWear.isEmpty()) {
                    Toast.makeText(WearableListenerService.this, "Got the stops", Toast.LENGTH_SHORT).show();
                }
            });

        }

        if (messageEvent.getPath().equals(FAVORITES_PATH)) {
            Handler handler = new Handler(getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(WearableListenerService.this, "Favorites clicked on wear", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void setStopsForLocation(ImmutableList<WearStop> wearStops) {
        nearbyStopsForWear = wearStops;
    }
}
