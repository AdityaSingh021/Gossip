package com.example.gossip.messages;

import android.graphics.Bitmap;

public class MessagesList {
    private String name, mobile,lastMessage,chatKey;
    private Long LastNode;
    private String profilePic;
    private int unseenMessages;

    public MessagesList(String name, String mobile, String textMessage, String profilePic , int unseenMessages, String chatKey, Long LastNode) {
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setName(String name) {
        this.name = name;
    }
}

//https://firebasestorage.googleapis.com/v0/b/goss-p-dc95b.appspot.com/o/ProfilePictures%2F9818514406?alt=media&token=78d108c2-5fab-48c6-906c-8f8957fa4d21