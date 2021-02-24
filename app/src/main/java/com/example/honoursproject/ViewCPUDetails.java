/*
Honours Project - PC part Builder
File: Selectcpu Class
Author: Conner McGill - B00320975
Date: 2021/02/10

Summary of file:

    This class assigns the HashMap data that is retrieved from the SelectCPU class and displays
    the detailed information in a nestedScrollView showing the extra details that the user would
    not see on the SelectCPU recycler view

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

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewCPUDetails extends AppCompatActivity {

    //Tag used for debugging the firestore data retrieval
    private static final String TAG = "ViewCPUDetailsTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cpu_details);

        //Get all the data for the CPU firestore document that was selected as a HashMap
        Intent intent = getIntent();
        final HashMap<String, Object> cpuHashMapData = (HashMap<String, Object>) intent.getSerializableExtra("hashMap");
        //Randomly testing I can get some data here
        Log.d(TAG, "onCreate: " + cpuHashMapData.get("l1 cache"));
        Log.d(TAG, "values: " + cpuHashMapData.keySet());

        //Setup references to UI elements within the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("CPU Details:");

        //Go back to the previous activity in the activity backstack
        //https://stackoverflow.com/questions/49350686/back-to-previous-activity-arrow-button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                finish();
            }
        });


        //Get the image of the CPU and set it to the imageView. First though store the name of the
        //cpu which is the name of the image file in a String variable
        String cpuImageName = (String) cpuHashMapData.get("name");
        Log.d(TAG, "CPU Name: " + cpuImageName);

        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(cpuImageName + ".jpg");
        Log.d("my cpu image", String.valueOf(storageReference));

        try {
            //Create a placeholder that will store the image for the activity
            final File tempFile = File.createTempFile(cpuImageName, "jpg");
            //Try to retrieve the image from the firestore cloud storage
            storageReference.getFile(tempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Temp toast message for testing
                            //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                            ((ImageView) findViewById(R.id.imageOfClickedCPU)).setImageBitmap(bitmap);
                            Log.d("image retrieved", "The image has been retrieved successfully. Which makes sense if you can actually see it");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //If the image can't be retrieved for whatever reason then display the toast as an error message and write a log statement to the console
                    Toast.makeText(ViewCPUDetails.this, "Image failed to be retrieved", Toast.LENGTH_SHORT).show();
                    Log.d("image not retrieved", "The image has failed to be retrieved successfully. Which makes sense as you can't see the image");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Assign the values into their respective text views:

        //Name of the CPU:
        TextView titleOfClickedCPU = findViewById(R.id.nameOfClickedCPU);
        titleOfClickedCPU.setText((CharSequence) cpuHashMapData.get("name"));

        //Price of the CPU:
        TextView priceOfClickedCPU = findViewById(R.id.priceOfClickedCPU);
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String price = Double.toString((Double) cpuHashMapData.get("price"));
        Log.d(TAG, "price: " + price);
        priceOfClickedCPU.setText("Price: £" + price);

        //Manufacturer of the CPU:
        TextView manufacturerOfCPU = findViewById(R.id.cpuManufacturerClickedNameValue);
        manufacturerOfCPU.setText((CharSequence) cpuHashMapData.get("manufacturer"));

        //Core Count of CPU:
        TextView coreCountOfCPU = findViewById(R.id.cpuCoreCountClickedValue);
        //Convert the core count from the HashMap (it's a long for some reason) to a string
        Long coreCountNum = (Long) cpuHashMapData.get("core count");
        Log.d(TAG, "onCreate: " + coreCountNum);
        String coreCountString = Long.toString(coreCountNum);
        Log.d(TAG, "String core count: " + coreCountString);
        coreCountOfCPU.setText(coreCountString);

        //Core Clock of CPU:
        TextView coreClockOfCPU = findViewById(R.id.cpuCoreClockClickedValue);
        coreClockOfCPU.setText((CharSequence) cpuHashMapData.get("core clock"));

        //Boost Clock of CPU:
        TextView boostClockOfCPU = findViewById(R.id.cpuBoostClockClickedValue);
        boostClockOfCPU.setText((CharSequence) cpuHashMapData.get("boost clock"));

        //TDP of CPU:
        TextView tdpOfCPU = findViewById(R.id.cpuTDPClickedValue);
        //Convert the long value to a string so I can add it into the TextView
        Long cpuTDP = (Long) cpuHashMapData.get("tdp");
        String cpuTDPString = Long.toString(cpuTDP);
        tdpOfCPU.setText(cpuTDPString + " W");

        //Series of CPU:
        TextView seriesOfCPU = findViewById(R.id.cpuSeriesClickedValue);
        seriesOfCPU.setText((CharSequence) cpuHashMapData.get("series"));

        //Microarchitecture of cpu:
        TextView microarchitectureOfCPU = findViewById(R.id.cpuMicroarchitectureClickedValue);
        microarchitectureOfCPU.setText((CharSequence) cpuHashMapData.get("microarchitecture"));

        //Core Family of CPU:
        TextView coreFamilyOfCPU = findViewById(R.id.cpuCoreFamilyClickedValue);
        coreFamilyOfCPU.setText((CharSequence) cpuHashMapData.get("core family"));

        //Socket of CPU:
        TextView socketOfCPU = findViewById(R.id.cpuSocketClickedValue);
        socketOfCPU.setText((CharSequence) cpuHashMapData.get("socket"));

        //Integrated Graphics of CPU:
        TextView integratedGraphicsOfCPU = findViewById(R.id.cpuIntergratedGraphicsClickedValue);
        integratedGraphicsOfCPU.setText((CharSequence) cpuHashMapData.get("integrated graphics"));

        //Maximum supported memory of CPU:
        TextView maxSupportedMemOfCPU = findViewById(R.id.maxMemorySupportClickedValue);
        Long maxSupportedMemLong = (Long) cpuHashMapData.get("maximum supported memory");
        String maxSupportedMemString = Long.toString(maxSupportedMemLong);
        maxSupportedMemOfCPU.setText(maxSupportedMemString);

        //Includes CPU Cooler:
        TextView includesCPUCooler = findViewById(R.id.cpuIncludesCoolerClickedValue);
        includesCPUCooler.setText((CharSequence) cpuHashMapData.get("includes cpu cooler"));

        //L1 Cache of CPU:
        //As L1 Cache field in the document is an array I am storing it as an ArrayList
        ArrayList<String> l1cache = (ArrayList<String>) cpuHashMapData.get("l1 cache");
        TextView cpuL1CacheInstruction = findViewById(R.id.cpuL1CacheClickedValueZero);
        cpuL1CacheInstruction.setText("• " + l1cache.get(0));
        TextView cpuL1CacheData = findViewById(R.id.cpuL1CacheClickedValueOne);
        cpuL1CacheData.setText("• " + l1cache.get(1));

        //L2 Cache of CPU:
        TextView cpuL2Cache = findViewById(R.id.cpuL2CacheClickedValue);
        cpuL2Cache.setText((CharSequence) cpuHashMapData.get("l2 cache"));

        //L3 Cache of CPU:
        TextView cpuL3Cache = findViewById(R.id.cpuL3CacheClickedValue);
        cpuL3Cache.setText((CharSequence) cpuHashMapData.get("l3 cache"));

        //Lithography of cpu:
        TextView lithographyOfCPU = findViewById(R.id.cpuLithographyClickedValue);
        Long lithographyOfCPULong = (Long) cpuHashMapData.get("lithography");
        String lithographyOfCPUString = Long.toString(lithographyOfCPULong);
        lithographyOfCPU.setText(lithographyOfCPUString + "nm");

        //SMT of CPU:
        TextView smtOfCPU = findViewById(R.id.cpuSMTClickedValue);
        smtOfCPU.setText((CharSequence) cpuHashMapData.get("smt"));

        //External Review of CPU:
        TextView externalReviewOfCPU = findViewById(R.id.cpuExternalReviewClickedValue);
        externalReviewOfCPU.setText((CharSequence) cpuHashMapData.get("external review"));

        //Add CPU Details to CreateComputerListActivity if add button is clicked:
        Button addCPUtoListBtn = findViewById(R.id.addClickedCPUtoListBtn);
        addCPUtoListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pass the data back to the CreateComputerListActivity:

                String cpuName = (String) cpuHashMapData.get("name");
                Double cpuPriceDouble = (Double) cpuHashMapData.get("price");
                String cpuPrice = Double.toString(cpuPriceDouble);
                String cpuSocket = (String) cpuHashMapData.get("socket");

                Intent passCPUDataToCreateComputerActivity = new Intent
                        (ViewCPUDetails.this, CreateComputerListActivity.class);
                passCPUDataToCreateComputerActivity.putExtra("CPU NAME", cpuName);
                passCPUDataToCreateComputerActivity.putExtra("CPU PRICE", cpuPrice);
                passCPUDataToCreateComputerActivity.putExtra("CPU SOCKET", cpuSocket);
                startActivity(passCPUDataToCreateComputerActivity);

            }
        });


    }

}