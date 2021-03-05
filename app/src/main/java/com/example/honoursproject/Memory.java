/*
Honours Project - PC part Builder
File: Memory Class
Author: Conner McGill - B00320975
Date: 2021/03/04

Summary of file:

    This class acts as a model for the recyclerview in order to store data
    that is retrieved from firestore and then passed into the recyclerView

 */


package com.example.honoursproject;

import com.google.firebase.firestore.PropertyName;

public class Memory {

    /*
     From the Firebase documentation the @PropertyName marks a field to be renamed when serialised:
     https://firebase.google.com/docs/reference/android/com/google/firebase/database/PropertyName
     */

    private String name;
    private Double price;
    private String speed;
    private String modules;
    @PropertyName("price-per-gb")
    private Double pricePerGB;
    private String colour;
    @PropertyName("first-word-latency")
    private String firstWordLatency;
    @PropertyName("cas-latency")
    private int casLatency;


    //Constructor
    public Memory(String name, Double price, String speed, String modules, Double pricePerGB, String colour, String firstWordLatency, int casLatency) {
        this.name = name;
        this.price = price;
        this.speed = speed;
        this.modules = modules;
        this.pricePerGB = pricePerGB;
        this.colour = colour;
        this.firstWordLatency = firstWordLatency;
        this.casLatency = casLatency;
    }

    public Memory(){
        //empty constructor needed for firebase storing the relevant data for the cpu objects
    }

    @PropertyName("name")
    public String getName() {
        return name;
    }

    @PropertyName("price")
    public Double getPrice() {
        return price;
    }

    @PropertyName("speed")
    public String getSpeed() {
        return speed;
    }

    @PropertyName("modules")
    public String getModules() {
        return modules;
    }

    @PropertyName("price-per-gb")
    public Double getPricePerGB() {
        return pricePerGB;
    }

    @PropertyName("colour")
    public String getColour() {
        return colour;
    }

    @PropertyName("first-word-latency")
    public String getFirstWordLatency() {
        return firstWordLatency;
    }

    @PropertyName("cas-latency")
    public int getCasLatency() {
        return casLatency;
    }
}
