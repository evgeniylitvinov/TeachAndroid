package com.teachandroid.app.data;

import com.google.gson.annotations.SerializedName;

public class AudioAlbum {

    @SerializedName("id")
    private long id;

    @SerializedName("owner_id")
    private long ownerId;

    @SerializedName("title")
    private String title;

    public long getId() {
        return id;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public String getTitle() {
        return title;
    }
}
