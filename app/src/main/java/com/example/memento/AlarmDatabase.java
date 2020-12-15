package com.example.memento;

import android.widget.Toast;



import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AlarmDatabase {    //gère les accès à la base de données Firestore
    FirebaseApp App;
    static FirebaseFirestore Database;
    public AlarmDatabase() throws IOException {
        /*FileInputStream serviceAccount =
                new FileInputStream("memento-cc796-firebase-adminsdk-nzn86-4c8adca17a.json");

        FirebaseOptions.Builder builder = FirebaseOptions.builder();
        FirebaseOptions options = builder.setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://memento-cc796.firebaseio.com")
                .build();

        App=FirebaseApp.initializeApp(options);
        Database = FirebaseDatabase.getInstance(App);*/

        Database=FirebaseFirestore.getInstance();      //on récupère l'instance de FireStore
    }

    public static void insert(Alarm alarm){        // insère une alarme dans la base de données
        Map<String, Object> docData = new HashMap<>();
        docData.put("hour",alarm.getHour());
        docData.put("minute",alarm.getMinute());
        docData.put("recurring",alarm.isRecurring());
        docData.put("started",alarm.isStarted());
        docData.put("title",alarm.getTitle());
        docData.put("friday",alarm.isFriday());
        docData.put("monday",alarm.isMonday());
        docData.put("tuesday",alarm.isTuesday());
        docData.put("wednesday",alarm.isWednesday());
        docData.put("thursday",alarm.isThursday());
        docData.put("saturday",alarm.isSaturday());
        docData.put("sunday",alarm.isSunday());
        docData.put("UID", alarm.getUserID());
        String test=Integer.toString(alarm.getAlarmId());
        Database.collection("Alarms").document(Integer.toString(alarm.getAlarmId())).set(docData);
    }

    public static void done(Alarm alarm){           // enregistre la modification de started dans la base de données
        Map<String, Object> docData = new HashMap<>();
        docData.put("hour",alarm.getHour());
        docData.put("minute",alarm.getMinute());
        docData.put("recurring",alarm.isRecurring());
        docData.put("started",!alarm.isStarted());
        docData.put("title",alarm.getTitle());
        docData.put("friday",alarm.isFriday());
        docData.put("monday",alarm.isMonday());
        docData.put("tuesday",alarm.isTuesday());
        docData.put("wednesday",alarm.isWednesday());
        docData.put("thursday",alarm.isThursday());
        docData.put("saturday",alarm.isSaturday());
        docData.put("sunday",alarm.isSunday());
        docData.put("UID", alarm.getUserID());
        String test=Integer.toString(alarm.getAlarmId());
        Database.collection("Alarms").document(Integer.toString(alarm.getAlarmId())).set(docData);
    }

    public static void delete(Alarm alarm){  // supprimme une alarme
        String test=Integer.toString(alarm.getAlarmId());
        Database.collection("Alarms").document(test).delete();
    }

    public static void deleteWithID(int ID){  //supprimme une alarme à partir
        String test=Integer.toString(ID);
        Database.collection("Alarms").document(test).delete();
    }
}