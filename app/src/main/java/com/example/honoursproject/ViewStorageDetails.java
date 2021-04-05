/*
Honours Project - PC part Builder
File: ViewStorageDetails Class
Author: Conner McGill - B00320975
Date: 2021/03/05

Summary of file:

    This class assigns the HashMap data that is retrieved from the SelectStorage class and displays
    the detailed information in a nestedScrollView showing the extra details that the user would
    not see on the SelectStorage recycler view

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

public class ViewStorageDetails extends AppCompatActivity {

    //Tag used for debugging the firestore data retrieval
    private static final String TAG = "ViewStorageDetailsTAG";

    //DataStorage Instance:
    DataStorage storageData = new DataStorage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_storage_details);

        //Get all the data for the storage firestore document that was selected as a HashMap
        Intent intent = getIntent();
        final HashMap<String, Object> storageHashMapData = (HashMap<String, Object>) intent.getSerializableExtra("hashMap");
        //Randomly testing I can get some data here
        Log.d(TAG, "onCreate: " + storageHashMapData.get("name"));
        Log.d(TAG, "values: " + storageHashMapData.keySet());

        //Setup references to UI elements within the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Storage Details:");

        //Go back to the previous activity in the activity backstack
        //https://stackoverflow.com/questions/49350686/back-to-previous-activity-arrow-button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                finish();
            }
        });

        //Get the image of the storage and set it to the imageView. First though store the name of the
        //storage which is the name of the image file in a String variable
        String storageImageName = (String) storageHashMapData.get("name");
        Log.d(TAG, "storage Name: " + storageImageName);

        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(storageImageName + ".jpg");
        Log.d("my storage image", String.valueOf(storageReference));

        try {
            //Create a placeholder that will store the image for the activity
            final File tempFile = File.createTempFile(storageImageName, "jpg");
            //Try to retrieve the image from the firestore cloud storage
            storageReference.getFile(tempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Temp toast message for testing
                            //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                            ((ImageView) findViewById(R.id.imageOfCLickedStorage)).setImageBitmap(bitmap);
                            Log.d("image retrieved", "The image has been retrieved successfully. Which makes sense if you can actually see it");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //If the image can't be retrieved for whatever reason then display the toast as an error message and write a log statement to the console
                    Toast.makeText(ViewStorageDetails.this, "Image failed to be retrieved", Toast.LENGTH_SHORT).show();
                    Log.d("image not retrieved", "The image has failed to be retrieved successfully. Which makes sense as you can't see the image");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Assign the values into their respective text views:

        //Name of the storage:
        TextView titleOfClickedStorage = findViewById(R.id.nameOfClickedStorage);
        titleOfClickedStorage.setText((CharSequence) storageHashMapData.get("name"));

        //Price of the storage:
        TextView priceOfClickedStorage = findViewById(R.id.priceOfClickedStorage);
        Log.d(TAG, "onCreate: " + storageHashMapData.get("price"));
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String price = Double.toString((Double) storageHashMapData.get("price"));
        Log.d(TAG, "price: " + price);
        priceOfClickedStorage.setText("Price: £" + price);

        //Manufacturer of the storage:
        TextView manufacturerOfStorage = findViewById(R.id.storageManufacturerClickedValue);
        manufacturerOfStorage.setText((CharSequence) storageHashMapData.get("manufacturer"));

        //Capacity of the storage:
        TextView capacityOfStorage = findViewById(R.id.storageCapacityClickedValue);
        capacityOfStorage.setText((CharSequence) storageHashMapData.get("capacity"));

        //Price Per GB of the storage:
        TextView pricePerGBOfClickedStorage = findViewById(R.id.storagePricePerGBTitleClickedValue);
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String pricePerGB = Double.toString((Double) storageHashMapData.get("price-per-gb"));
        Log.d(TAG, "price per GB: " + pricePerGB);
        pricePerGBOfClickedStorage.setText("£" + pricePerGB);

        //type of the storage:
        TextView typeOfStorage = findViewById(R.id.storageTypeClickedValue);
        typeOfStorage.setText((CharSequence) storageHashMapData.get("type"));

        //cache of the storage:
        TextView cacheOfStorage = findViewById(R.id.storageCacheClickedValue);
        cacheOfStorage.setText((CharSequence) storageHashMapData.get("cache"));

        //form factor of the storage:
        TextView formFactorOfStorage = findViewById(R.id.storageFormFactorClickedValue);
        formFactorOfStorage.setText((CharSequence) storageHashMapData.get("form-factor"));

        //interface of the storage:
        TextView interfaceOfStorage = findViewById(R.id.storageInterfaceClickedValue);
        interfaceOfStorage.setText((CharSequence) storageHashMapData.get("interface"));

        //external review of the storage:
        TextView externalReviewOfStorage = findViewById(R.id.storageExternalReviewClickedValue);
        externalReviewOfStorage.setText((CharSequence) storageHashMapData.get("external-review"));

        //Add Storage Details to CreateComputerListActivity if add button is clicked:
        Button addStorageToListBtn = findViewById(R.id.addClickedStorageToListBtn);
        addStorageToListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pass the data back to the CreateComputerListActivity by storing the data in the DataStorage
                //hashmap and then returning to the CreateComputerListActivity:

                String storageName = (String) storageHashMapData.get("name");
                Double storagePrice = (Double) storageHashMapData.get("price");
                String storagePriceString = Double.toString(storagePrice);

                Intent passStorageDataToCreateComputerActivity = new Intent
                        (ViewStorageDetails.this, CreateComputerListActivity.class);

                storageData.getComputerList().put("STORAGE NAME", storageName);
                storageData.getComputerList().put("STORAGE PRICE", storagePriceString);
                Log.d(TAG, "onSuccess: " + storageData.getComputerList().get("STORAGE NAME"));
                Log.d(TAG, "onSuccess: " + storageData.getComputerList().get("STORAGE PRICE"));

                startActivity(passStorageDataToCreateComputerActivity);

            }
        });

    }
}