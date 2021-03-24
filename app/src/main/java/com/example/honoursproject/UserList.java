package com.example.honoursproject;

import com.google.firebase.firestore.PropertyName;

public class UserList {

    @PropertyName("title")
    private String title;
    @PropertyName("description")
    private String description;

    //Constructor
    public UserList(String title, String description){
        this.title = title;
        this.description = description;
    }

    public UserList(){

    }

    @PropertyName("title")
    public String getTitle() {
        return title;
    }

    @PropertyName("description")
    public String getDescription() {
        return description;
    }
}
