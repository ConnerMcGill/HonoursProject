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

public class Storage {

    /*
     From the Firebase documentation the @PropertyName marks a field to be renamed when serialised:
     https://firebase.google.com/docs/reference/android/com/google/firebase/database/PropertyName
     */

    private String name;
    private Double price;
    private String capacity;
    @PropertyName("price-per-gb")
    private Double pricePerGB;
    private String type;
    private String cache;
    @PropertyName("form-factor")
    private String formFactor;
    @PropertyName("interface")
    private String storageInterface;


    //Constructor
    public Storage(String name, Double price, String capacity, Double pricePerGB, String type, String cache, String formFactor, String storageInterface) {
        this.name = name;
        this.price = price;
        this.capacity = capacity;
        this.pricePerGB = pricePerGB;
        this.type = type;
        this.cache = cache;
        this.formFactor = formFactor;
        this.storageInterface = storageInterface;
    }

    public Storage(){
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

    @PropertyName("capacity")
    public String getCapacity() {
        return capacity;
    }

    @PropertyName("price-per-gb")
    public Double getPricePerGB() {
        return pricePerGB;
    }

    @PropertyName("type")
    public String getType() {
        return type;
    }

    @PropertyName("cache")
    public String getCache() {
        return cache;
    }

    @PropertyName("form-factor")
    public String getFormFactor() {
        return formFactor;
    }

    @PropertyName("interface")
    public String getStorageInterface() {
        return storageInterface;
    }
}
