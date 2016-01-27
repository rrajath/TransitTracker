package com.rrajath.transittracker.data.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Stop {

    @SerializedName("code")
    @Expose
    public String code;
    @SerializedName("direction")
    @Expose
    public String direction;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("lat")
    @Expose
    public Double lat;
    @SerializedName("locationType")
    @Expose
    public Integer locationType;
    @SerializedName("lon")
    @Expose
    public Double lon;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("routeIds")
    @Expose
    public List<String> routeIds = new ArrayList<String>();
    @SerializedName("wheelchairBoarding")
    @Expose
    public String wheelchairBoarding;

}