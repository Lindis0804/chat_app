package com.ldh.chatapp.models;

import java.util.Date;

public class ChatMessage {
    public String senderId, receiverId, message, dateTime;
    public Date dateObject;
    public String conversionId, conversionName, conversionImage;

    public ChatMessage(String senderId, String receiverId, String message, String dateTime, Date date) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.dateTime = dateTime;
        this.dateObject = date;
    }

    public ChatMessage() {
    }
}
