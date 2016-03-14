package com.rrajath.transittracker.data.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Trip {

    @SerializedName("blockId")
    @Expose
    public String blockId;
    @SerializedName("directionId")
    @Expose
    public String directionId;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("routeId")
    @Expose
    public String routeId;
    @SerializedName("routeShortName")
    @Expose
    public String routeShortName;
    @SerializedName("serviceId")
    @Expose
    public String serviceId;
    @SerializedName("shapeId")
    @Expose
    public String shapeId;
    @SerializedName("timeZone")
    @Expose
    public String timeZone;
    @SerializedName("tripHeadsign")
    @Expose
    public String tripHeadsign;
    @SerializedName("tripShortName")
    @Expose
    public String tripShortName;

}