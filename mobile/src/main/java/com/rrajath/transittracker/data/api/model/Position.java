package com.rrajath.transittracker.data.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Position {

    @SerializedName("lat")
    @Expose
    public Double lat;
    @SerializedName("lon")
    @Expose
    public Double lon;

}