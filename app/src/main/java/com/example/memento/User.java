package com.example.memento;

//Classe contenant les informations d'un utilisateur
public class User {

    public String fullname;     //Nom
    public String age;          //Age
    public String email;        //Mail
    public boolean relax;       //Si il est intéressé par des activités avec le tag relaxation
    public boolean sport;       //Si il est intéressé par des activités avec le tag sport
    public String uid;          //ID

    //Constructeurs
    public User(){

    }

    public User(String sfullname, String sAge, String sEmail, String uid)
    {
        this.fullname = sfullname;
        this.age = sAge;
        this.email = sEmail;
        this.uid = uid;
    }

    //Getters
    public String getfullname() {
        return fullname;
    }

    public String getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public boolean getRelax() {
        return relax;
    }

    public boolean getSport() {
        return sport;
    }

    public String getuid() {
        return uid;
    }

    //Setters
    public void setfullname(String fullname) {
        this.fullname = fullname;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRelax(boolean relax) {
        this.relax = relax;
    }

    public void setSport(boolean sport) {
        this.sport = sport;
    }

    public void setuid(String uid) {
        this.uid = uid;
    }
}
