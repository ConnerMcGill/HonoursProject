/*
Honours Project - PC part Builder
File: Motherboard Class
Author: Conner McGill - B00320975
Date: 2021/03/03

Summary of file:

    This class acts as a model for the recyclerview in order to store data
    that is retrieved from firestore and then passed into the recyclerView

 */


package com.example.honoursproject;

import com.google.firebase.firestore.PropertyName;

public class Motherboard {

    /*
     From the Firebase documentation the @PropertyName marks a field to be renamed when serialised:
     https://firebase.google.com/docs/reference/android/com/google/firebase/database/PropertyName
     */

    private String name;
    private double price;
    private String socket;
    @PropertyName("form-factor")
    private String formFactor;
    @PropertyName("max-memory")
    private int memoryMax;
    @PropertyName("memory-slots")
    private int memorySlots;
    private String colour;


    //Constructor
    public Motherboard(String name, double price, String socket, String formFactor, int memoryMax, int memorySlots, String colour) {
        this.name = name;
        this.price = price;
        this.socket = socket;
        this.formFactor = formFactor;
        this.memoryMax = memoryMax;
        this.memorySlots = memorySlots;
        this.colour = colour;
    }

    public Motherboard(){
        //empty constructor needed for firebase storing the relevant data for the cpu objects
    }

    //Getter methods - all getter methods should start with get for firebase to work
    @PropertyName("name")
    public String getMotherboardName() {
        return name;
    }

    @PropertyName("price")
    public double getMotherboardPrice() {
        return price;
    }

    @PropertyName("socket")
    public String getMotherboardSocket() {
        return socket;
    }

    @PropertyName("form-factor")
    public String getFormFactor() {
        return formFactor;
    }

    @PropertyName("max-memory")
    public int getMemoryMax() {
        return memoryMax;
    }

    @PropertyName("memory-slots")
    public int getMemorySlots() {
        return memorySlots;
    }

    public String getColour() {
        return colour;
    }
}
