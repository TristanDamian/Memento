package com.example.memento;

public interface OnCheckAlarmListener {    //interface pour s'assurer de l'implémentation de ses fonctions

        void onCheck(Alarm alarm);
        void onDelete(Alarm alarm);
}
