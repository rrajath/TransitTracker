package com.rrajath.transittracker.data.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rrajath.transittracker.data.api.model.Data;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class ArrivalsAndDeparturesForStopOutput {

    @SerializedName("code")
    @Expose
    public Integer code;
    @SerializedName("currentTime")
    @Expose
    public Integer currentTime;
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