package com.example.memento;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConversationDatabase {

    private static final String COLLECTION_NAME = "Conversation";

    public static CollectionReference getAllConversation(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static DocumentReference getConversation(String ID){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME).document(ID);
    }


}
