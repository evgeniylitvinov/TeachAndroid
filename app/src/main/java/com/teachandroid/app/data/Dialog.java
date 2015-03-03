package com.teachandroid.app.data;


import com.google.gson.annotations.SerializedName;

public class Dialog {

    @SerializedName("unread")
    private int unread;

    @SerializedName("message")
    private Message message;

    public int getUnread() {
        return unread;
    }

    public Message getMessage() {
        return message;
    }
}
