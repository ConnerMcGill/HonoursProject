/*
Honours Project - PC part Builder
File: Storage Class
Author: Conner McGill - B00320975
Date: 2021/03/05

Summary of file:

    This class acts as a model for the recyclerview in order to store data
    that is retrieved from firestore and then passed into the recyclerView

 */

package com.example.honoursproject;

import com.google.firebase.firestore.PropertyName;

public class GPU {

    /*
     From the Firebase documentation the @PropertyName marks a field to be renamed when serialised:
     https://firebase.google.com/docs/reference/android/com/google/firebase/database/PropertyName
     */

    private String name;
    private Double price;
    private String chipset;
    private Long memory;
    @PropertyName("core-clock")
    private Long coreClock;
    @PropertyName("boost-clock")
    private Long boostClock;
    private String colour;
    private String length;

    //Constructor
    public GPU(String name, Double price, String chipset, Long memory, Long coreClock, Long boostClock, String colour, String length) {
        this.name = name;
        this.price = price;
        this.chipset = chipset;
        this.memory = memory;
        this.coreClock = coreClock;
        this.boostClock = boostClock;
        this.colour = colour;
        this.length = length;
    }

    public GPU(){
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

    @PropertyName("chipset")
    public String getChipset() {
        return chipset;
    }

    @PropertyName("memory")
    public Long getMemory() {
        return memory;
    }

    @PropertyName("core-clock")
    public Long getCoreClock() {
        return coreClock;
    }

    @PropertyName("boost-clock")
    public Long getBoostClock() {
        return boostClock;
    }

    @PropertyName("colour")
    public String getColour() {
        return colour;
    }

    @PropertyName("length")
    public String getLength() {
        return length;
    }
}
