package com.dev.shehzadi.startupguidance.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by shehzadi on 18/3/18.
 */

public class ChatModel implements Serializable {

    private String chatId;
    private String chatMessage;
    private String fromUid;
    private String toUid;
    private String chatTime;

    public ChatModel() {
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getFromUid() {
        return fromUid;
    }

    public void setFromUid(String fromUid) {
        this.fromUid = fromUid;
    }

    public String getToUid() {
        return toUid;
    }

    public void setToUid(String toUid) {
        this.toUid = toUid;
    }

    public String getChatTime() {
        return chatTime;
    }

    public void setChatTime(String chatTime) {
        this.chatTime = chatTime;
    }
}
