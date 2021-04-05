/*
Honours Project - PC part Builder
File: Case Class
Author: Conner McGill - B00320975
Date: 2021/03/08

Summary of file:

    This class acts as a model for the recyclerview in order to store data
    that is retrieved from firestore and then passed into the recyclerView

 */

package com.example.honoursproject;

import com.google.firebase.firestore.PropertyName;

public class Case {

     /*
     From the Firebase documentation the @PropertyName marks a field to be renamed when serialised:
     https://firebase.google.com/docs/reference/android/com/google/firebase/database/PropertyName
     */

    private String name;
    private Double price;
    private String type;
    private String colour;
    @PropertyName("side-panel-window")
    private String sidePanel;
    @PropertyName("internal-3-bays")
    private Long internal3inchBay;
    @PropertyName("internal-2-bays")
    private Long internal2inchBay;


    //Constructor
    public Case(String name, Double price, String type, String colour, String sidePanel, Long internal3inchBay, Long internal2inchBay) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.colour = colour;
        this.sidePanel = sidePanel;
        this.internal3inchBay = internal3inchBay;
        this.internal2inchBay = internal2inchBay;
    }

    public Case(){
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

    @PropertyName("type")
    public String getType() {
        return type;
    }

    @PropertyName("colour")
    public String getColour() {
        return colour;
    }

    @PropertyName("side-panel-window")
    public String getSidePanel() {
        return sidePanel;
    }

    @PropertyName("internal-3-bays")
    public Long getInternal3inchBay() {
        return internal3inchBay;
    }

    @PropertyName("internal-2-bays")
    public Long getInternal2inchBay() {
        return internal2inchBay;
    }
}
