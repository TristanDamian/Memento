package com.example.memento;

import android.provider.ContactsContract;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AlarmDatabase {    //gère les accès à la base de données Firestore
    FirebaseApp App;
    static FirebaseFirestore Database;

    static List<Timestamp> monthlyData;
    static List<Boolean> last20;

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
        docData.put("sport", alarm.isSport());
        docData.put("relax", alarm.isRelax());
        String test=Integer.toString(alarm.getAlarmId());
        singletonData data = singletonData.getInstance();
        Database.collection("Alarms").document(Integer.toString(alarm.getAlarmId())).set(docData);
        Database.collection("Stats").document(Integer.toString(alarm.getAlarmId()));

        if(alarm.isSport()){
            Database.collection("UserInfo").document(data.getUserID()).update("sport", true);
        }
        if(alarm.isRelax()){
            Database.collection("UserInfo").document(data.getUserID()).update("relax", true);
        }
    }

    public static void done(final Alarm alarm){           // enregistre la modification de started dans la base de données

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
        docData.put("sport",alarm.isSport());
        docData.put("relax",alarm.isRelax());
        final String test=Integer.toString(alarm.getAlarmId());
        Database.collection("Alarms").document(Integer.toString(alarm.getAlarmId())).set(docData);

        if(alarm.isStarted())
            return;

        final Map<String, Object> statsData = new HashMap<>();

        Database.collection("Stats").document(test).get().addOnCompleteListener( // Récupère les stats déjà présentes sur la database et les modifie, pour ensuite les sauvegarder
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            monthlyData = (List<Timestamp>) document.get("monthlyData");

                            if(monthlyData == null) // Si il n'existe pas de données mensuelles, crée une liste
                                monthlyData = new LinkedList<>();

                            last20 = (List<Boolean>) document.get("last20");

                            if(last20 == null) // Si il n'existe pas de données sur les 20 dernières itérations, crée une liste
                                last20 = new LinkedList<>();

                            Timestamp timestamp = new Timestamp(new Date());
                            monthlyData.add(timestamp);

                            last20.add(true);

                            if(last20.size() > 20) // Si la liste dépasse 20 booléens, réduit de 1
                                last20.remove(0);

                            statsData.put("monthlyData", monthlyData);
                            statsData.put("last20", last20);
                            Database.collection("Stats").document(test).set(statsData);

                        }
                    }
                }
        );
    }

    public static void delete(Alarm alarm){  // supprimme une alarme
        String test=Integer.toString(alarm.getAlarmId());
        Database.collection("Alarms").document(test).delete();
        Database.collection("Stats").document(test).delete();
    }

    public static void deleteWithID(int ID){  //supprimme une alarme à partir
        String test=Integer.toString(ID);
        Database.collection("Alarms").document(test).delete();
    }

    public static void insertUser(User user){
        CollectionReference userInfo = Database.collection("UserInfo");
        Map<String, Object> userData = new HashMap<>();
        userData.put("fullname",user.getfullname());
        userData.put("age",user.getAge());
        userData.put("sport",false);
        userData.put("relax",false);
        userData.put("UID",user.getuid());
        singletonData data = singletonData.getInstance();
        userInfo.document(data.getUserID()).set(userData);
    }
}