package com.rrajath.transittracker.ui.activity;

import android.app.Activity;
import android.content.Intent;
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
import com.rrajath.shared.util.Constants;
import com.rrajath.transittracker.R;
import com.rrajath.transittracker.WearApplication;
import com.rrajath.transittracker.di.module.MainActivityModule;
import com.rrajath.transittracker.ui.MainMenuItem;
import com.rrajath.transittracker.ui.adapter.MainMenuAdapter;
import com.rrajath.transittracker.ui.presenter.MainActivityPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import timber.log.Timber;

public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        WearableListView.ClickListener,
        MessageApi.MessageListener {

    private static final int NEARBY = 0;
    private static final int STARRED = 1;

    @Inject
    GoogleApiClient mGoogleApiClient;
    @Inject
    MainActivityPresenter mPresenter;

    private WearableListView mainMenuList;
    private String nodeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WearApplication.get(this)
                .getAppComponent()
                .plus(new MainActivityModule(this))
                .inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        buildGoogleApiClient();
        retrieveDeviceNode();

        View view = findViewById(R.id.box_inset_layout);
        view.setOnApplyWindowInsetsListener((view1, windowInsets) -> {
            // do something here
            mainMenuList = (WearableListView) view1.findViewById(R.id.lv_main_menu);
            loadAdapter();
            return windowInsets;
        });
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient.registerConnectionCallbacks(this);
        mGoogleApiClient.registerConnectionFailedListener(this);
        mGoogleApiClient.connect();
    }

    // Get the nearest connected device node id
    public void retrieveDeviceNode() {
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(
                getConnectedNodesResult -> Observable.from(getConnectedNodesResult.getNodes())
                        .map(node -> nodeId = node.getId())
                        .subscribe(nodeId -> Timber.d("NodeId: " + nodeId))
        );
    }

    // Load all items in the main menu into an adapter
    private void loadAdapter() {
        List<MainMenuItem> mainMenuItems = new ArrayList<>();
        mainMenuItems.add(new MainMenuItem(
                getString(R.string.nearbyStopsMenuItem), R.drawable.ic_my_location_green_500_24dp));
        mainMenuItems.add(new MainMenuItem(
                getString(R.string.starredStopsMenuItem), R.drawable.ic_favorite_red_500_24dp));

        MainMenuAdapter adapter = new MainMenuAdapter(this, mainMenuItems);
        mainMenuList.setAdapter(adapter);
        mainMenuList.setClickListener(this);
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
    public void onConnected(Bundle bundle) {
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Timber.d("onConnectionSuspended: connection to Google API Client was suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.d(String.format("connection to Google API Client failed: %s", connectionResult.getErrorMessage()));
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        switch (viewHolder.getLayoutPosition()) {
            case NEARBY:
                mPresenter.onNearbyMenuItemClick();
                break;
            case STARRED:
                mPresenter.onStarredMenuItemClick();
                break;
        }
    }

    @Override
    public void onTopEmptyRegionClick() {

    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(Constants.STOPS_LIST_PATH)) {
            Intent intent = new Intent(this, NearbyStopsListActivity.class);
            intent.putExtra("stopsListJson", messageEvent.getData());
            startActivity(intent);
        } else if (messageEvent.getPath().equals(Constants.FAVORITES_PATH)){
            Toast.makeText(MainActivity.this, "Got the fav list", Toast.LENGTH_SHORT).show();
            // start new activity with this list
        }
    }

    public void sendMessageToHandheld(final String path) {
        if (nodeId != null) {
            // TODO: Remove blockingConnect() to something else
            new Thread(() -> {
                mGoogleApiClient.blockingConnect(30, TimeUnit.SECONDS);
                Wearable.MessageApi.sendMessage(mGoogleApiClient, nodeId, path, null);
            }).start();
        } else {
            Toast.makeText(MainActivity.this, "No connected device found", Toast.LENGTH_SHORT).show();
        }
    }
}
