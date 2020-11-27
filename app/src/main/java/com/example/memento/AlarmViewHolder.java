package com.example.memento;

import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.*;

public class AlarmViewHolder extends RecyclerView.ViewHolder {     //ViewHolder pour les alarmes de la liste
    private TextView alarmTime;
    private ImageView alarmRecurring;
    private TextView alarmRecurringDays;
    private TextView alarmTitle;
    private Button alarmDelete;
    CheckBox alarmStarted;

    public AlarmViewHolder(@NonNull View itemView) {
        super(itemView);

        alarmTime = itemView.findViewById(R.id.item_alarm_time);
        alarmStarted = itemView.findViewById(R.id.item_alarm_started);
        alarmRecurring = itemView.findViewById(R.id.item_alarm_recurring);
        alarmRecurringDays = itemView.findViewById(R.id.item_alarm_recurringDays);
        alarmTitle = itemView.findViewById(R.id.item_alarm_title);
        alarmDelete= itemView.findViewById(R.id.item_alarm_delete);
    }

    public void bind(final Alarm alarm, final OnCheckAlarmListener listener) {  //associe l'alarm et le ViewHolder
        String alarmText = String.format("%02d:%02d", alarm.getHour(), alarm.getMinute());

        alarmTime.setText(alarmText);
        alarmStarted.setChecked(alarm.isStarted());

        if (alarm.isRecurring()) {
            alarmRecurring.setImageResource(R.drawable.baseline_repeat_black_18dp);
            alarmRecurringDays.setText(alarm.getAlarmDays());
        } else {
            alarmRecurring.setImageResource(R.drawable.baseline_looks_one_black_18dp);
            alarmRecurringDays.setText("Once Off");
        }

        if (alarm.getTitle().length() != 0) {
            alarmTitle.setText(alarm.getTitle());
        } else {
            alarmTitle.setText("My alarm");
        }

        alarmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDelete(alarm);
            }
        });   //associe le bouton à la fonction onDelete, pour supprimer l'alarm associée de la base de données

        alarmStarted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.onCheck(alarm);
            }
        });
    }
}

