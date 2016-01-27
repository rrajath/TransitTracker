package com.rrajath.transittracker.data.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class References {
    @SerializedName("agencies")
    @Expose
    public List<Agency> agencies = new ArrayList<Agency>();
    @SerializedName("routes")
    @Expose
    public List<Route> routes = new ArrayList<Route>();
    @SerializedName("situations")
    @Expose
    public List<Object> situations = new ArrayList<Object>();
    @SerializedName("stops")
    @Expose
    public List<Object> stops = new ArrayList<Object>();
    @SerializedName("trips")
    @Expose
    public List<Object> trips = new ArrayList<Object>();
}
