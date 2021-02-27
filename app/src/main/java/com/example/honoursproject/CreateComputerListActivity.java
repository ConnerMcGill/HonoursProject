/*
Honours Project - PC part Builder
File: CreateComputerListActivity Class
Author: Conner McGill - B00320975
Date: 2021/02/18

Summary of file:

    Update this later once I have the list sort of working right

 */

package com.example.honoursproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class CreateComputerListActivity extends AppCompatActivity implements View.OnClickListener {

    //Tag used for debugging and log comments
    private static final String TAG = "CreateComputerListActivity";


    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LIST_NAME = "list name";

    //EditText that lets the user enter a title for the list
    EditText enterTitleForPCList;
    String listTitleName;

    //I am going to need to use sharedPreferences in order to save the UI state when switching
    //between activities as I ain't gonna be able to do it with savedInstances due to the
    //way the android lifecycle works

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_computer_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Create List");

        final Button selectCPU = findViewById(R.id.addCPUButton);
        selectCPU.setOnClickListener(this);

        final Button selectCPUCooler = findViewById(R.id.addCPUCoolerButton);
        selectCPUCooler.setOnClickListener(this);

        //EditText that lets user enter a title:
        enterTitleForPCList = findViewById(R.id.enterTitleForListEditText);


        //I will need to clean this part up into its own functions
        //and find a way to pull the data from that storage thing I forgot the name of it


        //Get CPU Data from intent
        String cpuName = getIntent().getStringExtra("CPU NAME");
        Log.d(TAG, "is cpu name null: " + cpuName);
        String cpuPrice = getIntent().getStringExtra("CPU PRICE");
        String cpuSocket = getIntent().getStringExtra("CPU SOCKET");
        Log.d(TAG, "CPU Socket Name: " + cpuSocket);


        //Setup relevant cpu interface fields
        TextView cpuNameField = findViewById(R.id.nameOfSelectedCPU);
        TextView cpuPriceField = findViewById(R.id.priceValueOfSelectedCPU);

        //Assign CPU data to fields:
        cpuNameField.setText(cpuName);

        /*
            If I don't wrap this in a if statement and have the "Price: " concatenated at the start
            then the view shows "Price: Null" instead of nothing at first which looks out of place
            in my opinion
        */

        if (cpuPrice == null) {
            cpuPriceField.setText(cpuPrice);
        } else {
            cpuPriceField.setText(String.format(getResources().getString(R.string.price_of_product), cpuPrice));
        }

        //Get the image for the selected CPU if there's a selected cpu:
        if (cpuName != null) {

            //In order to get the image for the guide a storage reference needs to be created
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(cpuName + ".jpg");
            Log.d(TAG, "getting storage reference name " + storageReference);

            try {
                //Create a placeholder that will store the image for the activity
                final File tempFile = File.createTempFile(cpuName, "jpg");
                //Try to retrieve the image from the firestore cloud storage
                storageReference.getFile(tempFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                //Temp toast message for testing
                                //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                                Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                                ((ImageView) findViewById(R.id.imageOfSelectedCPU)).setImageBitmap(bitmap);
                                Log.d("image retrieved", "The image has been retrieved successfully. Which makes sense if you can actually see it");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //If the image can't be retrieved for whatever reason then display the toast as an error message and write a log statement to the console
                        Toast.makeText(CreateComputerListActivity.this, "Image failed to be retrieved", Toast.LENGTH_SHORT).show();
                        Log.d("image not retrieved", "The image has failed to be retrieved successfully. Which makes sense as you can't see the image");
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Get CPU Cooler Data from intent
        String cpuCoolerName = getIntent().getStringExtra("CPU COOLER NAME");
        Log.d(TAG, "is cpu cooler name null: " + cpuCoolerName);
        String cpuCoolerPrice = getIntent().getStringExtra("CPU COOLER PRICE");

        //Setup relevant cpu interface fields
        TextView cpuCoolerNameField = findViewById(R.id.nameOfSelectedCPUCooler);
        TextView cpuCoolerPriceField = findViewById(R.id.priceValueOfSelectedCPUCooler);

        //Assign CPU Cooler data to fields:
        cpuCoolerNameField.setText(cpuCoolerName);

            /*
            If I don't wrap this in a if statement and have the "Price: " concatenated at the start
            then the view shows "Price: Null" instead of nothing at first which looks out of place
            in my opinion
            */

        if (cpuCoolerPrice == null) {
            cpuCoolerPriceField.setText(cpuCoolerPrice);
        } else {
            cpuCoolerPriceField.setText(String.format(getResources().getString(R.string.price_of_product), cpuCoolerPrice));
        }

        //Get the image for the selected CPU if there's a selected cpu:
        if (cpuCoolerName != null) {

            //In order to get the image for the guide a storage reference needs to be created
            StorageReference storageReferenceCPUCooler = FirebaseStorage.getInstance().getReference().child(cpuCoolerName + ".jpg");
            Log.d(TAG, "getting storage reference name " + storageReferenceCPUCooler);

            try {
                //Create a placeholder that will store the image for the activity
                final File tempFile = File.createTempFile(cpuCoolerName, "jpg");
                //Try to retrieve the image from the firestore cloud storage
                storageReferenceCPUCooler.getFile(tempFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                //Temp toast message for testing
                                //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                                Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                                ((ImageView) findViewById(R.id.imageOfSelectedCPUCooler)).setImageBitmap(bitmap);
                                Log.d("image retrieved", "The image has been retrieved successfully. Which makes sense if you can actually see it");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //If the image can't be retrieved for whatever reason then display the toast as an error message and write a log statement to the console
                        Toast.makeText(CreateComputerListActivity.this, "Image failed to be retrieved", Toast.LENGTH_SHORT).show();
                        Log.d("image not retrieved", "The image has failed to be retrieved successfully. Which makes sense as you can't see the image");
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        loadData();
        updateViews();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addCPUButton:
                openSelectCPUOptions();
                break;
            case R.id.addCPUCoolerButton:
                openSelectCPUCoolerOptions();
        }
    }


    private void openSelectCPUOptions() {
        Intent openSelectCPUOptions = new Intent(CreateComputerListActivity.this,
                Selectcpu.class);
        saveData();
        startActivity(openSelectCPUOptions);
    }

    private void openSelectCPUCoolerOptions() {
        Intent openSelectCPUCoolerOptions = new Intent(CreateComputerListActivity.this,
                SelectCPUCooler.class);
        saveData();
        startActivity(openSelectCPUCoolerOptions);
    }


    public void saveData() {
        listTitleName = enterTitleForPCList.getText().toString();


        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(LIST_NAME, listTitleName);

        editor.apply();

        Toast.makeText(this, "data saved debug msg", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        listTitleName = sharedPreferences.getString(LIST_NAME, "");
    }

    public void updateViews() {
        enterTitleForPCList.setText(listTitleName);
    }

}