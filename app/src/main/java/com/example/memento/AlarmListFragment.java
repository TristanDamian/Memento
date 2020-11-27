package com.example.memento;

import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.*;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.IOException;

public class AlarmListFragment  extends Fragment implements OnCheckAlarmListener{  //Fragment pour afficher la liste des alarmes
    private FirestoreRecyclerAdapter alarmRecyclerViewAdapter;           //RecyclerViewAdapter pour Firestore
    private RecyclerView alarmsRecyclerView;
    private Button addAlarm;
    public OnCheckAlarmListener listener;                 //listener pour les click sur les éléments de la liste
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener=this;
       // alarmRecyclerViewAdapter = new AlarmRecyclerViewAdapter(this);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Query query = FirebaseFirestore.getInstance()           //la requête pour récupérer les alarmes
                .collection("Alarms").limit(50);
        FirestoreRecyclerOptions<Alarm> options = new FirestoreRecyclerOptions.Builder<Alarm>()
                .setQuery(query, Alarm.class)
                .build();
        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<Alarm, AlarmViewHolder>(options) {         //initialisation du FirestoreRecyclerAdapter
            @Override
            public void onBindViewHolder(AlarmViewHolder holder, int position, Alarm model) {      // lie chaque alarme avec un viewHolder
                model.setAlarmId( Integer.parseInt(getSnapshots().getSnapshot(position).getReference().getId()));
                holder.bind(model,listener);
            }

            @Override
            public AlarmViewHolder onCreateViewHolder(ViewGroup group, int i) {         // créé les viewHolder

                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.item_alarm, group, false);
                return new AlarmViewHolder(view);
            }

        };

        View view = inflater.inflate(R.layout.fragment_listalarms, container, false);
        alarmRecyclerViewAdapter=adapter;
        alarmsRecyclerView = view.findViewById(R.id.fragment_listalarms_recylerView);
        alarmsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        alarmsRecyclerView.setAdapter(alarmRecyclerViewAdapter);    //fin de l'initialisation de la RecyclerView

        addAlarm = view.findViewById(R.id.fragment_listalarms_addAlarm);    //navigation vers l'écran d'ajout d'alarme quand on click sur 'addAlarm'
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_alarmsListFragment_to_createAlarmFragment);
            }
        });

        return view;
    }

    public void onCheck(Alarm alarm) {    //est appellée quand on valide un rappel
        if (alarm.isStarted()) {
            alarm.cancelAlarm(getContext());
            try {
                AlarmDatabase manager= new AlarmDatabase();
                manager.done(alarm);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            alarm.schedule(getContext());
            try {
                AlarmDatabase manager= new AlarmDatabase();
                manager.done(alarm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onDelete(Alarm alarm) { //est appellée pour supprimer un rappel
            try {
                AlarmDatabase manager= new AlarmDatabase();
                manager.delete(alarm);
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    @Override
    public void onStart() {  //permet de rafraîchir la vue après modification de la base
        super.onStart();
        alarmRecyclerViewAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        alarmRecyclerViewAdapter.stopListening();
    }
}
