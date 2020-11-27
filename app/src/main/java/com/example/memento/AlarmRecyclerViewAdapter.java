package com.example.memento;

import android.view.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.*;

import java.util.ArrayList;
import java.util.List;
/*
OBSOLÈTE
Le RecyclerViewAdapter est initialisé directement dans AlarmListFragment
 */
public class AlarmRecyclerViewAdapter extends RecyclerView.Adapter<AlarmViewHolder> {
    private List<Alarm> alarms;
    private OnCheckAlarmListener listener;

    public AlarmRecyclerViewAdapter(OnCheckAlarmListener listener) {
        this.alarms = new ArrayList<Alarm>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        Alarm alarm = alarms.get(position);
        holder.bind(alarm, listener);
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    @Override
    public void onViewRecycled(@NonNull AlarmViewHolder holder) {
        super.onViewRecycled(holder);
        holder.alarmStarted.setOnCheckedChangeListener(null);
    }

    public void setAlarms(List<Alarm> alarms) {
        this.alarms = alarms;
        notifyDataSetChanged();
    }
}

