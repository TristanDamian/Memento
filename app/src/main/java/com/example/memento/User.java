package com.example.memento;

public class User {

    public String fullName;
    public String age;
    public String email;

    public User(){

    }

    public User(String sFullName, String sAge, String sEmail)
    {
        this.fullName = sFullName;
        this.age = sAge;
        this.email = sEmail;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
