/*
Honours Project - PC part Builder
File: CPU Class
Author: Conner McGill - B00320975
Date: 2021/02/19

Summary of file:

    This class acts as a model for the recyclerview in order to store data
    that is retrieved from firestore and then passed into the recyclerView

 */


package com.example.honoursproject;

import com.google.firebase.firestore.PropertyName;

public class CPU {

    /*
     From the Firebase documentation the @PropertyName marks a field to be renamed when serialised:
     https://firebase.google.com/docs/reference/android/com/google/firebase/database/PropertyName
     */

    private String name;
    private double price;
    @PropertyName("core count")
    private int coreCount;
    @PropertyName("core clock")
    private String coreClock;
    @PropertyName("boost clock")
    private String boostClock;
    private int tdp;
    @PropertyName("integrated graphics")
    private String integratedGraphics;
    private String smt;

    //Constructor
    public CPU(String name, double price, int coreCount, String coreClock, String boostClock, int tdp, String integratedGraphics, String smt) {
        this.name = name;
        this.price = price;
        this.coreCount = coreCount;
        this.coreClock = coreClock;
        this.boostClock = boostClock;
        this.tdp = tdp;
        this.integratedGraphics = integratedGraphics;
        this.smt = smt;
    }

    public CPU() {
        //empty constructor needed for firebase storing the relevant data for the cpu objects
    }

    //Getter methods - all getter methods should start with get for firebase to work
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @PropertyName("core count")
    public int getCoreCount() {
        return coreCount;
    }

    @PropertyName("core clock")
    public String getCoreClock() {
        return coreClock;
    }

    @PropertyName("boost clock")
    public String getBoostClock() {
        return boostClock;
    }

    public int getTdp() {
        return tdp;
    }

    @PropertyName("integrated graphics")
    public String getIntegratedGraphics() {
        return integratedGraphics;
    }

    public String getSmt() {
        return smt;
    }
}
