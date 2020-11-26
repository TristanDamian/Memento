package com.example.memento;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Random;


public class NewTaskFragment extends Fragment {
    TimePicker timePicker;
    EditText title;
    Button scheduleAlarm;
    CheckBox recurring;
    CheckBox mon;
    CheckBox tue;
    CheckBox wed;
    CheckBox thu;
    CheckBox fri;
    CheckBox sat;
    CheckBox sun;
    LinearLayout recurringOptions;
    // TODO: Rename parameter arguments, choose names that match



    public NewTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //createAlarmViewModel = ViewModelProviders.of(this).get(CreateAlarmViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newtask, container, false);
        TimePicker timePicker = (TimePicker)view.findViewById(R.id.fragment_createalarm_timePicker);
        EditText title = (EditText)view.findViewById(R.id.fragment_createalarm_title);
        Button scheduleAlarm = (Button)view.findViewById(R.id.fragment_createalarm_scheduleAlarm);
        recurring = (CheckBox) view.findViewById(R.id.fragment_createalarm_recurring );
        mon = (CheckBox) view.findViewById(R.id.fragment_createalarm_checkMon );
        tue = (CheckBox) view.findViewById(R.id.fragment_createalarm_checkTue );
        wed = (CheckBox) view.findViewById(R.id.fragment_createalarm_checkWed );
        thu = (CheckBox) view.findViewById(R.id.fragment_createalarm_checkThu );
        fri = (CheckBox) view.findViewById(R.id.fragment_createalarm_checkFri );
         sat = (CheckBox) view.findViewById(R.id.fragment_createalarm_checkSat );
         sun = (CheckBox) view.findViewById(R.id.fragment_createalarm_checkSun );
         recurringOptions = (LinearLayout) view.findViewById(R.id.fragment_createalarm_recurring_options );


        recurring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recurringOptions.setVisibility(View.VISIBLE);
                } else {
                    recurringOptions.setVisibility(View.GONE);
                }
            }
        });

        scheduleAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleAlarm();
                Navigation.findNavController(v).navigate(R.id.action_createAlarmFragment_to_alarmsListFragment);
            }
        });

        return view;
    }

    private void scheduleAlarm() {
        int alarmId = new Random().nextInt(Integer.MAX_VALUE);

        Alarm alarm = new Alarm(
                alarmId,
                timePicker.getHour(),
                timePicker.getMinute(),
                title.getText().toString(),
                true,
                recurring.isChecked(),
                mon.isChecked(),
                tue.isChecked(),
                wed.isChecked(),
                thu.isChecked(),
                fri.isChecked(),
                sat.isChecked(),
                sun.isChecked()
        );

        AlarmDatabase.insert(alarm);

        alarm.schedule(getContext());
    }
}
}