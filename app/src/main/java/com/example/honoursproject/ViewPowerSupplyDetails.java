/*
Honours Project - PC part Builder
File: ViewPowerSupplyDetails Class
Author: Conner McGill - B00320975
Date: 2021/03/08

Summary of file:

    This class assigns the HashMap data that is retrieved from the SelectPSU class and displays
    the detailed information in a nestedScrollView showing the extra details that the user would
    not see on the SelectPSU recycler view

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

public class ViewPowerSupplyDetails extends AppCompatActivity {

    //Tag used for debugging the firestore data retrieval
    private static final String TAG = "ViewPSUDetailsTAG";

    //DataStorage Instance:
    DataStorage psuData = new DataStorage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_power_supply_details);

        //Get all the data for the motherboard firestore document that was selected as a HashMap
        Intent intent = getIntent();
        final HashMap<String, Object> psuHashMapData = (HashMap<String, Object>) intent.getSerializableExtra("hashMap");
        //Randomly testing I can get some data here
        Log.d(TAG, "onCreate: " + psuHashMapData.get("name"));
        Log.d(TAG, "values: " + psuHashMapData.keySet());

        //Setup references to UI elements within the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("PSU Details:");

        //Go back to the previous activity in the activity backstack
        //https://stackoverflow.com/questions/49350686/back-to-previous-activity-arrow-button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                finish();
            }
        });

        //Get the image of the psu and set it to the imageView. First though store the name of the
        //psu which is the name of the image file in a String variable
        String psuImageName = (String) psuHashMapData.get("name");
        Log.d(TAG, "psu Name: " + psuImageName);

        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(psuImageName + ".jpg");
        Log.d("my psu image", String.valueOf(storageReference));

        try {
            //Create a placeholder that will store the image for the activity
            final File tempFile = File.createTempFile(psuImageName, "jpg");
            //Try to retrieve the image from the firestore cloud storage
            storageReference.getFile(tempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Temp toast message for testing
                            //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                            ((ImageView) findViewById(R.id.imageOfClickedPSU)).setImageBitmap(bitmap);
                            Log.d("image retrieved", "The image has been retrieved successfully. Which makes sense if you can actually see it");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //If the image can't be retrieved for whatever reason then display the toast as an error message and write a log statement to the console
                    Toast.makeText(ViewPowerSupplyDetails.this, "Image failed to be retrieved", Toast.LENGTH_SHORT).show();
                    Log.d("image not retrieved", "The image has failed to be retrieved successfully. Which makes sense as you can't see the image");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Assign the values into their respective text views:

        //Name of the psu:
        TextView nameOfClickedPSU = findViewById(R.id.nameOfClickedPSU);
        nameOfClickedPSU.setText((CharSequence) psuHashMapData.get("name"));

        //Price of the psu:
        TextView priceOfClickedPSU = findViewById(R.id.priceOfClickedPSU);
        Log.d(TAG, "onCreate: " + psuHashMapData.get("price"));
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String price = Double.toString((Double) psuHashMapData.get("price"));
        Log.d(TAG, "price: " + price);
        priceOfClickedPSU.setText("Price: £" + price);

        //Manufacturer of the psu:
        TextView manufacturerOfPSU = findViewById(R.id.psuManufacturerClickedValue);
        manufacturerOfPSU.setText((CharSequence) psuHashMapData.get("manufacturer"));

        //Form factor of psu:
        TextView formFactor = findViewById(R.id.psuFormFactorClickedValue);
        formFactor.setText((CharSequence) psuHashMapData.get("form-factor"));

        //Efficiency Rating of psu:
        TextView efficiencyRatingOfPSU = findViewById(R.id.psuEfficiencyRatingClickedValue);
        efficiencyRatingOfPSU.setText((CharSequence) psuHashMapData.get("efficiency-rating"));

        //Wattage of psu:
        TextView wattageOfPSU = findViewById(R.id.psuWattageClickedValue);
        //Convert the value from the HashMap into a string so that it can be assigned into the TextView
        String psuWattage = Long.toString((Long) psuHashMapData.get("wattage"));
        wattageOfPSU.setText(psuWattage + " W");

        //Length of psu:
        TextView lengthOfPSU = findViewById(R.id.psuLengthClickedValue);
        //Convert the value from the HashMap into a string so that it can be assigned into the TextView
        String length = Long.toString((Long) psuHashMapData.get("length"));
        lengthOfPSU.setText(length + " mm");

        //Modular of psu:
        TextView modularOfPSU = findViewById(R.id.psuModularClickedValue);
        modularOfPSU.setText((CharSequence) psuHashMapData.get("modular"));

        //Type of psu:
        TextView typeOfPSU = findViewById(R.id.psuTypeClickedValue);
        typeOfPSU.setText((CharSequence) psuHashMapData.get("type"));

        //Is psu fanless:
        TextView fanlessPSU = findViewById(R.id.psuFanlessClickedValue);
        fanlessPSU.setText((CharSequence) psuHashMapData.get("fanless"));

        //EPS Connectors of psu:
        TextView epsConnectorsOfPSU = findViewById(R.id.psuEPSConnectorsClickedValue);
        //Convert the value from the HashMap into a string so that it can be assigned into the TextView
        String epsConnectors = Long.toString((Long) psuHashMapData.get("eps-connectors"));
        epsConnectorsOfPSU.setText(epsConnectors);

        //PCIe 6+2 Pin Connectors of psu:
        TextView PCIe6Plus2PinConnectorsOfPSU = findViewById(R.id.psuPCIE6plus2PinConnectorsClickedValue);
        //Convert the value from the HashMap into a string so that it can be assigned into the TextView
        String pcie6plus2Connectors = Long.toString((Long) psuHashMapData.get("pcie-6-plus-2-pin-connectors"));
        PCIe6Plus2PinConnectorsOfPSU.setText(pcie6plus2Connectors);

        //Sata Connectors of psu:
        TextView sataConnectorsOfPSU = findViewById(R.id.psuSataConnectorsClickedValue);
        //Convert the value from the HashMap into a string so that it can be assigned into the TextView
        String sataConnectors = Long.toString((Long) psuHashMapData.get("sata-connectors"));
        sataConnectorsOfPSU.setText(sataConnectors);

        //Molex 4 Pin Connectors of psu:
        TextView Molex4PinConnectorsOfPSU = findViewById(R.id.psuMolex4PinConnectorClickedValue);
        //Convert the value from the HashMap into a string so that it can be assigned into the TextView
        String Molex4PinConnectors = Long.toString((Long) psuHashMapData.get("molex-4-pin-connectors"));
        Molex4PinConnectorsOfPSU.setText(Molex4PinConnectors);

        //external review of the psu:
        TextView externalReviewOfPSU = findViewById(R.id.psuExternalReviewClickedValue);
        externalReviewOfPSU.setText((CharSequence) psuHashMapData.get("external-review"));

        //Add psu Details to CreateComputerListActivity if add button is clicked:
        Button addPSUToListBtn = findViewById(R.id.addClickedPSUToListBtn);
        addPSUToListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pass the data back to the CreateComputerListActivity by storing the data in the DataStorage
                //hashmap and then returning to the CreateComputerListActivity:

                String psuName = (String) psuHashMapData.get("name");
                Double psuPrice = (Double) psuHashMapData.get("price");
                String psuPriceString = Double.toString(psuPrice);

                Intent passPSUDataToCreateComputerActivity = new Intent
                        (ViewPowerSupplyDetails.this, CreateComputerListActivity.class);

                psuData.getComputerList().put("PSU NAME", psuName);
                psuData.getComputerList().put("PSU PRICE", psuPriceString);
                Log.d(TAG, "onSuccess: " + psuData.getComputerList().get("PSU NAME"));
                Log.d(TAG, "onSuccess: " + psuData.getComputerList().get("PSU PRICE"));

                startActivity(passPSUDataToCreateComputerActivity);

            }
        });










    }
}