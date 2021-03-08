/*
Honours Project - PC part Builder
File: PSU Class
Author: Conner McGill - B00320975
Date: 2021/03/08

Summary of file:

    This class acts as a model for the recyclerview in order to store data
    that is retrieved from firestore and then passed into the recyclerView

 */

package com.example.honoursproject;

import com.google.firebase.firestore.PropertyName;

public class PSU {

    /*
     From the Firebase documentation the @PropertyName marks a field to be renamed when serialised:
     https://firebase.google.com/docs/reference/android/com/google/firebase/database/PropertyName
     */

    private String name;
    private Double price;
    @PropertyName("form-factor")
    private String formFactor;
    @PropertyName("efficiency-rating")
    private String efficiencyRating;
    private Long wattage;
    private String modular;

    //Constructor
    public PSU(String name, Double price, String formFactor, String efficiencyRating, Long wattage, String modular) {
        this.name = name;
        this.price = price;
        this.formFactor = formFactor;
        this.efficiencyRating = efficiencyRating;
        this.wattage = wattage;
        this.modular = modular;
    }

    public PSU(){
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

    @PropertyName("form-factor")
    public String getFormFactor() {
        return formFactor;
    }

    @PropertyName("efficiency-rating")
    public String getEfficiencyRating() {
        return efficiencyRating;
    }

    @PropertyName("wattage")
    public Long getWattage() {
        return wattage;
    }

    @PropertyName("modular")
    public String getModular() {
        return modular;
    }


}
