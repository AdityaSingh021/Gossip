package com.example.gossip.messages;

import android.graphics.Bitmap;

public class MessagesList {
    private String name, mobile,lastMessage,chatKey;
    private Long LastNode;
    private Bitmap profilePic;
    private int unseenMessages;

    public MessagesList(String name, String mobile, String textMessage, Bitmap profilePic , int unseenMessages, String chatKey, Long LastNode) {
        this.name = name;
        this.mobile = mobile;
        this.lastMessage = textMessage;
        this.chatKey=chatKey;
        this.profilePic=profilePic;
        this.unseenMessages = unseenMessages;
        this.LastNode=LastNode;
    }

    public Long getLastNode() {
        return LastNode;
    }

    public void setLastNode(Long lastNode) {
        LastNode = lastNode;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getlastMessage() {
        return lastMessage;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }
    public void setUnseenMessages(){
        this.unseenMessages=0;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getChatKey() {
        return chatKey;
    }

    public Bitmap getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic = profilePic;
    }

    public void setName(String name) {
        this.name = name;
    }
}
