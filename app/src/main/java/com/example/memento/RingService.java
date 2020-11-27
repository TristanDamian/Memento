package com.example.memento;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.example.memento.MementoApp.CHANNEL_ID;

public class RingService extends Service {   //crée la notification lorsqu'on a reçu le broadcast correspondant à l'alarme

        private MediaPlayer mediaPlayer;
        private Vibrator vibrator;

        @Override
        public void onCreate() {
            super.onCreate();
            AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume (AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0);
            mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
            mediaPlayer.setLooping(true);      //pour lire la sonnerie en boucle tant que le service n'est pas arrêté

            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);  //vibration ant que le service n'est pas arrêté

        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Intent notificationIntent = new Intent(this, RingActivity.class);
            int id=intent.getIntExtra("ID",0);
            boolean recurring=intent.getBooleanExtra("RECURRING",true);
            notificationIntent.putExtra("ID",id);
            notificationIntent.putExtra("RECURRING",recurring);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            String alarmTitle = String.format("%s Alarm", intent.getStringExtra("TITLE"));

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID) //création de la notification
                    .setContentTitle(alarmTitle)
                    .setContentText("Ring Ring .. Ring Ring")
                    .setSmallIcon(R.drawable.alarm_clock)
                    .setContentIntent(pendingIntent)
                    .build();

            mediaPlayer.start();

            long[] pattern = { 0, 100, 1000 };
            vibrator.vibrate(pattern, 0);

            startForeground(1, notification);  //lancement de la notification en mode Foreground

            return START_STICKY;
        }

        @Override
        public void onDestroy() {  //arrête la sonnerie et les vibrations quand le service est détruit
            super.onDestroy();

            mediaPlayer.stop();
            vibrator.cancel();
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
}


