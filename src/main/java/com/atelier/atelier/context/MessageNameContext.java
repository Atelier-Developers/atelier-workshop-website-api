package com.atelier.atelier.context;

import java.util.Calendar;

public class MessageNameContext {

    private long participantId;
    private String content;
    private MessageTimestampContext timestamp;


    public long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(long participantId) {
        this.participantId = participantId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageTimestampContext getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(MessageTimestampContext timestamp) {
        this.timestamp = timestamp;
    }
}
