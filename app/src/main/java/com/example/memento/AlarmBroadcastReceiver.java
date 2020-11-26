package com.example.memento;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
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
        intentService.putExtra("TITLE", intent.getStringExtra("TITLE"));
        context.startService(intentService);

    }

    private void RecreateAlarm(Context context) {
        Intent intentService = new Intent(context, RecreateService.class);
        context.startService(intentService);

    }
}
}
