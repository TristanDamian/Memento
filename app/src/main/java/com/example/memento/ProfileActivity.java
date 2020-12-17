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

public class ProfileActivity extends AppCompatActivity {  //affiche les détails d'un utilisateur et propose de lancer une conversation avec lui
    String convToGo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView textName=findViewById(R.id.profile_name);
        TextView textAge=findViewById(R.id.profile_age);
        TextView textEmail=findViewById(R.id.profile_email);
        Button newConv=findViewById(R.id.profile_newconv);

        String fullname=getIntent().getStringExtra("nom");  //on récupère les valeurs de l'intent envoyée par UserListFragment
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
            }  //on associe la fonction de création de conversation avec le bouton
        });
        Task<QuerySnapshot> checkConv=ConversationDatabase.getConversation1(currentUser).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() { //on vérifie les conversations existantes de notre utilisateur
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    boolean test=task.getResult().isEmpty();
                    List<DocumentSnapshot> snap=task.getResult().getDocuments();

                    for(int i=0; i<snap.size();i++){ // on parcourt la liste de toutes les conversations de l'utilisateur
                        DocumentSnapshot currentDoc=snap.get(i);
                        List<String> IDs=(List<String>)currentDoc.get("Users");

                        for(int j=0;j<IDs.size();j++){ //la liste des participants à ces conversations
                            String userToCheck=IDs.get(j);
                            if(userToCheck.equals(userID)){
                                //si il existe déjà une conversation entre les deux utilisateurs, on enlève la possibilité de créer une conversation
                                newConv.setText("Vous êtes en contact \n accédez à la conversation"); //on change le texte du bouton
                                newConv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        checkConv(currentDoc.getId()); //à la place, le bouton envoie simplement vers la conversation existante
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

        CollectionReference Conversations = FirebaseFirestore.getInstance().collection("Conversation"); //on récupère la collection de conversations
        Map<String, Object> newConversation = new HashMap<>();    //on créé la hashMap pour la création de la conversation
        newConversation.put("lastMessage","Empty Conversation");  //on entre les valeurs
        newConversation.put("lastUpdate", new Timestamp(new Date()));
        newConversation.put("Users", Arrays.asList(User1, User2));
        Conversations.add(newConversation).addOnSuccessListener(new OnSuccessListener<DocumentReference>() { //on ajoute la nouvelle conversation
            @Override
            public void onSuccess(DocumentReference documentReference) {  //quand  l'ajout a réussi on créé la collection pour la réception des messages
                Map<String, Object>initiate= new HashMap<>();
                initiate.put("first", "init");   //on ne peut pas ajouter une collection vide: cette objet n'a pas le format des messages, il ne sera pas récupéré pour l'affichage de la conversation
                documentReference.collection("messages").add(initiate);  //on ajoute la collection messages
                String convID=documentReference.getId();
                checkConv(convID);   //on lance l'affichage de la nouvelle conversation
            }
        });
    }

    private void checkConv(String convID){  //on lance l'affichage de la conversation avec un intent vers MessageListFragment
        Intent chatIntent = new Intent(this, MessageListFragment.class);
        chatIntent.putExtra("chat", convID);
        startActivity(chatIntent);
    }

}
