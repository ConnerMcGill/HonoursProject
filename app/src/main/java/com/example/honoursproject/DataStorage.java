/*
Honours Project - PC part Builder
File: DataStorage class
Author: Conner McGill - B00320975
Date: 2021/03/02

Summary of file:

    The class contains a Hashmap that stores the data required for the CreateComputerListActivity.
    It stores the data in a hashmap so the views in the CreateComputerListActivity can be populated
    properly if there's data. It would not work with a bundle/saved instance state or shared preference
    for some reason
 */


package com.example.honoursproject;

import java.util.HashMap;
import java.util.Objects;

public class DataStorage {

    public static HashMap<String, String> computerList = new HashMap<>();

    public HashMap<String, String> getComputerList() {

        return computerList;

    }



}
