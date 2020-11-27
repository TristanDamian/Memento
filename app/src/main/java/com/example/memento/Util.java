package com.example.memento;

import java.util.Calendar;

public class Util {     //contient certains traitements récurrents sur les données
    public static final String toDay(int day) throws Exception {    //renvoie une String représentant un jour de la semaine en fonction  d'un int
        switch (day) {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
        }
        throw new Exception("Could not locate day");
    }


}
