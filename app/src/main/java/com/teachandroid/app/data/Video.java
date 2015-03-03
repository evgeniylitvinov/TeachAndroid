package com.teachandroid.app.data;

import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("id")
    private long id ;

    @SerializedName("owner_id")
    private long owner_id ;

    @SerializedName("title")
    private String title ;

    @SerializedName("duration")
    private long duration ;

    @SerializedName("description")
    private String description ;

    @SerializedName("date")
    private long date ;

    @SerializedName("views")
    private long views;

    @SerializedName("comments")
    private long comments;

    public long getId() {
        return id;
    }

    public long getOwner_id() {
        return owner_id;
    }

    @SerializedName("photo_130")
    private String photo_130;

    @SerializedName("photo_320")
    private String photo_320;

    @SerializedName("photo_640")
    private String photo_640;

    @SerializedName("adding_date")
    private long adding_date;

    @SerializedName("player")
    private String player;
}
