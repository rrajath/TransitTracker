package com.rrajath.transittracker.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.wearable.view.WearableListView;
import android.view.View;

import com.rrajath.shared.model.WearStop;
import com.rrajath.transittracker.R;
import com.rrajath.transittracker.WearApplication;
import com.rrajath.transittracker.di.module.NearbyStopsListActivityModule;
import com.rrajath.transittracker.ui.adapter.NearbyStopsAdapter;
import com.rrajath.transittracker.ui.presenter.NearbyStopsListActivityPresenter;

import java.util.List;

import javax.inject.Inject;

public class NearbyStopsListActivity extends Activity {
    @Inject
    NearbyStopsListActivityPresenter mPresenter;

    WearableListView lvNearbyStops;
    private NearbyStopsAdapter mAdapter;
    private List<WearStop> mNearbyWearStops;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        WearApplication.get(this)
                .getAppComponent()
                .plus(new NearbyStopsListActivityModule(this))
                .inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nearby_stops);

        View view = findViewById(R.id.nearby_stops_box_inset_layout);
        view.setOnApplyWindowInsetsListener((view1, windowInsets) -> {
            lvNearbyStops = (WearableListView) view1.findViewById(R.id.lv_nearby_stops);
            lvNearbyStops.setLayoutManager(new LinearLayoutManager(this));
            loadAdapter();
            String stopsListJson = NearbyStopsListActivity.this.getIntent().getStringExtra("stopsListJson");
            mPresenter.translateNearbyStopsList(stopsListJson.getBytes());
            return windowInsets;
        });

    }

    private void loadAdapter() {
        mAdapter = new NearbyStopsAdapter(mNearbyWearStops);
        lvNearbyStops.setAdapter(mAdapter);
    }

    public void setNearbyWearStops(List<WearStop> nearbyWearStops) {
        mNearbyWearStops = nearbyWearStops;
    }


}
