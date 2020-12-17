package com.example.memento;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.navigation.ui.NavigationUI.setupWithNavController;
import static com.example.memento.AlarmDatabase.Database;

public class ProfileActivity extends AppCompatActivity {
    String convToGo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        TextView textName=findViewById(R.id.profile_name);
        TextView textAge=findViewById(R.id.profile_age);
        TextView textEmail=findViewById(R.id.profile_email);
        Button newConv=findViewById(R.id.profile_newconv);

        String fullname=getIntent().getStringExtra("nom");
        String age=getIntent().getStringExtra("age");
        String userID=getIntent().getStringExtra("user");
        String email=getIntent().getStringExtra("email");
        String currentUser= FirebaseAuth.getInstance().getUid();

        textName.setText(fullname);
        textEmail.setText(email);
        textAge.setText(age);

        newConv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addConv(currentUser,userID);
            }
        });
        //addConv(userID,currentUser);
        Task<QuerySnapshot> checkConv=ConversationDatabase.getConversation1(currentUser).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() { //on vérifie les conversations existantes entre les utilisateurs
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    boolean test=task.getResult().isEmpty();
                    List<DocumentSnapshot> snap=task.getResult().getDocuments();
                    for(int i=0; i<snap.size();i++){
                        DocumentSnapshot currentDoc=snap.get(i);
                        List<String> IDs=(List<String>)currentDoc.get("Users");
                        for(int j=0;j<IDs.size();j++){
                            String userToCheck=IDs.get(j);
                            if(userToCheck.equals(userID)){
                                //si il existe déjà une conversation entre les deux utilisateurs, on enlève la possibilité de créer une conversation
                                //newConv.setEnabled(false);
                                newConv.setText("Vous êtes en contact \n accédez à la conversation");
                                newConv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        checkConv(currentDoc.getId());
                                    }
                                });
                            }
                        }
                    }
                }
                else{
                    Log.d("Query Conversation", "Error getting documents: ", task.getException());
                }
            }
        });



    }

    private void addConv(String User1, String User2){
        CollectionReference Conversations = FirebaseFirestore.getInstance().collection("Conversation");
        Map<String, Object> newConversation = new HashMap<>();
        newConversation.put("lastMessage","Empty Conversation");
        newConversation.put("lastUpdate", new Timestamp(new Date()));
        newConversation.put("Users", Arrays.asList(User1, User2));
        Conversations.add(newConversation).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Map<String, Object>initiate= new HashMap<>();
                initiate.put("first", "test");
                documentReference.collection("messages").add(initiate);
                String convID=documentReference.getId();
                checkConv(convID);
            }
        });
    }

    private void checkConv(String convID){
        Intent chatIntent = new Intent(this, MessageListFragment.class);
        chatIntent.putExtra("chat", convID);
        startActivity(chatIntent);
    }

}
