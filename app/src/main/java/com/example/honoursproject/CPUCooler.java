/*
Honours Project - PC part Builder
File: CPU Class
Author: Conner McGill - B00320975
Date: 2021/02/26

Summary of file:

    This class acts as a model for the recyclerview in order to store data
    that is retrieved from firestore and then passed into the recyclerView

 */



package com.example.honoursproject;

import com.google.firebase.firestore.PropertyName;

public class CPUCooler {

    /*
     From the Firebase documentation the @PropertyName marks a field to be renamed when serialised:
     https://firebase.google.com/docs/reference/android/com/google/firebase/database/PropertyName
     */

    private String name;
    private double price;
    @PropertyName("fan-rpm")
    private String fanrpm;
    @PropertyName("noise-level")
    private String noiselevel;
    private String colour;
    @PropertyName("radiator-size")
    private String radsize;


    public CPUCooler(String name, double price, String fanrpm, String noiselevel, String colour, String radsize) {
        this.name = name;
        this.price = price;
        this.fanrpm = fanrpm;
        this.noiselevel = noiselevel;
        this.colour = colour;
        this.radsize = radsize;
    }

    public CPUCooler(){
        //Default constructor
    }


    @PropertyName("name")
    public String getCPUCoolerName() {
        return name;
    }

    @PropertyName("price")
    public double getCPUCoolerPrice() {
        return price;
    }

    @PropertyName("fan-rpm")
    public String getFanrpm() {
        return fanrpm;
    }

    @PropertyName("noise-level")
    public String getNoiselevel() {
        return noiselevel;
    }

    public String getColour() {
        return colour;
    }

    @PropertyName("radiator-size")
    public String getRadsize() {
        return radsize;
    }
}
