/*
Honours Project - PC part Builder
File: ViewMemoryDetails Class
Author: Conner McGill - B00320975
Date: 2021/03/04

Summary of file:

    This class assigns the HashMap data that is retrieved from the SelectMemory class and displays
    the detailed information in a nestedScrollView showing the extra details that the user would
    not see on the SelectMemory recycler view

 */

package com.example.honoursproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ViewMemoryDetails extends AppCompatActivity {


    //Tag used for debugging the firestore data retrieval
    private static final String TAG = "ViewMemoryDetailsTAG";

    //DataStorage Instance:
    DataStorage memoryData = new DataStorage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_memory_details);

        //Get all the data for the motherboard firestore document that was selected as a HashMap
        Intent intent = getIntent();
        final HashMap<String, Object> memoryHashMapData = (HashMap<String, Object>) intent.getSerializableExtra("hashMap");
        //Randomly testing I can get some data here
        Log.d(TAG, "onCreate: " + memoryHashMapData.get("name"));
        Log.d(TAG, "values: " + memoryHashMapData.keySet());

        //Setup references to UI elements within the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Memory Details:");

        //Go back to the previous activity in the activity backstack
        //https://stackoverflow.com/questions/49350686/back-to-previous-activity-arrow-button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                finish();
            }
        });

        //Get the image of the memory and set it to the imageView. First though store the name of the
        //memory which is the name of the image file in a String variable
        String memoryImageName = (String) memoryHashMapData.get("name");
        Log.d(TAG, "memory Name: " + memoryImageName);

        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(memoryImageName + ".jpg");
        Log.d("my memory image", String.valueOf(storageReference));

        try {
            //Create a placeholder that will store the image for the activity
            final File tempFile = File.createTempFile(memoryImageName, "jpg");
            //Try to retrieve the image from the firestore cloud storage
            storageReference.getFile(tempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Temp toast message for testing
                            //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                            ((ImageView) findViewById(R.id.imageOfCLickedMemory)).setImageBitmap(bitmap);
                            Log.d("image retrieved", "The image has been retrieved successfully. Which makes sense if you can actually see it");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //If the image can't be retrieved for whatever reason then display the toast as an error message and write a log statement to the console
                    Toast.makeText(ViewMemoryDetails.this, "Image failed to be retrieved", Toast.LENGTH_SHORT).show();
                    Log.d("image not retrieved", "The image has failed to be retrieved successfully. Which makes sense as you can't see the image");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Assign the values into their respective text views:

        //Name of the memory:
        TextView titleOfClickedMemory = findViewById(R.id.nameOfClickedMemory);
        titleOfClickedMemory.setText((CharSequence) memoryHashMapData.get("name"));

        //Price of the memory:
        TextView priceOfClickedMemory = findViewById(R.id.priceOfClickedMemory);
        Log.d(TAG, "onCreate: " + memoryHashMapData.get("price"));
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String price = Double.toString((Double) memoryHashMapData.get("price"));
        Log.d(TAG, "price: " + price);
        priceOfClickedMemory.setText("Price: £" + price);

        //Manufacturer of the memory:
        TextView manufacturerOfMemory = findViewById(R.id.memoryManufacturerClickedValue);
        manufacturerOfMemory.setText((CharSequence) memoryHashMapData.get("manufacturer"));

        //Form Factor of the memory:
        TextView formFactorOfMemory = findViewById(R.id.memoryFormFactorClickedValue);
        formFactorOfMemory.setText((CharSequence) memoryHashMapData.get("form-factor"));

        //Modules of the memory:
        TextView modulesOfMemory = findViewById(R.id.memoryModulesClickedValue);
        modulesOfMemory.setText((CharSequence) memoryHashMapData.get("modules"));

        //Price per GB of the memory:
        TextView pricePerGBOfClickedMemory = findViewById(R.id.memoryPricePerGBClickedValue);
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String pricePerGB = Double.toString((Double) memoryHashMapData.get("price-per-gb"));
        Log.d(TAG, "price per GB: " + pricePerGB);
        pricePerGBOfClickedMemory.setText("£" + pricePerGB);

        //Colour of the memory:
        TextView colourOfMemory = findViewById(R.id.memoryColourClickedValue);
        colourOfMemory.setText((CharSequence) memoryHashMapData.get("colour"));

        //First word latency of the memory:
        TextView firstWordLatencyOfMemory = findViewById(R.id.memoryFirstWordLatencyClickedValue);
        firstWordLatencyOfMemory.setText((CharSequence) memoryHashMapData.get("first-word-latency"));

        //CAS Latency of memory:
        TextView casLatencyOfMemory = findViewById(R.id.memoryCASLatencyClickedValue);
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String casLatency = Long.toString((Long) memoryHashMapData.get("cas-latency"));
        casLatencyOfMemory.setText(casLatency);

        //Voltage of the memory:
        TextView voltageOfTheMemory = findViewById(R.id.memoryVoltageClickedValue);
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String voltageOfMemory = Double.toString((Double) memoryHashMapData.get("voltage"));
        voltageOfTheMemory.setText(voltageOfMemory + " V");

        //Timing of the memory:
        TextView timingOfTheMemory = findViewById(R.id.memoryTimingClickedValue);
        timingOfTheMemory.setText((CharSequence) memoryHashMapData.get("timing"));

        //Heat Spreader of the memory:
        TextView heatSpreaderOfMemory = findViewById(R.id.memoryHeatSpreaderClickedValue);
        heatSpreaderOfMemory.setText((CharSequence) memoryHashMapData.get("heat-spreader"));

        //External Review of the memory:
        TextView externalReviewOfMemory = findViewById(R.id.memoryExternalReviewClickedValue);
        externalReviewOfMemory.setText((CharSequence) memoryHashMapData.get("external-review"));

        //Add Memory Details to CreateComputerListActivity if add button is clicked:
        Button addMemoryToListBtn = findViewById(R.id.addClickedMemoryToListBtn);
        addMemoryToListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pass the data back to the CreateComputerListActivity by storing the data in the DataStorage
                //hashmap and then returning to the CreateComputerListActivity:

                String memoryName = (String) memoryHashMapData.get("name");
                Double memoryPrice = (Double) memoryHashMapData.get("price");
                String memoryPriceString = Double.toString(memoryPrice);
                Long memoryTDP = (Long) memoryHashMapData.get("tdp");

                Intent passMotherboardDataToCreateComputerActivity = new Intent
                        (ViewMemoryDetails.this, CreateComputerListActivity.class);

                memoryData.getComputerList().put("MEMORY NAME", memoryName);
                memoryData.getComputerList().put("MEMORY PRICE", memoryPriceString);
                memoryData.getComputerList().put("MEMORY TDP", String.valueOf(memoryTDP));
                Log.d(TAG, "onSuccess: " + memoryData.getComputerList().get("MEMORY NAME"));
                Log.d(TAG, "onSuccess: " + memoryData.getComputerList().get("MEMORY PRICE"));
                Log.d(TAG, "onSuccess: " + memoryData.getComputerList().get("MEMORY TDP"));

                startActivity(passMotherboardDataToCreateComputerActivity);

            }
        });


    }
}