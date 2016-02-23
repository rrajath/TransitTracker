package com.rrajath.transittracker.ui.presenter;

import com.rrajath.shared.model.WearStop;
import com.rrajath.transittracker.ui.activity.NearbyStopsListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class NearbyStopsListActivityPresenter {
    NearbyStopsListActivity mStopsListActivity;

    public NearbyStopsListActivityPresenter(NearbyStopsListActivity stopsListActivity) {
        mStopsListActivity = stopsListActivity;
    }

    public void translateNearbyStopsList(byte[] data) {
        List<WearStop> wearStops = new ArrayList<>();
        JSONObject jsonObject;
        try {
            JSONArray jsonArray = new JSONArray(new String(data));
            for (int i=0; i<jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                wearStops.add(WearStop.fromJSON(jsonObject));
            }
            Timber.d("Final WearStops List: " + wearStops.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mStopsListActivity.setNearbyWearStops(wearStops);
    }

}
