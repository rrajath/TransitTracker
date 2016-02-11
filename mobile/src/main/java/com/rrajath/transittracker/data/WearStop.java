package com.rrajath.transittracker.data;

import com.google.android.gms.wearable.DataMap;

import java.util.ArrayList;
import java.util.List;

public class WearStop {
    public String name;
    public String direction;
    public List<String> routeIds;
    public String code;

    public WearStop(String name, String direction, List<String> routeIds, String code) {
        this.name = name;
        this.direction = direction;
        this.routeIds = routeIds;
        this.code = code;
    }

    public WearStop(DataMap dataMap) {
        this(
                dataMap.get("name"),
                dataMap.get("direction"),
                dataMap.getStringArrayList("routeIds"),
                dataMap.getString("code")
        );
    }

    public DataMap putToDataMap() {
        DataMap dataMap = new DataMap();
        dataMap.putString("name", name);
        dataMap.putString("direction", direction);
        dataMap.putStringArrayList("routeIds", (ArrayList<String>) routeIds);
        dataMap.putString("code", code);
        return dataMap;
    }
}
