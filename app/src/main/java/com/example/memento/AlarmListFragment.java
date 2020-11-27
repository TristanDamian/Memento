package com.example.memento;

import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.*;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AlarmListFragment  extends Fragment implements OnToggleAlarmListener{
    private FirestoreRecyclerAdapter alarmRecyclerViewAdapter;
    private RecyclerView alarmsRecyclerView;
    private Button addAlarm;
    private Button delAlarm;

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
                holder.bind(model);
            }

            @Override
            public AlarmViewHolder onCreateViewHolder(ViewGroup group, int i) {

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

        delAlarm = view.findViewById(R.id.fragment_listalarms_delAlarm);

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

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            Alarm alarmTemp = (Alarm)alarmRecyclerViewAdapter.getItem(viewHolder.getAdapterPosition());
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            com.google.firebase.database.Query alarmQuery = ref.orderByChild("title").equalTo(alarmTemp.getTitle());
            alarmQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot alarmSnapshot : snapshot.getChildren()) {
                        alarmSnapshot.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                };
            });
        }
    };
    }

