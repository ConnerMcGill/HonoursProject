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
import java.util.HashMap;

public class CreateComputerListActivity extends AppCompatActivity implements View.OnClickListener {

    //Tag used for debugging and log comments
    private static final String TAG = "CreateComputerListActivity";


    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LIST_NAME = "list name";

    //DataStorage instance
    DataStorage computerComponentData = new DataStorage();

    //Setup view references:

    //EditText that lets the user enter a title for the list
    EditText enterTitleForPCList;
    String listTitleName;

    //CPU:
    TextView cpuNameField;
    TextView cpuPriceField;

    //CPU Cooler:
    TextView cpuCoolerNameField;
    TextView cpuCoolerPriceField;

    //Motherboard:
    TextView motherboardNameFiled;
    TextView motherboardPriceField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_computer_list);

        //Setup Toolbar elements
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Create List");


        //Random debug logs I will remove this later on
        Log.d(TAG, "conner: " + computerComponentData.getComputerList().get("CPU COOLER NAME"));
        Log.d(TAG, "conner: " + computerComponentData.getComputerList().get("CPU COOLER PRICE"));
        Log.d(TAG, "conner: " + computerComponentData.getComputerList().get("CPU NAME"));
        Log.d(TAG, "conner: " + computerComponentData.getComputerList().get("CPU PRICE"));
        Log.d(TAG, "conner: " + computerComponentData.getComputerList().keySet());
        Log.d(TAG, "conner: " + computerComponentData.getComputerList().values());


        //Retrieve reference to view elements:

        //EditText that lets user enter a title:
        enterTitleForPCList = findViewById(R.id.enterTitleForListEditText);

        //CPU References:
        cpuNameField = findViewById(R.id.nameOfSelectedCPU);
        cpuPriceField = findViewById(R.id.priceValueOfSelectedCPU);
        final Button selectCPU = findViewById(R.id.addCPUButton);
        selectCPU.setOnClickListener(this);

        //CPU Cooler References:
        cpuCoolerNameField = findViewById(R.id.nameOfSelectedCPUCooler);
        cpuCoolerPriceField = findViewById(R.id.priceValueOfSelectedCPUCooler);
        final Button selectCPUCooler = findViewById(R.id.addCPUCoolerButton);
        selectCPUCooler.setOnClickListener(this);

        //Motherboard References:
        motherboardNameFiled = findViewById(R.id.nameOfSelectedMotherboard);
        motherboardPriceField = findViewById(R.id.priceValueOfSelectedMotherboard);
        final Button selectMotherboard = findViewById(R.id.addMotherboardButton);
        selectMotherboard.setOnClickListener(this);

        //Retrieve relevant item data
        retrieveCPUData();
        retrieveCPUCoolerData();
        retrieveMotherboardData();

        loadData();
        updateViews();


    }


    //Take user to new activity or run method depending on which button was clicked
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addCPUButton:
                openSelectCPUOptions();
                break;
            case R.id.addCPUCoolerButton:
                openSelectCPUCoolerOptions();
                break;
            case R.id.addMotherboardButton:
                openSelectMotherboardOptions();
                break;
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

    private void openSelectMotherboardOptions() {
        Intent openSelectMotherboardOptions = new Intent(CreateComputerListActivity.this,
                SelectMotherboard.class);
        saveData();
        startActivity(openSelectMotherboardOptions);
    }

    private void retrieveCPUData() {

        //Set the fields with the relevant data
        cpuNameField.setText(computerComponentData.getComputerList().get("CPU NAME"));
        if (computerComponentData.getComputerList().get("CPU PRICE") == null) {
            cpuPriceField.setText("");
        } else {
            cpuPriceField.setText((String.format(getResources().getString(R.string.price_of_product), computerComponentData.getComputerList().get("CPU PRICE"))));
        }


        //String that is used to check if a product has been selected and to get the associated image for the product
        String cpuName = cpuNameField.getText().toString();


        //Image retrieval is wrapped in a try catch statement. If no product is selected then a IllegalArgumentException
        //is thrown so this prevents the app from crashing
        try {
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
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "retrieveCPUData: " + "something went wrong!");
        }

    }

    private void retrieveCPUCoolerData() {

        //Set the fields with the relevant data
        cpuCoolerNameField.setText(computerComponentData.getComputerList().get("CPU COOLER NAME"));
        if (computerComponentData.getComputerList().get("CPU COOLER PRICE") == null) {
            cpuCoolerPriceField.setText("");
        } else {
            cpuCoolerPriceField.setText((String.format(getResources().getString(R.string.price_of_product), computerComponentData.getComputerList().get("CPU COOLER PRICE"))));
        }

        //String that is used to check if a product has been selected and to get the associated image for the product
        String cpuCoolerName = cpuCoolerNameField.getText().toString();

        //Image retrieval is wrapped in a try catch statement. If no product is selected then a IllegalArgumentException
        //is thrown so this prevents the app from crashing
        try {
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
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "retrieveCPUCoolerData: " + "Something went wrong or the catch statement is working as intended");
        }

    }

    private void retrieveMotherboardData(){
        //Set the fields with the relevant data
        motherboardNameFiled.setText(computerComponentData.getComputerList().get("MOTHERBOARD NAME"));
        if (computerComponentData.getComputerList().get("MOTHERBOARD PRICE") == null) {
            motherboardPriceField.setText("");
        } else {
            motherboardPriceField.setText((String.format(getResources().getString(R.string.price_of_product), computerComponentData.getComputerList().get("MOTHERBOARD PRICE"))));
        }

        //String that is used to check if a product has been selected and to get the associated image for the product
        String motherboardName = motherboardNameFiled.getText().toString();

        //Image retrieval is wrapped in a try catch statement. If no product is selected then a IllegalArgumentException
        //is thrown so this prevents the app from crashing
        try {
            //Get the image for the selected CPU if there's a selected cpu:
            if (motherboardName != null) {

                //In order to get the image for the guide a storage reference needs to be created
                StorageReference storageReferenceCPUCooler = FirebaseStorage.getInstance().getReference().child(motherboardName + ".jpg");
                Log.d(TAG, "getting storage reference name " + storageReferenceCPUCooler);

                try {
                    //Create a placeholder that will store the image for the activity
                    final File tempFile = File.createTempFile(motherboardName, "jpg");
                    //Try to retrieve the image from the firestore cloud storage
                    storageReferenceCPUCooler.getFile(tempFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    //Temp toast message for testing
                                    //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                                    Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                                    ((ImageView) findViewById(R.id.imageOfSelectedMotherboard)).setImageBitmap(bitmap);
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
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "retrieveCPUCoolerData: " + "Something went wrong or the catch statement is working as intended");
        }


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