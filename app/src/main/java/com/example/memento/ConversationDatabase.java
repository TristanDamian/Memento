package com.example.memento;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;

public class ConversationDatabase {

    private static final String COLLECTION_NAME = "Conversation";

    public static CollectionReference getAllConversation(){  //récupérer toutes les conversations de la base
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static DocumentReference getConversation(String ID){   //récupérer une conversations à partir de son ID
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME).document(ID);
    }

    public static Task<QuerySnapshot> getUserInfo(String ID){  //récupérer les infos d'un utilisateur à partir de son ID de connexion
        return FirebaseFirestore.getInstance().collection("UserInfo").whereEqualTo("uid",ID).get();
    }

    public static Task<QuerySnapshot> getConversation1(String User1){  //récupérer les conversations d'un utilisateur selon son ID
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME).whereArrayContains("Users", User1).get();
    }

}
