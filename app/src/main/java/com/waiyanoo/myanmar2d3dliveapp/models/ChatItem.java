package com.waiyanoo.myanmar2d3dliveapp.models;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatItem  {

    public String userImage;
    public String userName;
    public String messageTxt;
    public long currentTime;

    public ChatItem(String userImage, String userName, String messageTxt, long currentTime) {
        this.userImage = userImage;
        this.userName = userName;
        this.messageTxt = messageTxt;
        this.currentTime = currentTime;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public ChatItem(String userImage, String userName, String messageTxt, Context context) {
        this.userImage = userImage;
        this.userName = userName;
        this.messageTxt = messageTxt;
    }

    public ChatItem() {
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessageTxt() {
        return messageTxt;
    }

    public void setMessageTxt(String messageTxt) {
        this.messageTxt = messageTxt;
    }
}

