package com.rrajath.shared.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class WearStop {
    private String name;
    private String direction;
    private List<String> routeIds;
    private String code;

    public WearStop(String name, String direction, List<String> routeIds, String code) {
        this.name = name;
        this.direction = direction;
        this.routeIds = routeIds;
        this.code = code;
    }

    public WearStop() {
        this.name = null;
        this.direction = null;
        this.routeIds = null;
        this.code = null;
    }

    public static WearStop fromJSON(JSONObject jsonObject) {
        WearStop wearStop = new WearStop();
        try {
            wearStop.setName(jsonObject.getString("name"));
            wearStop.setDirection(jsonObject.getString("direction"));
            JSONArray jsonRouteIds = jsonObject.getJSONArray("routeIds");
            List<String> routeIds = new ArrayList<>();
            for (int i=0; i<jsonRouteIds.length(); i++) {
                routeIds.add(jsonObject.getJSONArray("routeIds").getString(i));
            }
            wearStop.setRouteIds(routeIds);
            wearStop.setCode(jsonObject.getString("code"));
        } catch (JSONException e) {
            Timber.e("An error occurred while parsing JSON array: " + e.getMessage());
        }
        return wearStop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<String> getRouteIds() {
        return routeIds;
    }

    public void setRouteIds(List<String> routeIds) {
        this.routeIds = routeIds;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
