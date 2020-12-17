package com.example.memento;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;

public class ConversationDatabase {

    private static final String COLLECTION_NAME = "Conversation";

    public static CollectionReference getAllConversation(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static DocumentReference getConversation(String ID){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME).document(ID);
    }

    public static Task<QuerySnapshot> getUserInfo(String ID){
        return FirebaseFirestore.getInstance().collection("UserInfo").whereEqualTo("uid",ID).get();
    }

    public static Task<QuerySnapshot> getConversation(String User1, String User2){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME).whereArrayContains("Users", Arrays.asList(User1,User2)).get();
    }
    public static Task<QuerySnapshot> getConversation1(String User1){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME).whereArrayContains("Users", User1).get();
    }

}
