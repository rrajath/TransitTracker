package com.rrajath.transittracker.data.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Agency {

    @SerializedName("disclaimer")
    @Expose
    public String disclaimer;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("lang")
    @Expose
    public String lang;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("phone")
    @Expose
    public String phone;
    @SerializedName("privateService")
    @Expose
    public Boolean privateService;
    @SerializedName("timezone")
    @Expose
    public String timezone;
    @SerializedName("url")
    @Expose
    public String url;

}