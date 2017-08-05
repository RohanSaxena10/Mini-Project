package com.example.rohan.miniproject.Chat;

/**
 * Created by Rohan on 01/04/17.
 */
public class ChatPreviewPOJO {

    private String UserID;

    public ChatPreviewPOJO() {
    }

    private String DisplayName;
    private ChatMessage LastMessage;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public ChatMessage getLastMessage() {
        return LastMessage;
    }

    public void setLastMessage(ChatMessage lastMessage) {
        LastMessage = lastMessage;
    }
}
