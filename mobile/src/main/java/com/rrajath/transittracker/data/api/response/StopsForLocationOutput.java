package com.rrajath.transittracker.data.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rrajath.transittracker.data.api.model.Data;

public class StopsForLocationOutput {

    @SerializedName("code")
    @Expose
    public Integer code;
    @SerializedName("currentTime")
    @Expose
    public Double currentTime;
    @SerializedName("data")
    @Expose
    public Data data;
    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("version")
    @Expose
    public Integer version;

}