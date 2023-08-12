package com.example.gossip.Status;

public class StatusModel {
    private String imageUrl;
    private String mobile;
    private String name;
    private String chatId;

    private boolean state;

    public StatusModel(String imageUrl, String name,String mobile,String chatId,boolean state) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.mobile=mobile;
        this.chatId=chatId;
        this.state=state;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getMobile() {
        return mobile;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }
}
