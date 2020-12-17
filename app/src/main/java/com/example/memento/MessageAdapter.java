package com.example.memento;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MessageAdapter extends FirestoreRecyclerAdapter<Message, MessageViewHolder> {  //adapter pour les messages

    public interface Listener {
        void onDataChanged();
    }


    private final String idCurrentUser;

    private Listener callback;

    public MessageAdapter(@NonNull FirestoreRecyclerOptions<Message> options, Listener callback, String idCurrentUser) {
        super(options);
        this.callback = callback;
        this.idCurrentUser = idCurrentUser;
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Message model) {
        holder.updateWithMessage(model, this.idCurrentUser);  //on appelle la fonction qui bind le viewHolder et gère l'affichage des messages
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false));
    }

    @Override
    public void onDataChanged() {  //on raffraichit le message quand de nouvelles données arrivent
        super.onDataChanged();
        this.callback.onDataChanged();
    }
}
