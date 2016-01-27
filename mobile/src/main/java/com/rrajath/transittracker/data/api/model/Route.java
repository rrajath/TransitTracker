package com.rrajath.transittracker.data.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Route {
    @SerializedName("agencyId")
    @Expose
    public String agencyId;
    @SerializedName("color")
    @Expose
    public String color;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("longName")
    @Expose
    public String longName;
    @SerializedName("shortName")
    @Expose
    public String shortName;
    @SerializedName("textColor")
    @Expose
    public String textColor;
    @SerializedName("type")
    @Expose
    public Integer type;
    @SerializedName("url")
    @Expose
    public String url;
}