package com.example.memento;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import java.util.Date;

public class MessageDatabase {
    private static final String COLLECTION_NAME = "messages";

    // --- GET ---

    public static Task<DocumentReference> createMessageForChat(String textMessage, String chat, String userSender){

        // 1 - On créé l'objet message
        Message message = new Message(textMessage, userSender);
        Date now=new Date();
        message.setDateCreated(now);
        // 2 - On le stock dans Firestore
        return ConversationDatabase.getAllConversation()
                .document(chat)
                .collection(COLLECTION_NAME)
                .add(message);
    }

    public static Query getAllMessageForChat(String conv){
        return ConversationDatabase.getAllConversation()
                .document(conv)
                .collection(COLLECTION_NAME)
                .orderBy("date")
                .limit(50);
    }
}
