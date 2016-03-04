package com.rrajath.transittracker.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.rrajath.transittracker.logging.AppLogger;

import javax.inject.Inject;

public class LocationUtils {
    @Inject
    GoogleApiClient mGoogleApiClient;
    @Inject
    LocationManager locationManager;
    @Inject
    AppLogger mAppLogger;

    private Location mLastLocation;

    public LocationUtils(GoogleApiClient googleApiClient, LocationManager locationManager,
                         AppLogger appLogger) {
        this.mGoogleApiClient = googleApiClient;
        this.locationManager = locationManager;
        this.mAppLogger = appLogger;
    }

    public Location getCurrentLocation(Context context) {
        if (mLastLocation == null) {
            mAppLogger.d("In getCurrentLocation: mLastLocation is null. Calling initLocationIfNeeded()");
            initLocationIfNeeded(context);
        }
        return mLastLocation;
    }

    private void initLocationIfNeeded(Context context) {
        // Check permissions and send notification (if needed)
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mAppLogger.d("No permissions granted. Showing notification");
            PermissionUtils.showPermissionsNotification(context);
        } else {
            mAppLogger.d("Permissions granted");
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                mAppLogger.d(String.format("Lat: %s, Lon: %s", mLastLocation.getLatitude(), mLastLocation.getLongitude()));
            } else {
                mLastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
    }
}
