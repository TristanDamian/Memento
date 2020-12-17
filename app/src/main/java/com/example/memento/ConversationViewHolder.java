package com.example.memento;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class ConversationViewHolder extends RecyclerView.ViewHolder {     //ViewHolder pour les alarmes de la liste
    private TextView convLastMessage;
    private  TextView convLastUpdate;
    private TextView convUser;
    //private CardView convCard;
    public ConversationViewHolder(@NonNull View itemView) {
        super(itemView);

       convLastMessage = itemView.findViewById(R.id.item_conv_last_message);
       convLastUpdate = itemView.findViewById(R.id.item_conv_last_update);
       convUser=itemView.findViewById(R.id.item_conv_user);
        //convCard=itemView.findViewById(R.id.item_conv_card);

    }


    public void bind(final Conversation conv, String convID) {  //associe l'Conversation et le ViewHolder
        SetUserName(convID);
        String ConversationText = conv.getLastMessage();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm");
        String dateText = dateFormat.format(conv.getLastUpdate());
        convLastUpdate.setText(dateText);
        convLastMessage.setText(ConversationText);
    }

    public void SetUserName(String convID){
        DocumentReference currentConversation=ConversationDatabase.getConversation(convID);
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        currentConversation.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> participants = (List<String>)document.get("Users");
                        for(int i=0; i<participants.size();i++){
                            String user=participants.get(i);
                            if(user!=currentUser){
                                Task<QuerySnapshot> snap=ConversationDatabase.getUserInfo(user).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if(queryDocumentSnapshots.getDocuments().size()>0){  //il peut arriver qu'on ait pas de UserInfo dans la database, même si le user existe
                                            String userName= (String) queryDocumentSnapshots.getDocuments().get(0).get("fullname");
                                            convUser.setText(userName);
                                        }
                                    }
                                });
                            }
                        }
                    } else {
                        Log.d("FireStore", "No such document");
                    }
                } else {
                    Log.d("Firestore", "get failed with ", task.getException());
                }
            }
        });
    }

}
