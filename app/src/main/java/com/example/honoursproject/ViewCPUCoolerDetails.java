/*
Honours Project - PC part Builder
File: Selectcpu Class
Author: Conner McGill - B00320975
Date: 2021/02/26

Summary of file:

    This class assigns the HashMap data that is retrieved from the SelectCPUCooler class and displays
    the detailed information in a nestedScrollView showing the extra details that the user would
    not see on the SelectCPUCooler recycler view

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

public class ViewCPUCoolerDetails extends AppCompatActivity {

    //Tag used for debugging the firestore data retrieval
    private static final String TAG = "ViewCPUCoolerDetailsTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cpu_cooler_details);

        //Get all the data for the CPU firestore document that was selected as a HashMap
        Intent intent = getIntent();
        final HashMap<String, Object> cpuCoolerHashMapData = (HashMap<String, Object>) intent.getSerializableExtra("hashMap");
        //Randomly testing I can get some data here
        Log.d(TAG, "onCreate: " + cpuCoolerHashMapData.get("socket"));
        Log.d(TAG, "values: " + cpuCoolerHashMapData.keySet());

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


        //Get the image of the CPU Cooler and set it to the imageView. First though store the name of the
        //cpu cooler which is the name of the image file in a String variable
        String cpuCoolerImageName = (String) cpuCoolerHashMapData.get("name");
        Log.d(TAG, "CPU Cooler Name: " + cpuCoolerImageName);

        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(cpuCoolerImageName + ".jpg");
        Log.d("my cpu cooler image", String.valueOf(storageReference));

        try {
            //Create a placeholder that will store the image for the activity
            final File tempFile = File.createTempFile(cpuCoolerImageName, "jpg");
            //Try to retrieve the image from the firestore cloud storage
            storageReference.getFile(tempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Temp toast message for testing
                            //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                            ((ImageView) findViewById(R.id.imageOfClickedCPUCooler)).setImageBitmap(bitmap);
                            Log.d("image retrieved", "The image has been retrieved successfully. Which makes sense if you can actually see it");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //If the image can't be retrieved for whatever reason then display the toast as an error message and write a log statement to the console
                    Toast.makeText(ViewCPUCoolerDetails.this, "Image failed to be retrieved", Toast.LENGTH_SHORT).show();
                    Log.d("image not retrieved", "The image has failed to be retrieved successfully. Which makes sense as you can't see the image");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Assign the values into their respective text views:

        //Name of the CPU Cooler:
        TextView titleOfClickedCPUCooler = findViewById(R.id.nameOfClickCPUCooler);
        titleOfClickedCPUCooler.setText((CharSequence) cpuCoolerHashMapData.get("name"));

        //Price of the CPU Cooler:
        TextView priceOfClickedCPUCooler = findViewById(R.id.priceOfClickedCPUCooler);
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String price = Double.toString((Double) cpuCoolerHashMapData.get("price"));
        Log.d(TAG, "price: " + price);
        priceOfClickedCPUCooler.setText("Price: £" + price);

        //Manufacturer of the CPU Cooler:
        TextView manufacturerOfCPUCooler = findViewById(R.id.cpuCoolerManufacturerClickedValue);
        manufacturerOfCPUCooler.setText((CharSequence) cpuCoolerHashMapData.get("manufacturer"));

        //Fan-RPM of the CPU Cooler
        TextView fanRpmOfCPUCooler = findViewById(R.id.fanRpmOfCPUCoolerClickedValue);
        fanRpmOfCPUCooler.setText((CharSequence) cpuCoolerHashMapData.get("fan-rpm"));

        //Noise Level of the CPU Cooler
        TextView noiseLevelOfCPUCooler = findViewById(R.id.noiseLevelOfCPUCoolerClickedValue);
        noiseLevelOfCPUCooler.setText((CharSequence) cpuCoolerHashMapData.get("noise-level"));

        //Height of the CPU Cooler
        TextView heightOfCPUCooler = findViewById(R.id.heightOfClickedCPUCoolerValue);
        //Convert the height of the CPU Cooler hashmap data from a long or double into a string and assign
        //it to the textview
        try {
            Long cpuCoolerHeightLong = (Long) cpuCoolerHashMapData.get("height");
            String cpuCoolerHeightString = Double.toString(cpuCoolerHeightLong);
            heightOfCPUCooler.setText(cpuCoolerHeightString + " mm");
        }catch (ClassCastException e){
            Double cpuCoolerHeightLong = (Double) cpuCoolerHashMapData.get("height");
            String cpuCoolerHeightString = Double.toString(cpuCoolerHeightLong);
            heightOfCPUCooler.setText(cpuCoolerHeightString + " mm");
        }

        //CPU Socket of the CPU Cooler
        ArrayList<String> socket = (ArrayList<String>) cpuCoolerHashMapData.get("socket");
        TextView cpuSocketOfCPUCooler = findViewById(R.id.cpuSocketsForCPUCoolerClickedValue);
        //Loop through the arrayList and assign the appropriate cpu sockets for that part
        //Since the size will be dynamic for each part it would make more sense to use a loop
        //rather than assigning all the data individually
        String s = "";
        for (int i = 0; i < socket.size(); i++){
            s += "• " + socket.get(i) + "\n";
        }
        cpuSocketOfCPUCooler.setText(s);
        Log.d(TAG, "onCreate: " + s);

        //Is the CPU Cooler watercooled
        TextView cpuCoolerWaterCooling = findViewById(R.id.waterCooledForCPUCoolerClickedValue);
        cpuCoolerWaterCooling.setText((CharSequence) cpuCoolerHashMapData.get("water-cooled"));

        //Is the CPU Cooler fanless
        TextView cpuCoolerFanless = findViewById(R.id.cpuCoolerClickedFanlessValue);
        cpuCoolerFanless.setText((CharSequence) cpuCoolerHashMapData.get("water-cooled"));

        //External Review of CPU:
        TextView externalReviewOfCPUCooler = findViewById(R.id.cpuCoolerExternalReviewClickedValue);
        externalReviewOfCPUCooler.setText((CharSequence) cpuCoolerHashMapData.get("external-review"));

        //Add CPU Details to CreateComputerListActivity if add button is clicked:
        Button addCPUCoolerToListBtn = findViewById(R.id.addClickedCPUCoolerToListBtn);
        addCPUCoolerToListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pass the data back to the CreateComputerListActivity:

                String cpuSocketName = (String) cpuCoolerHashMapData.get("name");
                Double cpuPriceDouble = (Double) cpuCoolerHashMapData.get("price");
                String cpuSocketPrice = Double.toString(cpuPriceDouble);

                Intent passCPUCoolerDataToCreateComputerActivity = new Intent
                        (ViewCPUCoolerDetails.this, CreateComputerListActivity.class);
                passCPUCoolerDataToCreateComputerActivity.putExtra("CPU COOLER NAME", cpuSocketName);
                passCPUCoolerDataToCreateComputerActivity.putExtra("CPU COOLER PRICE", cpuSocketPrice);
                startActivity(passCPUCoolerDataToCreateComputerActivity);

            }
        });

    }
}