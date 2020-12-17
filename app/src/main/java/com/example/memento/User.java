package com.example.memento;

public class User {

    public String fullname;
    public String age;
    public String email;
    public boolean relax;
    public boolean sport;
    public String uid;

    public User(){

    }

    public User(String sfullname, String sAge, String sEmail, String uid)
    {
        this.fullname = sfullname;
        this.age = sAge;
        this.email = sEmail;
        this.uid = uid;
    }

    public String getfullname() {
        return fullname;
    }

    public String getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public void setfullname(String fullname) {
        this.fullname = fullname;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getRelax() {
        return relax;
    }

    public void setRelax(boolean relax) {
        this.relax = relax;
    }

    public boolean getSport() {
        return sport;
    }

    public void setSport(boolean sport) {
        this.sport = sport;
    }

    public String getuid() {
        return uid;
    }

    public void setuid(String uid) {
        this.uid = uid;
    }
}
