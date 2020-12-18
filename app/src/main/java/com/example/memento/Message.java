package com.example.memento;



import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Message {   //classe pour gérer les messages des conversation
    private String message;  //texte du message
    private Date date;     //date d'envoi
    private String sender;  //identfifant de l'émetteur


    public Message() { }

    public Message(String message, String StringSender) {
        this.message = message;
        this.sender = StringSender;
    }


    // --- GETTERS ---
    public String getMessage() { return message; }
    @ServerTimestamp public Date getDate() { return date; }
    public String getSender() { return sender; }

    // --- SETTERS ---
    public void setMessage(String message) { this.message = message; }
    public void setDateCreated(Date dateCreated) { this.date = dateCreated; }
    public void setStringSender(String StringSender) { this.sender = StringSender; }

}
