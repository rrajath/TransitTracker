package com.rrajath.transittracker.data.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class TripStatus {

    @SerializedName("activeTripId")
    @Expose
    public String activeTripId;
    @SerializedName("blockTripSequence")
    @Expose
    public Integer blockTripSequence;
    @SerializedName("closestStop")
    @Expose
    public String closestStop;
    @SerializedName("closestStopTimeOffset")
    @Expose
    public Integer closestStopTimeOffset;
    @SerializedName("distanceAlongTrip")
    @Expose
    public Double distanceAlongTrip;
    @SerializedName("frequency")
    @Expose
    public Object frequency;
    @SerializedName("lastKnownDistanceAlongTrip")
    @Expose
    public Integer lastKnownDistanceAlongTrip;
    @SerializedName("lastKnownLocation")
    @Expose
    public LastKnownLocation lastKnownLocation;
    @SerializedName("lastKnownOrientation")
    @Expose
    public Integer lastKnownOrientation;
    @SerializedName("lastLocationUpdateTime")
    @Expose
    public Integer lastLocationUpdateTime;
    @SerializedName("lastUpdateTime")
    @Expose
    public Integer lastUpdateTime;
    @SerializedName("nextStop")
    @Expose
    public String nextStop;
    @SerializedName("nextStopTimeOffset")
    @Expose
    public Integer nextStopTimeOffset;
    @SerializedName("orientation")
    @Expose
    public Double orientation;
    @SerializedName("phase")
    @Expose
    public String phase;
    @SerializedName("position")
    @Expose
    public Position position;
    @SerializedName("predicted")
    @Expose
    public Boolean predicted;
    @SerializedName("scheduleDeviation")
    @Expose
    public Integer scheduleDeviation;
    @SerializedName("scheduledDistanceAlongTrip")
    @Expose
    public Double scheduledDistanceAlongTrip;
    @SerializedName("serviceDate")
    @Expose
    public Integer serviceDate;
    @SerializedName("situationIds")
    @Expose
    public List<Object> situationIds = new ArrayList<Object>();
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("totalDistanceAlongTrip")
    @Expose
    public Double totalDistanceAlongTrip;
    @SerializedName("vehicleId")
    @Expose
    public String vehicleId;

}