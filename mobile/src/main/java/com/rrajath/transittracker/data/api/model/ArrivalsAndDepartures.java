package com.rrajath.transittracker.data.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class ArrivalsAndDepartures {

    @SerializedName("arrivalEnabled")
    @Expose
    public Boolean arrivalEnabled;
    @SerializedName("blockTripSequence")
    @Expose
    public Integer blockTripSequence;
    @SerializedName("departureEnabled")
    @Expose
    public Boolean departureEnabled;
    @SerializedName("distanceFromStop")
    @Expose
    public Double distanceFromStop;
    @SerializedName("frequency")
    @Expose
    public Object frequency;
    @SerializedName("lastUpdateTime")
    @Expose
    public Integer lastUpdateTime;
    @SerializedName("numberOfStopsAway")
    @Expose
    public Integer numberOfStopsAway;
    @SerializedName("predicted")
    @Expose
    public Boolean predicted;
    @SerializedName("predictedArrivalInterval")
    @Expose
    public Object predictedArrivalInterval;
    @SerializedName("predictedArrivalTime")
    @Expose
    public Integer predictedArrivalTime;
    @SerializedName("predictedDepartureInterval")
    @Expose
    public Object predictedDepartureInterval;
    @SerializedName("predictedDepartureTime")
    @Expose
    public Integer predictedDepartureTime;
    @SerializedName("routeId")
    @Expose
    public String routeId;
    @SerializedName("routeLongName")
    @Expose
    public String routeLongName;
    @SerializedName("routeShortName")
    @Expose
    public String routeShortName;
    @SerializedName("scheduledArrivalInterval")
    @Expose
    public Object scheduledArrivalInterval;
    @SerializedName("scheduledArrivalTime")
    @Expose
    public Integer scheduledArrivalTime;
    @SerializedName("scheduledDepartureInterval")
    @Expose
    public Object scheduledDepartureInterval;
    @SerializedName("scheduledDepartureTime")
    @Expose
    public Integer scheduledDepartureTime;
    @SerializedName("serviceDate")
    @Expose
    public Integer serviceDate;
    @SerializedName("situationIds")
    @Expose
    public List<Object> situationIds = new ArrayList<Object>();
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("stopId")
    @Expose
    public String stopId;
    @SerializedName("stopSequence")
    @Expose
    public Integer stopSequence;
    @SerializedName("tripHeadsign")
    @Expose
    public String tripHeadsign;
    @SerializedName("tripId")
    @Expose
    public String tripId;
    @SerializedName("tripStatus")
    @Expose
    public TripStatus tripStatus;
    @SerializedName("vehicleId")
    @Expose
    public String vehicleId;

}