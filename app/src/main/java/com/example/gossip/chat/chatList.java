package com.example.gossip.chat;

public class chatList {
    private boolean isImage;
    private String mobile,name,message,time,date,ChatId,MessageId;

    public chatList(String mobile, String name, String message, String time, String date,String ChatId,String MessageId,boolean isImage) {
        this.mobile = mobile;
        this.name = name;
        this.message = message;
        this.time = time;
        this.date = date;
        this.ChatId=ChatId;
        this.MessageId=MessageId;
        this.isImage=isImage;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public String getChatId() {
        return ChatId;
    }

    public void setChatId(String chatId) {
        ChatId = chatId;
    }

    public String getMessageId() {
        return MessageId;
    }

    public void setMessageId(String messageId) {
        MessageId = messageId;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }
}
