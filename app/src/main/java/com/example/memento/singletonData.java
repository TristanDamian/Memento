package com.example.memento;

public class singletonData {

    boolean offlineModeEnabled = false;
    String userID;

    private static final singletonData ourInstance = new singletonData();
    public static singletonData getInstance() {
        return ourInstance;
    }
    private singletonData() {
    }

    public void setOfflineModeEnabled(boolean b) {
        this.offlineModeEnabled = b;
    }
    public boolean getOfflineModeEnabled() {
        return offlineModeEnabled;
    }

    public void setUserID(String s) {
        this.userID = s;
    }
    public String getUserID() {
        return userID;
    }
}
