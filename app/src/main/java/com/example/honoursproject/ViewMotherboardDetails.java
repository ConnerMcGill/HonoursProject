/*
Honours Project - PC part Builder
File: ViewMotherboardDetails Class
Author: Conner McGill - B00320975
Date: 2021/03/04

Summary of file:

    This class assigns the HashMap data that is retrieved from the SelectMotherboard class and displays
    the detailed information in a nestedScrollView showing the extra details that the user would
    not see on the SelectMotherboard recycler view

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
import java.util.ArrayList;
import java.util.HashMap;

public class ViewMotherboardDetails extends AppCompatActivity {

    //Tag used for debugging the firestore data retrieval
    private static final String TAG = "ViewMotherboardDetailsTAG";

    //DataStorage Instance:
    DataStorage motherboardData = new DataStorage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_motherboard_details);

        //Get all the data for the motherboard firestore document that was selected as a HashMap
        Intent intent = getIntent();
        final HashMap<String, Object> motherboardHashMapData = (HashMap<String, Object>) intent.getSerializableExtra("hashMap");
        //Randomly testing I can get some data here
        Log.d(TAG, "onCreate: " + motherboardHashMapData.get("socket"));
        Log.d(TAG, "values: " + motherboardHashMapData.keySet());

        //Setup references to UI elements within the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Motherboard Details:");

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
        //motherboard which is the name of the image file in a String variable
        String motherboardImageName = (String) motherboardHashMapData.get("name");
        Log.d(TAG, "motherboard Name: " + motherboardImageName);

        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(motherboardImageName + ".jpg");
        Log.d("my motherboard image", String.valueOf(storageReference));

        try {
            //Create a placeholder that will store the image for the activity
            final File tempFile = File.createTempFile(motherboardImageName, "jpg");
            //Try to retrieve the image from the firestore cloud storage
            storageReference.getFile(tempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Temp toast message for testing
                            //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                            ((ImageView) findViewById(R.id.imageOfCLickedMotherboard)).setImageBitmap(bitmap);
                            Log.d("image retrieved", "The image has been retrieved successfully. Which makes sense if you can actually see it");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //If the image can't be retrieved for whatever reason then display the toast as an error message and write a log statement to the console
                    Toast.makeText(ViewMotherboardDetails.this, "Image failed to be retrieved", Toast.LENGTH_SHORT).show();
                    Log.d("image not retrieved", "The image has failed to be retrieved successfully. Which makes sense as you can't see the image");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Assign the values into their respective text views:

        //Name of the motherboard:
        TextView titleOfClickedMotherboard = findViewById(R.id.nameOfClickedMotherboard);
        titleOfClickedMotherboard.setText((CharSequence) motherboardHashMapData.get("name"));

        //Price of the motherboard:
        TextView priceOfClickedMotherboard = findViewById(R.id.priceOfClickedMotherboard);
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String price = Double.toString((Double) motherboardHashMapData.get("price"));
        Log.d(TAG, "price: " + price);
        priceOfClickedMotherboard.setText("Price: £" + price);

        //Manufacturer of the motherboard:
        TextView manufacturerOfMotherboard = findViewById(R.id.motherboardManufacturerClickedValue);
        manufacturerOfMotherboard.setText((CharSequence) motherboardHashMapData.get("manufacturer"));

        //Socket for the motherboard:
        TextView socketForMotherboard = findViewById(R.id.motherboardSocketClickedValue);
        socketForMotherboard.setText((CharSequence) motherboardHashMapData.get("socket"));

        //Form Factor for the motherboard:
        TextView formFactorOfMotherboard = findViewById(R.id.motherboardFormFactorClickedValue);
        formFactorOfMotherboard.setText((CharSequence) motherboardHashMapData.get("form-factor"));

        //Chipset for the motherboard:
        TextView chipsetOfMotherboard = findViewById(R.id.motherboardChipsetClickedValue);
        chipsetOfMotherboard.setText((CharSequence) motherboardHashMapData.get("chipset"));

        //Max Memory of motherboard:
        TextView maxMemoryOfMotherboard = findViewById(R.id.motherboardMemoryMaxClickedValue);
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String maxMemory = Long.toString((Long) motherboardHashMapData.get("max-memory"));
        maxMemoryOfMotherboard.setText(maxMemory + " GB");

        //Number of memory slots on motherboard:
        TextView numberOfMemorySlotsOnMotherboard = findViewById(R.id.motherboardMemorySlotsClickedValue);
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String memorySlots = Long.toString((Long) motherboardHashMapData.get("memory-slots"));
        maxMemoryOfMotherboard.setText(memorySlots);

        //Memory speed support of motherboard
        ArrayList<String> memorySupport = (ArrayList<String>) motherboardHashMapData.get("memory-speed");
        TextView memorySpeedOfMotherboard = findViewById(R.id.motherboardMemorySpeedClickedValue);
        //Loop through the arrayList and assign the appropriate cpu sockets for that part
        //Since the size will be dynamic for each part it would make more sense to use a loop
        //rather than assigning all the data individually
        String ms = "";
        for (int i = 0; i < memorySupport.size(); i++){
            ms += "• " + memorySupport.get(i) + "\n";
        }
        memorySpeedOfMotherboard.setText(ms);
        Log.d(TAG, "onCreate: " + ms);

        //Colour of the motherboard:
        TextView colourOfMotherboard = findViewById(R.id.motherboardColourClickedValue);
        colourOfMotherboard.setText((CharSequence) motherboardHashMapData.get("colour"));

        //SLI/CrossFire Support of motherboard:
        ArrayList<String> sliCrossfireSupport = (ArrayList<String>) motherboardHashMapData.get("sli-crossfire");
        TextView sliSupportOfMotherboard = findViewById(R.id.motherboardSliClickedValue);
        //Loop through the arrayList and assign the appropriate cpu sockets for that part
        //Since the size will be dynamic for each part it would make more sense to use a loop
        //rather than assigning all the data individually
        String sli = "";
        for (int i = 0; i < sliCrossfireSupport.size(); i++){
            sli += "• " + sliCrossfireSupport.get(i) + "\n";
        }
        sliSupportOfMotherboard.setText(sli);
        Log.d(TAG, "onCreate: " + sli);

        //Number of PCI-E x16 Slots on motherboard:
        TextView numberOfPCIEx16SlotsOnMotherboard = findViewById(R.id.motherboardPCIEX16SlotsClickedValue);
        //Convert the value from the HashMap into a string so that it can be assigned into the TextView
        String pciex16SlotsNum = Long.toString((Long) motherboardHashMapData.get("pci-e-x16-slots"));
        numberOfPCIEx16SlotsOnMotherboard.setText(pciex16SlotsNum);

        //Number of PCI-E x8 Slots on motherboard:
        TextView numberOfPCIEx8SlotsOnMotherboard = findViewById(R.id.motherboardPCIEX8SlotsClickedValue);
        //Convert the value from the HashMap into a string so that it can be assigned into the TextView
        String pciex8SlotsNum = Long.toString((Long) motherboardHashMapData.get("pci-e-x8-slots"));
        numberOfPCIEx8SlotsOnMotherboard.setText(pciex8SlotsNum);

        //Number of PCI-E x4 Slots on motherboard:
        TextView numberOfPCIEx4SlotsOnMotherboard = findViewById(R.id.motherboardPCIEX4SlotsClickedValue);
        //Convert the value from the HashMap into a string so that it can be assigned into the TextView
        String pciex4SlotsNum = Long.toString((Long) motherboardHashMapData.get("pci-e-x4-slots"));
        numberOfPCIEx4SlotsOnMotherboard.setText(pciex4SlotsNum);

        //Number of PCI-E x1 Slots on motherboard:
        TextView numberOfPCIEx1SlotsOnMotherboard = findViewById(R.id.motherboardPCIEX1SlotsClickedValue);
        //Convert the value from the HashMap into a string so that it can be assigned into the TextView
        String pciex1SlotsNum = Long.toString((Long) motherboardHashMapData.get("pci-e-x1-slots"));
        numberOfPCIEx1SlotsOnMotherboard.setText(pciex1SlotsNum);

        //Number of PCI-E Slots on motherboard:
        TextView numberOfPCIESlotsOnMotherboard = findViewById(R.id.motherboardPCIESlotsClickedValue);
        //Convert the value from the HashMap into a string so that it can be assigned into the TextView
        String pcieSlotsNum = Long.toString((Long) motherboardHashMapData.get("pci-slots"));
        numberOfPCIESlotsOnMotherboard.setText(pcieSlotsNum);

        //M.2 Slots of motherboard
        ArrayList<String> m2Slots = (ArrayList<String>) motherboardHashMapData.get("m2-slots");
        TextView m2SlotsOfMotherboard = findViewById(R.id.motherboardM2SlotsClickedValue);
        //Loop through the arrayList and assign the appropriate cpu sockets for that part
        //Since the size will be dynamic for each part it would make more sense to use a loop
        //rather than assigning all the data individually
        String m2s = "";
        for (int i = 0; i < m2Slots.size(); i++){
            m2s += "• " + m2Slots.get(i) + "\n";
        }
        m2SlotsOfMotherboard.setText(m2s);
        Log.d(TAG, "onCreate: " + m2s);

        //mSata Slots on motherboard:
        TextView numberOfmSataSlotsOnMotherboard = findViewById(R.id.motherboardmSataSlotsClickedValue);
        //Convert the value from the HashMap into a string so that it can be assigned into the TextView
        String mSataSlots = Long.toString((Long) motherboardHashMapData.get("msata-slots"));
        numberOfmSataSlotsOnMotherboard.setText(mSataSlots);

        //onboard ethernet of motherboard
        ArrayList<String> onboardEthernet = (ArrayList<String>) motherboardHashMapData.get("onboard-ethernet");
        TextView onboardEthernetOfMotherboard = findViewById(R.id.motherboardOnboardEthernetClickedValue);
        //Loop through the arrayList and assign the appropriate cpu sockets for that part
        //Since the size will be dynamic for each part it would make more sense to use a loop
        //rather than assigning all the data individually
        String oe = "";
        for (int i = 0; i < onboardEthernet.size(); i++){
            oe += "• " + onboardEthernet.get(i) + "\n";
        }
        m2SlotsOfMotherboard.setText(oe);
        Log.d(TAG, "onCreate: " + oe);

        //Sata 6Gbs Slots on motherboard:
        TextView numberOfSataSlotsOnMotherboard = findViewById(R.id.motherboardSataSixGBSClickedValue);
        //Convert the value from the HashMap into a string so that it can be assigned into the TextView
        String sataSlots = Long.toString((Long) motherboardHashMapData.get("sata-6-gbs"));
        numberOfmSataSlotsOnMotherboard.setText(sataSlots);

        //Onboard video of the motherboard:
        TextView onboardVideoOfMotheboard = findViewById(R.id.motherboardOnboardVideoClickedValue);
        onboardVideoOfMotheboard.setText((CharSequence) motherboardHashMapData.get("onboard-video"));

        //USB 2.0 Slots on motherboard:
        TextView usb2dot0SlotsOnMotherboard = findViewById(R.id.motherboardUSBGen2HeadersClickedValue);
        //Convert the value from the HashMap into a string so that it can be assigned into the TextView
        String usbGen2Headers = Long.toString((Long) motherboardHashMapData.get("usb-2-0-headers"));
        usb2dot0SlotsOnMotherboard.setText(usbGen2Headers);

        //USB 3.2 Gen 1 Slots on motherboard:
        TextView usb3dot2Gen1SlotsOnMotherboard = findViewById(R.id.motherboardUSB3_2Gen1HeadersClickedValue);
        //Convert the value from the HashMap into a string so that it can be assigned into the TextView
        String usbGen3dot2dot1Headers = Long.toString((Long) motherboardHashMapData.get("usb-3-2-gen-1-headers"));
        usb3dot2Gen1SlotsOnMotherboard.setText(usbGen3dot2dot1Headers);

        //USB 3.2 Gen 2 Slots on motherboard:
        TextView usb3dot2Gen2SlotsOnMotherboard = findViewById(R.id.motherboardUSB3_2Gen2HeadersClickedValue);
        //Convert the value from the HashMap into a string so that it can be assigned into the TextView
        String usbGen3dot2dot2Headers = Long.toString((Long) motherboardHashMapData.get("usb-3-2-gen-2-headers"));
        usb3dot2Gen2SlotsOnMotherboard.setText(usbGen3dot2dot2Headers);

        //USB 3.2 Gen 2x2 Slots on motherboard:
        TextView usb3dot2Gen2x2SlotsOnMotherboard = findViewById(R.id.motherboardUSB3_2Gen2x2HeadersClickedValue);
        //Convert the value from the HashMap into a string so that it can be assigned into the TextView
        String usbGen3dot2dot2x2Headers = Long.toString((Long) motherboardHashMapData.get("usb-3-2-gen-2x2-headers"));
        usb3dot2Gen2x2SlotsOnMotherboard.setText(usbGen3dot2dot2x2Headers);

        //Wireless networking of the motherboard:
        TextView wirelessNetworkingOfMotherboard = findViewById(R.id.motherboardWirelessNetworkingClickedValue);
        wirelessNetworkingOfMotherboard.setText((CharSequence) motherboardHashMapData.get("wireless-networking"));

        //Raid Support of the motherboard:
        TextView raidSupportOfMotherboard = findViewById(R.id.motherboardRaidSupportClickedValue);
        raidSupportOfMotherboard.setText((CharSequence) motherboardHashMapData.get("raid-support"));

        //External Review of the motherboard
        TextView externalReviewOfMotherboard = findViewById(R.id.motherboardExternalReviewClickedValue);
        externalReviewOfMotherboard.setText((CharSequence) motherboardHashMapData.get("external-review"));

        //Add Motherboard Details to CreateComputerListActivity if add button is clicked:
        Button addMotherboardToListBtn = findViewById(R.id.addClickedMotherboardToListBtn);
        addMotherboardToListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pass the data back to the CreateComputerListActivity by storing the data in the DataStorage
                //hashmap and then returning to the CreateComputerListActivity:

                String motherboardName = (String) motherboardHashMapData.get("name");
                Double motherboardPrice = (Double) motherboardHashMapData.get("price");
                String motherboardPriceString = Double.toString(motherboardPrice);
                Long motherboardTDP = (Long) motherboardHashMapData.get("tdp");

                Intent passMotherboardDataToCreateComputerActivity = new Intent
                        (ViewMotherboardDetails.this, CreateComputerListActivity.class);

                motherboardData.getComputerList().put("MOTHERBOARD NAME", motherboardName);
                motherboardData.getComputerList().put("MOTHERBOARD PRICE", motherboardPriceString);
                motherboardData.getComputerList().put("MOTHERBOARD TDP", String.valueOf(motherboardTDP));
                Log.d(TAG, "onSuccess: " + motherboardData.getComputerList().get("MOTHERBOARD NAME"));
                Log.d(TAG, "onSuccess: " + motherboardData.getComputerList().get("MOTHERBOARD PRICE"));
                Log.d(TAG, "onSuccess: " + motherboardData.getComputerList().get("MOTHERBOARD TDP"));



                startActivity(passMotherboardDataToCreateComputerActivity);

            }
        });

    }
}