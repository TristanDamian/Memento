package com.example.memento;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ConvListFragment extends Fragment {
    private FirestoreRecyclerAdapter ConversationRecyclerViewAdapter;           //RecyclerViewAdapter pour Firestore
    private RecyclerView ConversationsRecyclerView;
    private Button addConversation;
    private Button chat;
    private String UID;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UID=FirebaseAuth.getInstance().getCurrentUser().getUid();

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Query query = FirebaseFirestore.getInstance()
                .collection("Conversation").whereArrayContains("Users",UID).limit(50); //la requête pour récupérer les Conversations de notre utilisateur
        FirestoreRecyclerOptions<Conversation> options = new FirestoreRecyclerOptions.Builder<Conversation>()
                .setQuery(query, Conversation.class)
                .build();

        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<Conversation, ConversationViewHolder>(options) {         //initialisation du FirestoreRecyclerAdapter
            @Override
            public void onBindViewHolder(ConversationViewHolder holder, int position, Conversation model) {      // lie chaque Conversatione avec un viewHolder

                String convID = getSnapshots().getSnapshot(position).getId();
                holder.bind(model,convID);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent chatIntent = new Intent(getContext(), MessageListFragment.class);  //lance l'activité de la conversation quand on clique sur la conversation
                        chatIntent.putExtra("chat", convID);
                        startActivity(chatIntent);
                    }
                });
            }
            @Override
            public ConversationViewHolder onCreateViewHolder(ViewGroup group, int i) {         // créé les viewHolder
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.item_conv, group, false);
                return new ConversationViewHolder(view);
            }

        };

        View view = inflater.inflate(R.layout.fragment_listconv, container, false);
        ConversationRecyclerViewAdapter=adapter;
        ConversationsRecyclerView = view.findViewById(R.id.fragment_listconv_recylerView);
        ConversationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ConversationsRecyclerView.setAdapter(ConversationRecyclerViewAdapter);    //fin de l'initialisation de la RecyclerView
        return view;
    }


    @Override
    public void onStart() {  //permet de rafraîchir la vue après modification de la base
        super.onStart();
        ConversationRecyclerViewAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        ConversationRecyclerViewAdapter.stopListening();
    }
}
