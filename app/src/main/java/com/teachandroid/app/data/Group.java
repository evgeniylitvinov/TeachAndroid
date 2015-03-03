package com.teachandroid.app.data;

import com.google.gson.annotations.SerializedName;

import java.net.URI;

public class Group {

    @SerializedName("id")
    private long id ;

    @SerializedName("name")
    private String name;

    @SerializedName("screen_name")
    private String screenName;

    @SerializedName("photo_50")
    private String photo_50;

    @SerializedName("photo_100")
    private String photo_100;

    @SerializedName("photo_200")
    private String photo_200;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getPhoto_50() {
        return photo_50;
    }

}
