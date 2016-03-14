package com.rrajath.transittracker.data.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Entry {

    @SerializedName("arrivalsAndDepartures")
    @Expose
    public List<ArrivalsAndDepartures> mArrivalsAndDepartures = new ArrayList<ArrivalsAndDepartures>();
    @SerializedName("nearbyStopIds")
    @Expose
    public List<String> nearbyStopIds = new ArrayList<String>();
    @SerializedName("situationIds")
    @Expose
    public List<Object> situationIds = new ArrayList<Object>();
    @SerializedName("stopId")
    @Expose
    public String stopId;

}