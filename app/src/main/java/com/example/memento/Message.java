package com.example.memento;



import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Message {
    private String message;
    private Date date;
    private String sender;


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
