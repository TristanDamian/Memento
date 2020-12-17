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

public class UserListFragment extends Fragment {
    private FirestoreRecyclerAdapter UserRecyclerViewAdapter;           //RecyclerViewAdapter pour Firestore
    private RecyclerView UsersRecyclerView;
    private Button addUser;
    private Button chat;
    private String UID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UID= FirebaseAuth.getInstance().getCurrentUser().getUid();

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Query query = FirebaseFirestore.getInstance()           //la requête pour récupérer les Users
                .collection("UserInfo").whereNotEqualTo("uid",UID).limit(50);
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();
        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<User, UserViewHolder>(options) {         //initialisation du FirestoreRecyclerAdapter
            @Override
            public void onBindViewHolder(UserViewHolder holder, int position, User model) {      // lie chaque User avec un viewHolder

                String currentUser = getSnapshots().getSnapshot(position).getId();
                holder.bind(model);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent userIntent = new Intent(getContext(), ProfileActivity.class);
                        userIntent.putExtra("user",model.getuid());
                        userIntent.putExtra("nom", model.fullname);
                        userIntent.putExtra("age", model.age);
                        userIntent.putExtra("email", model.email);
                        startActivity(userIntent);
                    }
                });
            }
            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup group, int i) {         // créé les viewHolder

                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.item_user, group, false);
                return new UserViewHolder(view);
            }

        };

        View view = inflater.inflate(R.layout.fragment_listuser, container, false);
        UserRecyclerViewAdapter=adapter;
        UsersRecyclerView = view.findViewById(R.id.fragment_listuser_recylerView);
        UsersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        UsersRecyclerView.setAdapter(UserRecyclerViewAdapter);    //fin de l'initialisation de la RecyclerView


        return view;
    }


    @Override
    public void onStart() {  //permet de rafraîchir la vue après modification de la base
        super.onStart();
        UserRecyclerViewAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        UserRecyclerViewAdapter.stopListening();
    }
}
