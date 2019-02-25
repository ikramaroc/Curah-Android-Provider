package com.curahservice.netset.module.chat;

import android.graphics.drawable.Drawable;

public class ChatModel {
    private String messageFrom, messageType, message;
    private Drawable img;

    ChatModel(String messageFrom, String messageType, String message, Drawable img) {
        this.messageFrom = messageFrom;
        this.messageType = messageType;
        this.message = message;
        this.img = img;
    }

    public String getMessageFrom() {
        return messageFrom;
    }

    public void setMessageFrom(String messageFrom) {
        this.messageFrom = messageFrom;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }

}
