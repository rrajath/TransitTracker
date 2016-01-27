package com.rrajath.transittracker;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, WearableListView.ClickListener, MessageApi.MessageListener {

    private WearableListView mainMenuList;
    GoogleApiClient mGoogleApiClient;
    private String nodeId;
    private List<MainMenuItem> mainMenuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        initApi();
        retrieveDeviceNode();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

/*
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mainMenuList = (WearableListView) stub.findViewById(R.id.lv_main_menu);
                loadAdapter();
            }
        });
*/
        View view = findViewById(R.id.box_inset_layout);
        view.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                // do something here
                mainMenuList = (WearableListView) view.findViewById(R.id.lv_main_menu);
                loadAdapter();
                return windowInsets;
            }
        });
    }

    // Initialize Google API Client
    private void initApi() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    // Get the nearest connected device node id
    public void retrieveDeviceNode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Since this is a blocking connect, it must be done on a different thread
                mGoogleApiClient.blockingConnect(30, TimeUnit.SECONDS);
                NodeApi.GetConnectedNodesResult result = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
                List<Node> nodes = result.getNodes();
                if (nodes.size() > 0) {
                    nodeId = nodes.get(0).getId();
                }
            }
        }).start();
    }

    // Load all items in the main menu into an adapter
    private void loadAdapter() {
        mainMenuItems = new ArrayList<>();
        mainMenuItems.add(new MainMenuItem("Nearby", R.drawable.ic_my_location_green_500_24dp));
        mainMenuItems.add(new MainMenuItem("Favorites", R.drawable.ic_favorite_red_500_24dp));

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
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Timber.d(String.format("connection to Google API Client failed with status code: %d and " +
                "reason: %s", connectionResult.getErrorCode(), connectionResult.getErrorMessage()));
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        Integer tag = (Integer) viewHolder.itemView.getTag();
        Toast.makeText(MainActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
        String path = "/" + mainMenuItems.get(viewHolder.getLayoutPosition()).title.toLowerCase();
        Timber.d("PATH on wear: " + path);
        sendToast(path);
    }

    @Override
    public void onTopEmptyRegionClick() {

    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

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
