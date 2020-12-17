package com.example.memento;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

import java.util.Date;

import butterknife.OnClick;

public class MessageListFragment extends AppCompatActivity implements MessageAdapter.Listener {


    private TextView textViewRecyclerViewEmpty;
    private TextInputEditText editTextMessage;
    private Button sendButton;
    private RecyclerView recyclerView;


    private MessageAdapter messageAdapter;
    private String currentChatName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        String chat=getIntent().getStringExtra("chat");   //on récupère l'identifiant de la conversation à afficher, fournis par ConversationViewHolder

        this.recyclerView=findViewById(R.id.activity_mentor_chat_recycler_view);
        this.textViewRecyclerViewEmpty=findViewById(R.id.activity_mentor_chat_text_view_recycler_view_empty);
        this.editTextMessage=findViewById(R.id.activity_mentor_chat_message_edit_text);
        this.sendButton=findViewById(R.id.activity_mentor_chat_send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSendMessage();
            }  //on ajoute le click pour l'envoid de  messages
        });
        this.configureRecyclerView(chat);

    }


    String getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    } //pour récupérer l'utilisateur actuellement connecté


    public void onClickSendMessage() {
        if (!TextUtils.isEmpty(editTextMessage.getText()) ){  //on vérifie que l'input n'est pas vide

            MessageDatabase database=new MessageDatabase();
            database.createMessageForChat(editTextMessage.getText().toString(), this.currentChatName, this.getCurrentUser()).addOnFailureListener(this.onFailureListener()); //on ajoute le message dans Firestore

            DocumentReference currentConv=ConversationDatabase.getConversation(this.currentChatName);
            currentConv.update("lastMessage",editTextMessage.getText().toString());  //on met à jour la conversation pour garder l'affichage dans la list de conversations à jour
            Date now=new Date();
            currentConv.update("lastUpdate", FieldValue.serverTimestamp());

            this.editTextMessage.setText("");  //on remet l'input à vide
        }
    }


    private void configureRecyclerView(String chatName){  //met en place la RecyclerView
        this.currentChatName = chatName;
        MessageDatabase database=new MessageDatabase();

        FirestoreRecyclerOptions<Message> options= new FirestoreRecyclerOptions.Builder<Message>() //la requête pour récupérer les messages, qui sera utilisé par l'adapter
                .setQuery(database.getAllMessageForChat(chatName), Message.class)
                .setLifecycleOwner(this)
                .build();

        this.messageAdapter = new MessageAdapter(options, this, this.getCurrentUser()); //création de l'adapter

        messageAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount()); //ramène en bas de la view à l'arivée de nouveau message
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.messageAdapter);
    }

    @Override
    public void onDataChanged() { //afficher un message si la conversation est vide
        textViewRecyclerViewEmpty.setVisibility(this.messageAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               // Toast.makeText(this, getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
            }
        };
    }
}