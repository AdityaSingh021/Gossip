package com.example.gossip.messages;

public class MessagesList {
    private String name, mobile,lastMessage,chatKey;
    private int unseenMessages;

    public MessagesList(String name, String mobile, String textMessage, int unseenMessages,String chatKey) {
        this.name = name;
        this.mobile = mobile;
        this.lastMessage = textMessage;
        this.chatKey=chatKey;
        this.unseenMessages = unseenMessages;
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

    public String getLastMessage() {
        return lastMessage;
    }

    public String getChatKey() {
        return chatKey;
    }
}
