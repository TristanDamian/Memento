package com.example.memento;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class Conversation {
    private String lastMessage;
    private Date lastUpdate;
    private Array Users;

    public Conversation() {
    }

    public Conversation(String lastMessage, Date lastUpdate) {
        this.lastMessage = lastMessage;
        this.lastUpdate = lastUpdate;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }


}
