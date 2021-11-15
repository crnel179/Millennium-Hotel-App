package com.example.millenniumhotelmobileapp;

// Creating an object through which the registration user data is sent to Firebase
public class User {

    public String firstName, lastName, email;

    public User(){

    }

    public User(String firstName, String lastName, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

}
