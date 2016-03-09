package com.rrajath.shared.model;

public class WearStopSchedule {
    private String direction;
    private String stopName;
    private String routeShortName;
    private String tripHeadSign;
    private long scheduledArrivalTime;
    private long predictedArrivalTime;

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public void setRouteShortName(String routeShortName) {
        this.routeShortName = routeShortName;
    }

    public void setTripHeadSign(String tripHeadSign) {
        this.tripHeadSign = tripHeadSign;
    }

    public void setScheduledArrivalTime(long scheduledArrivalTime) {
        this.scheduledArrivalTime = scheduledArrivalTime;
    }

    public void setPredictedArrivalTime(long predictedArrivalTime) {
        this.predictedArrivalTime = predictedArrivalTime;
    }
}
