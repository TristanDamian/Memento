package com.example.memento;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import java.util.Date;

public class MessageDatabase {
    private static final String COLLECTION_NAME = "messages";

    public static Task<DocumentReference> createMessageForChat(String textMessage, String chat, String userSender){
        Message message = new Message(textMessage, userSender); //  On créé l'objet message
        Date now=new Date();
        message.setDateCreated(now);
        return ConversationDatabase.getAllConversation()   // On le stock dans Firestore
                .document(chat)
                .collection(COLLECTION_NAME)
                .add(message);
    }

    public static Query getAllMessageForChat(String conv){  //on récupère tous les messages d'une conversation
        return ConversationDatabase.getAllConversation()
                .document(conv)
                .collection(COLLECTION_NAME)
                .orderBy("date")
                .limit(50);
    }
}
