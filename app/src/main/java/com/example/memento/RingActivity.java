package com.example.memento;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

public class RingActivity extends AppCompatActivity {
        Button dismiss;
        Button snooze;
        ImageView clock;
        boolean recurring;
        int idToDelete;
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ring);
            dismiss=findViewById(R.id.activity_ring_dismiss);
            snooze=findViewById(R.id.activity_ring_snooze);
            clock=findViewById(R.id.activity_ring_clock);
            Intent start=getIntent();
            idToDelete=getIntent().getIntExtra("ID",0);
            recurring=getIntent().getBooleanExtra("RECURRING",true);
            dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentService = new Intent(getApplicationContext(), RingService.class);
                    //Context essai= getApplicationContext();

                    if(!recurring){
                        try {
                            AlarmDatabase manager= new AlarmDatabase();
                            manager.deleteWithID(idToDelete);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                    getApplicationContext().stopService(intentService);
                    finish();
                }
            });

            snooze.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.add(Calendar.MINUTE, 10);

                    Alarm alarm = new Alarm(
                            new Random().nextInt(Integer.MAX_VALUE),
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            "Snooze",
                            true,
                            false,
                            false,
                            false,
                            false,
                            false,
                            false,
                            false,
                            false
                    );

                    alarm.schedule(getApplicationContext());

                    Intent intentService = new Intent(getApplicationContext(), RingService.class);
                    getApplicationContext().stopService(intentService);
                    finish();
                }
            });

        }

}

