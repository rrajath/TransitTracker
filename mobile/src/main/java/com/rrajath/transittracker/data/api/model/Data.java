package com.rrajath.transittracker.data.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Data {

    @SerializedName("limitExceeded")
    @Expose
    public Boolean limitExceeded;
    @SerializedName("list")
    @Expose
    public List<Stop> list = new ArrayList<Stop>();
    @SerializedName("outOfRange")
    @Expose
    public Boolean outOfRange;
    @SerializedName("references")
    @Expose
    public References references;

}