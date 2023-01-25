package com.example.gossip.chat;

public class chatList {
    private String mobile,name,message,time,date;

    public chatList(String mobile, String name, String message, String time, String date) {
        this.mobile = mobile;
        this.name = name;
        this.message = message;
        this.time = time;
        this.date = date;
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
