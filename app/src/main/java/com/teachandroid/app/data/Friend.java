package com.teachandroid.app.data;

import com.google.gson.annotations.SerializedName;

public class Friend {

    @SerializedName("id")
    private long userId;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("photo_200_orig")
    private String photo200Orig;
    @SerializedName("photo_100")
    private String photo100;
    @SerializedName("online")
    private int online;

    public long getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPhoto200Orig(String photo200Orig) {
        this.photo200Orig = photo200Orig;
    }

    public void setPhoto100(String photo100) {
        this.photo100 = photo100;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", photo200Orig='" + photo200Orig + '\'' +
                ", photo100='" + photo100 + '\'' +
                ", online=" + online +
                '}';
    }


}
