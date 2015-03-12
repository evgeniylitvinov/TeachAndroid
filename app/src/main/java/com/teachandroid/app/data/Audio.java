package com.teachandroid.app.data;


import com.google.gson.annotations.SerializedName;

public class Audio {

    @SerializedName("id")
    private long id ;

    @SerializedName("owner_id")
    private long ownerId ;

    @SerializedName("artist")
    private String artist ;

    @SerializedName("title")
    private String title ;

    @SerializedName("duration")
    private int duration ;

    @SerializedName("url")
    private String url ;

    @SerializedName("lyrics_id")
    private int lyricsId;

    @SerializedName("genre")
    private int genre ;

    public long getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }
}
