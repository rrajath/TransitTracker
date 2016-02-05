package com.rrajath.transittracker.service;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.common.collect.ImmutableList;
import com.rrajath.transittracker.TransitTrackerApplication;
import com.rrajath.transittracker.data.WearStop;
import com.rrajath.transittracker.di.module.TransitTrackerServiceModule;
import com.rrajath.transittracker.presenter.TransitTrackerServicePresenter;
import com.rrajath.transittracker.util.PermissionUtils;

import javax.inject.Inject;

import timber.log.Timber;

public class TransitTrackerService extends WearableListenerService implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Inject
    TransitTrackerServicePresenter presenter;

    public static final String NEARBY_PATH = "/nearby";
    public static final String FAVORITES_PATH = "/favorites";

    private ImmutableList<WearStop> nearbyStopsForWear = new ImmutableList.Builder<WearStop>().build();
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @Override
    public void onCreate() {
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        TransitTrackerApplication.get(this)
                .getComponent()
                .plus(new TransitTrackerServiceModule(this))
                .inject(this);
        super.onCreate();
    }

    private void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Wearable.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(NEARBY_PATH)) {
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

        }

        if (messageEvent.getPath().equals(FAVORITES_PATH)) {
            Handler handler = new Handler(getMainLooper());
            handler.post(() -> Toast.makeText(TransitTrackerService.this, "Favorites clicked on wear", Toast.LENGTH_SHORT).show());
        }
    }

    public void setStopsForLocation(ImmutableList<WearStop> wearStops) {
        nearbyStopsForWear = wearStops;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Timber.d("Connected");
        // Check permissions and send notification (if needed)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.showPermissionsNotification(this);
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Timber.d(String.format("Lat: %s, Lon: %s", mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        }
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
            mGoogleApiClient.disconnect();
        }
        super.onDestroy();
    }
}
