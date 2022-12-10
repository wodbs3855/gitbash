package com.example.testproject;

public class User {

    public String name;
    public String firstLunch;
    public String email;



    public User(){

    }

    public User(String name , String email,String firstLunch){
        this.name = name;
        this.email = email;
        this.firstLunch = firstLunch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getFirstLunch() {
        return firstLunch;
    }

    public void setFirstLunch(String firstLunch) {
        this.firstLunch = firstLunch;
    }

}


