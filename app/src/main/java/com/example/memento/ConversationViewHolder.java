package com.example.memento;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ConversationViewHolder extends RecyclerView.ViewHolder {     //ViewHolder pour les alarmes de la liste
    private TextView convLastMessage;
    private  TextView convLastUpdate;
    //private CardView convCard;
    public ConversationViewHolder(@NonNull View itemView) {
        super(itemView);

       convLastMessage = itemView.findViewById(R.id.item_conv_last_message);
       convLastUpdate = itemView.findViewById(R.id.item_conv_last_update);
        //convCard=itemView.findViewById(R.id.item_conv_card);

    }


    public void bind(final Conversation conv) {  //associe l'Conversation et le ViewHolder
        String ConversationText = conv.getLastMessage();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm");
        String dateText = dateFormat.format(conv.getLastUpdate());
        convLastUpdate.setText(dateText);
        convLastMessage.setText(ConversationText);


    }
}
