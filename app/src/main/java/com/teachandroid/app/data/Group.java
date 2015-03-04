package com.teachandroid.app.data;

import com.google.gson.annotations.SerializedName;
/**
 * Created by rakovskyi on 26.02.15.
 *gid 	Community ID.
 positive number
 name 	Community name.
 string
 photo 	URL of the 50px-wide community logo.
 string
 */
public class Group {
    @SerializedName("id")
     private  int gid;
    @SerializedName("name")
    private  String name;
    @SerializedName("photo_50")
    private String photo;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
