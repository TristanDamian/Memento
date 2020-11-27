package com.example.memento;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "TOAST", Toast.LENGTH_SHORT).show();
        System.out.println(intent);
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            String toastText = String.format("Alarm Reboot");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            RecreateAlarm(context);             //L'appareil a redémarré, on doit recréer les alarmes à partir de la base de données
        }
        else {
            String toastText = String.format("Alarm Received");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            if (!intent.getBooleanExtra("RECURRING", false)) {
                startRingService(context, intent);
            } {
                if (alarmIsToday(intent)) {
                    startRingService(context, intent);
                }
            }
        }
    }

    private boolean alarmIsToday(Intent intent) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        switch(today) {
            case Calendar.MONDAY:
                if (intent.getBooleanExtra("MONDAY", false))
                    return true;
                return false;
            case Calendar.TUESDAY:
                if (intent.getBooleanExtra("TUESDAY", false))
                    return true;
                return false;
            case Calendar.WEDNESDAY:
                if (intent.getBooleanExtra("WEDNESDAY", false))
                    return true;
                return false;
            case Calendar.THURSDAY:
                if (intent.getBooleanExtra("THURSDAY", false))
                    return true;
                return false;
            case Calendar.FRIDAY:
                if (intent.getBooleanExtra("FRIDAY", false))
                    return true;
                return false;
            case Calendar.SATURDAY:
                if (intent.getBooleanExtra("SATURDAY", false))
                    return true;
                return false;
            case Calendar.SUNDAY:
                if (intent.getBooleanExtra("SUNDAY", false))
                    return true;
                return false;
        }
        return false;
    }

    private void startRingService(Context context, Intent intent) {
        Intent intentService = new Intent(context, RingService.class);
        int id=intent.getIntExtra("ID",0);
        boolean recurring=intent.getBooleanExtra("RECURRING",true);
        intentService.putExtra("ID",id);
        intentService.putExtra("RECURRING",recurring);
        intentService.putExtra("TITLE", intent.getStringExtra("TITLE"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        }

    }

    private void RecreateAlarm(Context context) {
        Intent intentService = new Intent(context, RingService.class);
        context.startService(intentService);

    }
}

