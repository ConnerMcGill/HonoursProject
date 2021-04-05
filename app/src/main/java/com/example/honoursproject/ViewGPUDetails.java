/*
Honours Project - PC part Builder
File: ViewGPUDetails Class
Author: Conner McGill - B00320975
Date: 2021/03/05

Summary of file:

    This class assigns the HashMap data that is retrieved from the SelectGPU class and displays
    the detailed information in a nestedScrollView showing the extra details that the user would
    not see on the SelectGPU recycler view

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

public class ViewGPUDetails extends AppCompatActivity {

    //Tag used for debugging the firestore data retrieval
    private static final String TAG = "ViewGPUDetailsTAG";

    //DataStorage Instance:
    DataStorage gpuData = new DataStorage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_gpu_details);

        //Get all the data for the GPU firestore document that was selected as a HashMap
        Intent intent = getIntent();
        final HashMap<String, Object> gpuHashMapData = (HashMap<String, Object>) intent.getSerializableExtra("hashMap");
        //Randomly testing I can get some data here
        Log.d(TAG, "onCreate: " + gpuHashMapData.get("name"));
        Log.d(TAG, "values: " + gpuHashMapData.keySet());

        //Setup references to UI elements within the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("GPU Details:");

        //Go back to the previous activity in the activity backstack
        //https://stackoverflow.com/questions/49350686/back-to-previous-activity-arrow-button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                finish();
            }
        });

        //Get the image of the motherboard and set it to the imageView. First though store the name of the
        //GPU which is the name of the image file in a String variable
        String gpuImageName = (String) gpuHashMapData.get("name");
        Log.d(TAG, "gpu Name: " + gpuHashMapData.get("name"));

        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(gpuImageName + ".jpg");
        Log.d("my gpu image", String.valueOf(storageReference));

        try {
            //Create a placeholder that will store the image for the activity
            final File tempFile = File.createTempFile(gpuImageName, "jpg");
            //Try to retrieve the image from the firestore cloud storage
            storageReference.getFile(tempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Temp toast message for testing
                            //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                            ((ImageView) findViewById(R.id.imageOfClickedGPU)).setImageBitmap(bitmap);
                            Log.d("image retrieved", "The image has been retrieved successfully. Which makes sense if you can actually see it");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //If the image can't be retrieved for whatever reason then display the toast as an error message and write a log statement to the console
                    Toast.makeText(ViewGPUDetails.this, "Image failed to be retrieved", Toast.LENGTH_SHORT).show();
                    Log.d("image not retrieved", "The image has failed to be retrieved successfully. Which makes sense as you can't see the image");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Assign the values into their respective text views:

        //Name of the gpu:
        TextView titleOfClickedGPU = findViewById(R.id.nameOfClickedGPU);
        titleOfClickedGPU.setText((CharSequence) gpuHashMapData.get("name"));

        //Price of the gpu:
        TextView priceOfClickedGPU = findViewById(R.id.priceOfClickedGPU);
        Log.d(TAG, "onCreate: " + gpuHashMapData.get("price"));
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String price = Double.toString((Double) gpuHashMapData.get("price"));
        Log.d(TAG, "price: " + price);
        priceOfClickedGPU.setText("Price: £" + price);

        //Manufacturer of the gpu:
        TextView manufacturerOfGPU = findViewById(R.id.gpuManufacturerClickedValue);
        manufacturerOfGPU.setText((CharSequence) gpuHashMapData.get("manufacturer"));

        //Chipset of the gpu:
        TextView chipsetOfClickedGPU = findViewById(R.id.gpuChipsetClickedValue);
        chipsetOfClickedGPU.setText((CharSequence) gpuHashMapData.get("chipset"));

        //Memory of GPU:
        TextView memoryOfClickedGPU = findViewById(R.id.gpuMemoryClickedValue);
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String gpuMemSize = Long.toString((Long) gpuHashMapData.get("memory"));
        memoryOfClickedGPU.setText(gpuMemSize);

        //Memory type of GPU:
        TextView memoryTypeOfClickedGPU = findViewById(R.id.gpuMemoryTypeClickedValue);
        memoryTypeOfClickedGPU.setText((CharSequence) gpuHashMapData.get("memory-type"));

        //Core Clock of GPU:
        TextView coreClockOfClickedGPU = findViewById(R.id.gpuCoreClockClickedValue);
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String gpuCoreClock = Long.toString((Long) gpuHashMapData.get("core-clock"));
        coreClockOfClickedGPU.setText(gpuCoreClock + " MHz");

        //Boost clock of GPU:
        TextView boostClockOfClickedGPU = findViewById(R.id.gpuBoostClockClickedValue);
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String gpuBoostClock = Long.toString((Long) gpuHashMapData.get("boost-clock"));
        boostClockOfClickedGPU.setText(gpuBoostClock + " MHz");

        //Effective Memory Clock of GPU:
        TextView effectiveMemoryClockOfClickedGPU = findViewById(R.id.gpuEffectiveMemoryClockClickedValue);
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String effectiveMemClock = Long.toString((Long) gpuHashMapData.get("effective-memory-clock"));
        effectiveMemoryClockOfClickedGPU.setText(effectiveMemClock + " MHz");

        //Interface of GPU:
        TextView interfaceOfClickedGPU = findViewById(R.id.gpuInterfaceClickedValue);
        interfaceOfClickedGPU.setText((CharSequence) gpuHashMapData.get("interface"));

        //Colour of GPU:
        TextView colourOfClickedGPU = findViewById(R.id.gpuColourClickedValue);
        colourOfClickedGPU.setText((CharSequence) gpuHashMapData.get("colour"));

        //Frame Sync of GPU:
        TextView frameSyncOfClickedGPU = findViewById(R.id.gpuFrameSyncClickedValue);
        frameSyncOfClickedGPU.setText((CharSequence) gpuHashMapData.get("frame-sync"));

        //Length of GPU:
        TextView lengthOfClickedGPU = findViewById(R.id.gpuLengthClickedValue);
        lengthOfClickedGPU.setText((CharSequence) gpuHashMapData.get("length"));

        //TDP of GPU:
        TextView tdpOfClickedGPU = findViewById(R.id.gpuTDPClickedValue);
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String tdp = Long.toString((Long) gpuHashMapData.get("tdp"));
        tdpOfClickedGPU.setText(tdp + " W");

        //DVI Ports of GPU:
        TextView dviPortsOfClickedGPU = findViewById(R.id.gpuDVIPortsClickedValue);
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String dviPorts = Long.toString((Long) gpuHashMapData.get("dvi-ports"));
        dviPortsOfClickedGPU.setText(dviPorts);

        //HDMI Ports of GPU:
        TextView hdmiPortsOfClickedGPU = findViewById(R.id.gpuHDMIPortsClickedValue);
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String hdmiPorts = Long.toString((Long) gpuHashMapData.get("hdmi-ports"));
        hdmiPortsOfClickedGPU.setText(hdmiPorts);

        //Mini HDMI Ports of GPU:
        TextView miniHdmiPortsOfClickedGPU = findViewById(R.id.gpuMiniHDMIPortsClickedValue);
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String miniHdmiPorts = Long.toString((Long) gpuHashMapData.get("mini-hdmi-ports"));
        miniHdmiPortsOfClickedGPU.setText(miniHdmiPorts);

        //DisplayPorts of GPU:
        TextView displayPortsOfClickedGPU = findViewById(R.id.gpuDisplayPortsClickedValue);
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String gpuDisplayPorts = Long.toString((Long) gpuHashMapData.get("display-ports"));
        displayPortsOfClickedGPU.setText(gpuDisplayPorts);

        //Mini DisplayPorts of GPU:
        TextView miniDisplayPortsOfClickedGPU = findViewById(R.id.gpuMiniDisplayPortsClickedValue);
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String miniGpuDisplayPorts = Long.toString((Long) gpuHashMapData.get("mini-displayport-ports"));
        miniDisplayPortsOfClickedGPU.setText(miniGpuDisplayPorts);

        //Expansion Slot Width of GPU:
        TextView expansionSlotWidthOfClickedGPU = findViewById(R.id.gpuExpansionSlotWidthClickedValue);
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String expansionSlotWidth = Long.toString((Long) gpuHashMapData.get("expansion-slot-width"));
        expansionSlotWidthOfClickedGPU.setText(expansionSlotWidth);

        //Cooling of GPU:
        TextView coolingOfClickedGPU = findViewById(R.id.gpuCoolingClickedValue);
        coolingOfClickedGPU.setText((CharSequence) gpuHashMapData.get("cooling"));

        //External Power of GPU:
        TextView externalPowerOfClickedGPU = findViewById(R.id.gpuExternalPowerClickedValue);
        externalPowerOfClickedGPU.setText((CharSequence) gpuHashMapData.get("external-power"));

        //External Review of GPU:
        TextView externalReviewOfClickedGPU  = findViewById(R.id.gpuExternalReviewClickedValue);
        externalReviewOfClickedGPU.setText((CharSequence) gpuHashMapData.get("external-review"));

        //Add GPU Details to CreateComputerListActivity if add button is clicked:
        Button addGPUToListBtn = findViewById(R.id.addClickedGPUToListBtn);
        addGPUToListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pass the data back to the CreateComputerListActivity by storing the data in the DataStorage
                //hashmap and then returning to the CreateComputerListActivity:

                String gpuName = (String) gpuHashMapData.get("name");
                Double gpuPrice = (Double) gpuHashMapData.get("price");
                String gpuPriceString = Double.toString(gpuPrice);
                Long gpuTDP = (Long) gpuHashMapData.get("tdp");

                Intent passGPUDataToCreateComputerActivity = new Intent
                        (ViewGPUDetails.this, CreateComputerListActivity.class);

                gpuData.getComputerList().put("GPU NAME", gpuName);
                gpuData.getComputerList().put("GPU PRICE", gpuPriceString);
                gpuData.getComputerList().put("GPU TDP", String.valueOf(gpuTDP));
                Log.d(TAG, "onSuccess: " + gpuData.getComputerList().get("GPU NAME"));
                Log.d(TAG, "onSuccess: " + gpuData.getComputerList().get("GPU PRICE"));
                Log.d(TAG, "onSuccess: " + gpuData.getComputerList().get("GPU TDP"));

                startActivity(passGPUDataToCreateComputerActivity);

            }
        });












    }
}