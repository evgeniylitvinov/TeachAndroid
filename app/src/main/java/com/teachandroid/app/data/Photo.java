package com.teachandroid.app.data;

import com.google.gson.annotations.SerializedName;


public class Photo {

    @SerializedName("id")
    private long userId;

    @SerializedName("photo_200_orig")
    private String photo200Orig;
    @SerializedName("photo_100")
    private String photo100;


    public long getUserId() {
        return userId;
    }

    public String getPhoto200Orig() {
        return photo200Orig;
    }

    public String getPhoto100() {
        return photo100;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }


    public void setPhoto200Orig(String photo200Orig) {
        this.photo200Orig = photo200Orig;
    }

    public void setPhoto100(String photo100) {
        this.photo100 = photo100;
    }


}
