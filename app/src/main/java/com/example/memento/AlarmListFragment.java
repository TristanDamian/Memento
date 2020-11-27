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

public class AlarmListFragment  extends Fragment implements OnToggleAlarmListener{
    private FirestoreRecyclerAdapter alarmRecyclerViewAdapter;
    private RecyclerView alarmsRecyclerView;
    private Button addAlarm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // alarmRecyclerViewAdapter = new AlarmRecyclerViewAdapter(this);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Query query = FirebaseFirestore.getInstance()
                .collection("Alarms").limit(50);
        FirestoreRecyclerOptions<Alarm> options = new FirestoreRecyclerOptions.Builder<Alarm>()
                .setQuery(query, Alarm.class)
                .build();
        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<Alarm, AlarmViewHolder>(options) {
            @Override
            public void onBindViewHolder(AlarmViewHolder holder, int position, Alarm model) {

            }

            @Override
            public AlarmViewHolder onCreateViewHolder(ViewGroup group, int i) {
                // Using a custom layout called R.layout.message for each item, we create a new instance of the viewholder
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.item_alarm, group, false);

                return new AlarmViewHolder(view);
            }
        };
//Final step, where "mRecyclerView" is defined in your xml layout as
//the recyclerview
        View view = inflater.inflate(R.layout.fragment_listalarms, container, false);
        alarmRecyclerViewAdapter=adapter;
        alarmsRecyclerView = view.findViewById(R.id.fragment_listalarms_recylerView);
        alarmsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        alarmsRecyclerView.setAdapter(alarmRecyclerViewAdapter);

        addAlarm = view.findViewById(R.id.fragment_listalarms_addAlarm);
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_alarmsListFragment_to_createAlarmFragment);
            }
        });

        return view;
    }

    public void onToggle(Alarm alarm) {
        if (alarm.isStarted()) {
            alarm.cancelAlarm(getContext());
           // AlarmDatabase.update(alarm);
        } else {
            alarm.schedule(getContext());
            //AlarmDatabase.update(alarm);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        alarmRecyclerViewAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        alarmRecyclerViewAdapter.stopListening();
    }
}
