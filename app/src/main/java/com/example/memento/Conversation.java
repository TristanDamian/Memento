package com.example.memento;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class Conversation { //permet de récupérer les informations de base sur une conversation
    private String lastMessage;  //le texte du dernier message envoyé, pour l'affihage
    private Date lastUpdate;   //la date du dernier message, pour l'affichage


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
