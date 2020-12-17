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

    // FOR DESIGN
    // 1 - Getting all views needed
    //@BindView(R.id.activity_mentor_chat_recycler_view) RecyclerView recyclerView;
    //@BindView(R.id.activity_mentor_chat_text_view_recycler_view_empty)
    private TextView textViewRecyclerViewEmpty;
    //@BindView(R.id.activity_mentor_chat_message_edit_text)
    private TextInputEditText editTextMessage;
    private Button sendButton;
    private RecyclerView recyclerView;

    // FOR DATA
    // 2 - Declaring Adapter and data
    private MessageAdapter messageAdapter;
    //@Nullable private UserInfo modelCurrentUser;
    private String currentChatName;

    // STATIC DATA FOR CHAT (3)
    private static final String CHAT_NAME_ANDROID = "android";
    private static final String CHAT_NAME_BUG = "bug";
    private static final String CHAT_NAME_FIREBASE = "firebase";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        String chat=getIntent().getStringExtra("chat");
        this.recyclerView=findViewById(R.id.activity_mentor_chat_recycler_view);
        this.textViewRecyclerViewEmpty=findViewById(R.id.activity_mentor_chat_text_view_recycler_view_empty);
        this.editTextMessage=findViewById(R.id.activity_mentor_chat_message_edit_text);
        this.sendButton=findViewById(R.id.activity_mentor_chat_send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSendMessage();
            }
        });
        this.configureRecyclerView(chat);

    }


    String getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
     public int getFragmentLayout() { return R.layout.chat_layout; }

    // --------------------
    // ACTIONS
    // --------------------

    //@OnClick(R.id.activity_mentor_chat_send_button)
    public void onClickSendMessage() {
        // 1 - Check if text field is not empty and current user properly downloaded from Firestore
        if (!TextUtils.isEmpty(editTextMessage.getText()) ){
            // 2 - Create a new Message to Firestore
            MessageDatabase database=new MessageDatabase();
            database.createMessageForChat(editTextMessage.getText().toString(), this.currentChatName, this.getCurrentUser()).addOnFailureListener(this.onFailureListener());
            DocumentReference currentConv=ConversationDatabase.getConversation(this.currentChatName);
            currentConv.update("lastMessage",editTextMessage.getText().toString());
            Date now=new Date();
            currentConv.update("lastUpdate", FieldValue.serverTimestamp());
            // 3 - Reset text field
            this.editTextMessage.setText("");
        }
    }


    // --------------------
    // UI
    // --------------------
    // 5 - Configure RecyclerView with a Query
    private void configureRecyclerView(String chatName){
        //Track current chat name
        this.currentChatName = chatName;
        MessageDatabase database=new MessageDatabase();
        FirestoreRecyclerOptions<Message> options= new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(database.getAllMessageForChat(chatName), Message.class)
                .setLifecycleOwner(this)
                .build();
        //Configure Adapter & RecyclerView
        this.messageAdapter = new MessageAdapter(options, this, this.getCurrentUser());
        messageAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount()); // Scroll to bottom on new messages
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.messageAdapter);
    }

    /* 6 - Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Message> generateOptionsForAdapter(Query query){
        return
    }*/

    // --------------------
    // CALLBACK
    // --------------------

    @Override
    public void onDataChanged() {
        // 7 - Show TextView in case RecyclerView is empty
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