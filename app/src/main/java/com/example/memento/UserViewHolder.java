package com.example.memento;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class UserViewHolder extends RecyclerView.ViewHolder {     //ViewHolder pour les alarmes de la liste
    private TextView userFullName;
    private TextView userAge;
    private TextView userRelax;
    private TextView userSport;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);

        userFullName= itemView.findViewById(R.id.item_user_nom);
        userAge = itemView.findViewById(R.id.item_user_age);
        userRelax = itemView.findViewById(R.id.item_user_relax);
        userSport= itemView.findViewById(R.id.item_user_sport);
    }


    public void bind(final User user) {  //associe l'User et le ViewHolder
        String UserName = user.getfullname();
        String UserAge= user.getAge();
        userFullName.setText(UserName);
        userAge.setText((CharSequence) UserAge);
        if(!user.getRelax()){
            userRelax.setVisibility(View.INVISIBLE);
        }
        if(!user.getSport()){
            userSport.setVisibility(View.INVISIBLE);
        }
    }
}
