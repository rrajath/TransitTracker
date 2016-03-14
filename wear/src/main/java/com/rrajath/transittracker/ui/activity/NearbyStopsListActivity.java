package com.rrajath.transittracker.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wearable.view.WearableListView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.rrajath.shared.model.WearStop;
import com.rrajath.shared.util.Constants;
import com.rrajath.transittracker.R;
import com.rrajath.transittracker.WearApplication;
import com.rrajath.transittracker.di.module.NearbyStopsListActivityModule;
import com.rrajath.transittracker.logging.AppLogger;
import com.rrajath.transittracker.ui.adapter.NearbyStopsAdapter;
import com.rrajath.transittracker.ui.presenter.NearbyStopsListActivityPresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;

public class NearbyStopsListActivity extends Activity implements
        WearableListView.ClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        MessageApi.MessageListener {

    private static final String ARRIVALS_FOR_STOP_PATH = "/arrivalsForStop";

    @Inject
    NearbyStopsListActivityPresenter mPresenter;
    @Inject
    GoogleApiClient mGoogleApiClient;
    @Inject
    AppLogger mAppLogger;

    WearableListView lvNearbyStops;
    private NearbyStopsAdapter mAdapter;
    private List<WearStop> mNearbyWearStops;
    private String nodeId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        WearApplication.get(this)
                .getAppComponent()
                .plus(new NearbyStopsListActivityModule(this))
                .inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nearby_stops);

        buildGoogleApiClient();
        retrieveDeviceNode();

        byte[] stopsListJson = getIntent().getExtras().getByteArray("stopsListJson");
        mPresenter.translateNearbyStopsList(stopsListJson);

        View view = findViewById(R.id.nearby_stops_box_inset_layout);
        view.setOnApplyWindowInsetsListener((view1, windowInsets) -> {
            lvNearbyStops = (WearableListView) view1.findViewById(R.id.lv_nearby_stops);
            loadAdapter();
            return windowInsets;
        });
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient.registerConnectionCallbacks(this);
        mGoogleApiClient.registerConnectionFailedListener(this);
        mGoogleApiClient.connect();
    }

    private void loadAdapter() {
        mAdapter = new NearbyStopsAdapter(this, mNearbyWearStops);
        lvNearbyStops.setAdapter(mAdapter);
        lvNearbyStops.setClickListener(this);
    }

    public void setNearbyWearStops(List<WearStop> nearbyWearStops) {
        mNearbyWearStops = nearbyWearStops;
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        WearStop wearStop = mNearbyWearStops.get(viewHolder.getLayoutPosition());
        Toast.makeText(NearbyStopsListActivity.this, mNearbyWearStops.get(viewHolder.getLayoutPosition()).getName(), Toast.LENGTH_SHORT).show();
        // send stop code to service on handheld
        sendMessageToHandheld(ARRIVALS_FOR_STOP_PATH, wearStop.getCode());
    }

    @Override
    public void onTopEmptyRegionClick() {

    }

    // Get the nearest connected device node id
    public void retrieveDeviceNode() {
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(
                getConnectedNodesResult -> Observable.from(getConnectedNodesResult.getNodes())
                        .map(node -> nodeId = node.getId())
                        .subscribe(nodeId -> mAppLogger.d("NodeId: " + nodeId))
        );
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mAppLogger.d("onConnectionSuspended: connection to Google API Client was suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mAppLogger.d(String.format("connection to Google API Client failed: %s", connectionResult.getErrorMessage()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        Wearable.MessageApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.MessageApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(Constants.STOP_SCHEDULES_PATH)) {
            mAppLogger.d(new String(messageEvent.getData()));
            Toast.makeText(NearbyStopsListActivity.this, "Yes!", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendMessageToHandheld(String path, String stopCode) {
        if (nodeId != null) {
            new Thread(() -> {
                mGoogleApiClient.blockingConnect(30, TimeUnit.SECONDS);
                Wearable.MessageApi.sendMessage(mGoogleApiClient, nodeId, path, stopCode.getBytes());
            }).start();
        } else {
            Toast.makeText(NearbyStopsListActivity.this, "No connected device found", Toast.LENGTH_SHORT).show();
        }
    }
}
