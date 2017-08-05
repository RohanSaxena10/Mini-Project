package com.example.rohan.miniproject.Chat;

import java.util.Date;

/**
 * Created by Rohan on 01/04/17.
*/
public class ChatMessage {

    private String messageText;
    private String messageUser;
    private long messageTime;
    private String seen;

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public ChatMessage(){
        messageTime = new Date().getTime();
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}