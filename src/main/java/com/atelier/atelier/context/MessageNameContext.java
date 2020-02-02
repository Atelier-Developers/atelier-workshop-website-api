package com.atelier.atelier.context;

import java.util.Calendar;

public class MessageNameContext {

    private long messageId;
    private String message;
    private Calendar time;
    private String senderName;


    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
