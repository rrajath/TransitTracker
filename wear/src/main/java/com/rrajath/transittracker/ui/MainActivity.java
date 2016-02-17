package com.rrajath.transittracker.ui;

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
import com.rrajath.transittracker.BuildConfig;
import com.rrajath.transittracker.R;
import com.rrajath.transittracker.WearApplication;
import com.rrajath.transittracker.di.module.MainActivityModule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    @Inject
    GoogleApiClient mGoogleApiClient;

    private WearableListView mainMenuList;
    private String nodeId;
    private List<MainMenuItem> mainMenuItems;
    private static final String STOPS_LIST_PATH = "stopsList";

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
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

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
        mainMenuItems = new ArrayList<>();
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
        Toast.makeText(MainActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
        String path = "/" + mainMenuItems.get(viewHolder.getLayoutPosition()).title.toLowerCase();
        Timber.d("PATH on wear: " + path);
        // TODO: Replace this with more robust string mapping
        sendToast("/nearby");
    }

    @Override
    public void onTopEmptyRegionClick() {

    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(STOPS_LIST_PATH)) {
            byte[] bytes = messageEvent.getData();
            try {
                JSONArray jsonArray = new JSONArray(new String(bytes));
                JSONObject jsonObject;
                List<WearStop> wearStops = new ArrayList<>();
                for (int i=0; i<jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    wearStops.add(WearStop.fromJSON(jsonObject));
                }
                Timber.d("Final WearStops List: " + wearStops.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(MainActivity.this, "Got the list", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendToast(final String path) {
        if (nodeId != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mGoogleApiClient.blockingConnect(30, TimeUnit.SECONDS);
                    Wearable.MessageApi.sendMessage(mGoogleApiClient, nodeId, path, null);
                }
            }).start();
        } else {
            Toast.makeText(MainActivity.this, "No connected device found", Toast.LENGTH_SHORT).show();
        }
    }
}
