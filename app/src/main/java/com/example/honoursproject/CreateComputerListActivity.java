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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class CreateComputerListActivity extends AppCompatActivity implements View.OnClickListener {

    //Tag used for debugging and log comments
    private static final String TAG = "CreateComputerListActivity";

    //Declare an instance of firebaseAuthentication
    private FirebaseAuth mAuth;

    //Reference to firestore database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


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

    //Memory:
    TextView memoryNameField;
    TextView memoryPriceField;

    //Storage:
    TextView storageNameField;
    TextView storagePriceField;

    //GPU:
    TextView gpuNameField;
    TextView gpuPriceField;

    //Case:
    TextView caseNameField;
    TextView casePriceField;

    //PSU:
    TextView psuNameField;
    TextView psuPriceField;

    //Estimated Price and Wattage
    TextView estimatedPriceView;
    Double estimatedPrice = 0.00;
    String estimatedPriceString;
    TextView estimatedWattageView;
    int estimatedWattage = 0;
    String estimatedWattageString;

    //Description box and String for value
    EditText listDescription;
    String listDescriptionString;

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

        //Retrieve reference to view elements:

        //EditText that lets user enter a title:
        enterTitleForPCList = findViewById(R.id.enterTitleForListEditText);

        //EditText that lets user enter a description:
        listDescription = findViewById(R.id.userDescriptionForList);

        //CPU References:
        cpuNameField = findViewById(R.id.nameOfSelectedCPU);
        cpuPriceField = findViewById(R.id.priceValueOfSelectedCPU);
        final Button selectCPU = findViewById(R.id.addCPUButton);
        selectCPU.setOnClickListener(this);
        final Button removeCPU = findViewById(R.id.removeCPUButton);
        removeCPU.setOnClickListener(this);

        //CPU Cooler References:
        cpuCoolerNameField = findViewById(R.id.nameOfSelectedCPUCooler);
        cpuCoolerPriceField = findViewById(R.id.priceValueOfSelectedCPUCooler);
        final Button selectCPUCooler = findViewById(R.id.addCPUCoolerButton);
        selectCPUCooler.setOnClickListener(this);
        final Button removeCPUCooler = findViewById(R.id.removeCPUCoolerButton);
        removeCPUCooler.setOnClickListener(this);

        //Motherboard References:
        motherboardNameFiled = findViewById(R.id.nameOfSelectedMotherboard);
        motherboardPriceField = findViewById(R.id.priceValueOfSelectedMotherboard);
        final Button selectMotherboard = findViewById(R.id.addMotherboardButton);
        selectMotherboard.setOnClickListener(this);
        final Button removeMotherboard = findViewById(R.id.removeMotherboardButton);
        removeMotherboard.setOnClickListener(this);

        //Memory References:
        memoryNameField = findViewById(R.id.nameOfSelectedMemory);
        memoryPriceField = findViewById(R.id.priceValueOfSelectedMemory);
        final Button selectMemory = findViewById(R.id.addMemoryButton);
        selectMemory.setOnClickListener(this);
        final Button removeMemory = findViewById(R.id.removeMemoryButton);
        removeMemory.setOnClickListener(this);

        //Storage References:
        storageNameField = findViewById(R.id.nameOfSelectedStorage);
        storagePriceField = findViewById(R.id.priceValueOfSelectedStorage);
        final Button selectStorage = findViewById(R.id.addStorageButton);
        selectStorage.setOnClickListener(this);
        final Button removeStorage = findViewById(R.id.removeStorageButton);
        removeStorage.setOnClickListener(this);

        //GPU References:
        gpuNameField = findViewById(R.id.nameOfSelectedGPU);
        gpuPriceField = findViewById(R.id.priceValueOfSelectedGPU);
        final Button selectGPU = findViewById(R.id.addGPUButton);
        selectGPU.setOnClickListener(this);
        final Button removeGPU = findViewById(R.id.removeGPUButton);
        removeGPU.setOnClickListener(this);

        //Case References:
        caseNameField = findViewById(R.id.nameOfSelectedCase);
        casePriceField = findViewById(R.id.priceValueOfSelectedCase);
        final Button selectCase = findViewById(R.id.addCaseButton);
        selectCase.setOnClickListener(this);
        final Button removeCase = findViewById(R.id.removeCaseButton);
        removeCase.setOnClickListener(this);

        //PSU References:
        psuNameField = findViewById(R.id.nameOfSelectedPowerSupply);
        psuPriceField = findViewById(R.id.priceValueOfSelectedPowerSupply);
        final Button selectPSU = findViewById(R.id.addPowerSupplyButton);
        selectPSU.setOnClickListener(this);
        final Button removePSU = findViewById(R.id.removePowerSupplyButton);
        removePSU.setOnClickListener(this);

        //Estimated Price and Wattage:
        estimatedPriceView = findViewById(R.id.estimatedPriceOfList);
        estimatedWattageView = findViewById(R.id.estimatedWattageOfList);

        //Save List Button:
        final Button saveUsersListToFireStoreBtn = findViewById(R.id.saveUsersListToFirestore);
        saveUsersListToFireStoreBtn.setOnClickListener(this);


        //Retrieve relevant item data
        retrieveCPUData();
        retrieveCPUCoolerData();
        retrieveMotherboardData();
        retrieveMemoryData();
        retrieveStorageData();
        retrieveGPUData();
        retrieveCaseData();
        retrievePSUData();

        calculateEstimatedPrice();
        calculateEstimatedWattage();

        loadUserData();
        updateUserDataViews();


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
            case R.id.addMemoryButton:
                openSelectMemoryOptions();
                break;
            case R.id.addStorageButton:
                openSelectStorageOptions();
                break;
            case R.id.addGPUButton:
                openSelectGPUOptions();
                break;
            case R.id.addCaseButton:
                openSelectCaseOptions();
                break;
            case R.id.addPowerSupplyButton:
                openSelectPSUOptions();
                break;
            case R.id.removeCPUButton:
                removeSelectedCPUDetails();
                break;
            case R.id.removeCPUCoolerButton:
                removeSelectedCPUCoolerDetails();
                break;
            case R.id.removeMotherboardButton:
                removeSelectedMotherboardDetails();
                break;
            case R.id.removeMemoryButton:
                removeSelectedMemoryDetails();
                break;
            case R.id.removeStorageButton:
                removeSelectedStorageDetails();
                break;
            case R.id.removeGPUButton:
                removeSelectedGPUDetails();
                break;
            case R.id.removeCaseButton:
                removeSelectedCaseDetails();
                break;
            case R.id.removePowerSupplyButton:
                removeSelectedPSUDetails();
            case R.id.saveUsersListToFirestore:
                saveUsersListToFirestore();
        }
    }


    //Open select part options

    private void openSelectCPUOptions() {
        Intent openSelectCPUOptions = new Intent(CreateComputerListActivity.this,
                Selectcpu.class);
        saveUserTitleData();
        startActivity(openSelectCPUOptions);
    }

    private void openSelectCPUCoolerOptions() {
        Intent openSelectCPUCoolerOptions = new Intent(CreateComputerListActivity.this,
                SelectCPUCooler.class);
        saveUserTitleData();
        startActivity(openSelectCPUCoolerOptions);
    }

    private void openSelectMotherboardOptions() {
        Intent openSelectMotherboardOptions = new Intent(CreateComputerListActivity.this,
                SelectMotherboard.class);
        saveUserTitleData();
        startActivity(openSelectMotherboardOptions);
    }

    private void openSelectMemoryOptions() {
        Intent openSelectMemoryOptions = new Intent(CreateComputerListActivity.this,
                SelectMemory.class);
        saveUserTitleData();
        startActivity(openSelectMemoryOptions);
    }

    private void openSelectStorageOptions() {
        Intent openSelectStorageOptions = new Intent(CreateComputerListActivity.this,
                SelectStorage.class);
        saveUserTitleData();
        startActivity(openSelectStorageOptions);
    }

    private void openSelectGPUOptions() {
        Intent openSelectGPUOptions = new Intent(CreateComputerListActivity.this,
                SelectGPU.class);
        saveUserTitleData();
        startActivity(openSelectGPUOptions);
    }

    private void openSelectCaseOptions() {
        Intent openSelectCaseOptions = new Intent(CreateComputerListActivity.this,
                SelectCase.class);
        saveUserTitleData();
        startActivity(openSelectCaseOptions);
    }

    private void openSelectPSUOptions() {
        Intent openSelectPSUOptions = new Intent(CreateComputerListActivity.this,
                SelectPowerSupply.class);
        saveUserTitleData();
        startActivity(openSelectPSUOptions);
    }


    //Retrieve part options

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
            //Get the image for the selected CPU Cooler if there's a selected cpu cooler:
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

    private void retrieveMotherboardData() {
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
            //Get the image for the selected motherboard if there's a selected motherboard:
            if (motherboardName != null) {

                //In order to get the image for the guide a storage reference needs to be created
                StorageReference storageReferenceMotherboard = FirebaseStorage.getInstance().getReference().child(motherboardName + ".jpg");
                Log.d(TAG, "getting storage reference name " + storageReferenceMotherboard);

                try {
                    //Create a placeholder that will store the image for the activity
                    final File tempFile = File.createTempFile(motherboardName, "jpg");
                    //Try to retrieve the image from the firestore cloud storage
                    storageReferenceMotherboard.getFile(tempFile)
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
            Log.d(TAG, "retrieveMotherboardData: " + "Something went wrong or the catch statement is working as intended");
        }


    }

    private void retrieveMemoryData() {
        //Set the fields with the relevant data
        memoryNameField.setText(computerComponentData.getComputerList().get("MEMORY NAME"));
        if (computerComponentData.getComputerList().get("MEMORY PRICE") == null) {
            memoryPriceField.setText("");
        } else {
            memoryPriceField.setText((String.format(getResources().getString(R.string.price_of_product), computerComponentData.getComputerList().get("MEMORY PRICE"))));
        }

        //String that is used to check if a product has been selected and to get the associated image for the product
        String memoryName = memoryNameField.getText().toString();

        //Image retrieval is wrapped in a try catch statement. If no product is selected then a IllegalArgumentException
        //is thrown so this prevents the app from crashing
        try {
            //Get the image for the selected memory if there's a selected memory:
            if (memoryName != null) {

                //In order to get the image for the guide a storage reference needs to be created
                StorageReference storageReferenceMemory = FirebaseStorage.getInstance().getReference().child(memoryName + ".jpg");
                Log.d(TAG, "getting storage reference name " + storageReferenceMemory);

                try {
                    //Create a placeholder that will store the image for the activity
                    final File tempFile = File.createTempFile(memoryName, "jpg");
                    //Try to retrieve the image from the firestore cloud storage
                    storageReferenceMemory.getFile(tempFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    //Temp toast message for testing
                                    //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                                    Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                                    ((ImageView) findViewById(R.id.imageOfSelectedMemory)).setImageBitmap(bitmap);
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
            Log.d(TAG, "retrieveMemoryData: " + "Something went wrong or the catch statement is working as intended");
        }


    }

    private void retrieveStorageData() {
        //Set the fields with the relevant data
        storageNameField.setText(computerComponentData.getComputerList().get("STORAGE NAME"));
        if (computerComponentData.getComputerList().get("STORAGE PRICE") == null) {
            storagePriceField.setText("");
        } else {
            storagePriceField.setText((String.format(getResources().getString(R.string.price_of_product), computerComponentData.getComputerList().get("STORAGE PRICE"))));
        }

        //String that is used to check if a product has been selected and to get the associated image for the product
        String storageName = storageNameField.getText().toString();

        //Image retrieval is wrapped in a try catch statement. If no product is selected then a IllegalArgumentException
        //is thrown so this prevents the app from crashing
        try {
            //Get the image for the selected storage if there's a selected storage:
            if (storageName != null) {

                //In order to get the image for the guide a storage reference needs to be created
                StorageReference storageReferenceMemory = FirebaseStorage.getInstance().getReference().child(storageName + ".jpg");
                Log.d(TAG, "getting storage reference name " + storageReferenceMemory);

                try {
                    //Create a placeholder that will store the image for the activity
                    final File tempFile = File.createTempFile(storageName, "jpg");
                    //Try to retrieve the image from the firestore cloud storage
                    storageReferenceMemory.getFile(tempFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    //Temp toast message for testing
                                    //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                                    Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                                    ((ImageView) findViewById(R.id.imageOfSelectedStorage)).setImageBitmap(bitmap);
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
            Log.d(TAG, "retrieveStorageData: " + "Something went wrong or the catch statement is working as intended");
        }


    }

    private void retrieveGPUData() {
        //Set the fields with the relevant data
        gpuNameField.setText(computerComponentData.getComputerList().get("GPU NAME"));
        if (computerComponentData.getComputerList().get("GPU PRICE") == null) {
            gpuPriceField.setText("");
        } else {
            gpuPriceField.setText((String.format(getResources().getString(R.string.price_of_product), computerComponentData.getComputerList().get("GPU PRICE"))));
        }

        //String that is used to check if a product has been selected and to get the associated image for the product
        String gpuName = gpuNameField.getText().toString();

        //Image retrieval is wrapped in a try catch statement. If no product is selected then a IllegalArgumentException
        //is thrown so this prevents the app from crashing
        try {
            //Get the image for the selected storage if there's a selected storage:
            if (gpuName != null) {

                //In order to get the image for the guide a storage reference needs to be created
                StorageReference storageReferenceMemory = FirebaseStorage.getInstance().getReference().child(gpuName + ".jpg");
                Log.d(TAG, "getting storage reference name " + storageReferenceMemory);

                try {
                    //Create a placeholder that will store the image for the activity
                    final File tempFile = File.createTempFile(gpuName, "jpg");
                    //Try to retrieve the image from the firestore cloud storage
                    storageReferenceMemory.getFile(tempFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    //Temp toast message for testing
                                    //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                                    Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                                    ((ImageView) findViewById(R.id.imageOfSelectedGPU)).setImageBitmap(bitmap);
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
            Log.d(TAG, "retrieveStorageData: " + "Something went wrong or the catch statement is working as intended");
        }


    }

    private void retrieveCaseData() {
        //Set the fields with the relevant data
        caseNameField.setText(computerComponentData.getComputerList().get("CASE NAME"));
        if (computerComponentData.getComputerList().get("CASE PRICE") == null) {
            casePriceField.setText("");
        } else {
            casePriceField.setText((String.format(getResources().getString(R.string.price_of_product), computerComponentData.getComputerList().get("CASE PRICE"))));
        }

        //String that is used to check if a product has been selected and to get the associated image for the product
        String caseName = caseNameField.getText().toString();

        //Image retrieval is wrapped in a try catch statement. If no product is selected then a IllegalArgumentException
        //is thrown so this prevents the app from crashing
        try {
            //Get the image for the selected storage if there's a selected storage:
            if (caseName != null) {

                //In order to get the image for the guide a storage reference needs to be created
                StorageReference storageReferenceMemory = FirebaseStorage.getInstance().getReference().child(caseName + ".jpg");
                Log.d(TAG, "getting storage reference name " + storageReferenceMemory);

                try {
                    //Create a placeholder that will store the image for the activity
                    final File tempFile = File.createTempFile(caseName, "jpg");
                    //Try to retrieve the image from the firestore cloud storage
                    storageReferenceMemory.getFile(tempFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    //Temp toast message for testing
                                    //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                                    Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                                    ((ImageView) findViewById(R.id.imageOfSelectedCase)).setImageBitmap(bitmap);
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
            Log.d(TAG, "retrieveStorageData: " + "Something went wrong or the catch statement is working as intended");
        }


    }

    private void retrievePSUData() {
        //Set the fields with the relevant data
        psuNameField.setText(computerComponentData.getComputerList().get("PSU NAME"));
        if (computerComponentData.getComputerList().get("PSU PRICE") == null) {
            psuPriceField.setText("");
        } else {
            psuPriceField.setText((String.format(getResources().getString(R.string.price_of_product), computerComponentData.getComputerList().get("PSU PRICE"))));
        }

        //String that is used to check if a product has been selected and to get the associated image for the product
        String psuName = psuNameField.getText().toString();

        //Image retrieval is wrapped in a try catch statement. If no product is selected then a IllegalArgumentException
        //is thrown so this prevents the app from crashing
        try {
            //Get the image for the selected storage if there's a selected storage:
            if (psuName != null) {

                //In order to get the image for the guide a storage reference needs to be created
                StorageReference storageReferenceMemory = FirebaseStorage.getInstance().getReference().child(psuName + ".jpg");
                Log.d(TAG, "getting storage reference name " + storageReferenceMemory);

                try {
                    //Create a placeholder that will store the image for the activity
                    final File tempFile = File.createTempFile(psuName, "jpg");
                    //Try to retrieve the image from the firestore cloud storage
                    storageReferenceMemory.getFile(tempFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    //Temp toast message for testing
                                    //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                                    Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                                    ((ImageView) findViewById(R.id.imageOfSelectedPowerSupply)).setImageBitmap(bitmap);
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
            Log.d(TAG, "retrieveStorageData: " + "Something went wrong or the catch statement is working as intended");
        }


    }


    //Remove parts options
    private void removeSelectedCPUDetails() {
        if (computerComponentData.getComputerList().get("CPU NAME") != null && computerComponentData.getComputerList().get("CPU PRICE") != null) {

            //Update the total cost and total wattage counts
            double cpuPrice = Double.parseDouble(computerComponentData.getComputerList().get("CPU PRICE"));
            estimatedPrice = estimatedPrice - cpuPrice;
            computerComponentData.getComputerList().put("LIST COSTS", String.format(Locale.getDefault(), "%.2f", estimatedPrice));
            Log.d(TAG, "removeSelectedCPUDetails: Total Costs " + computerComponentData.getComputerList().get("LIST COSTS"));

            int cpuTDP = Integer.parseInt(computerComponentData.getComputerList().get("CPU TDP"));
            int temp = estimatedWattage - cpuTDP;
            Log.d(TAG, "removeSelectedCPUDetails: " + temp);
            computerComponentData.getComputerList().put("ESTIMATED WATTAGE", String.valueOf(temp));
            //If the original item CPU TDP key from the storage hashmap is not removed the old tdp value will remain
            computerComponentData.getComputerList().remove("CPU TDP");
            Log.d(TAG, "removeSelectedCPUDetails: estimated wattage: " + computerComponentData.getComputerList().get("ESTIMATED WATTAGE"));


            //Update view fields for CPU
            cpuPriceField.setText("");
            cpuNameField.setText("");

            //Update storage hashmap by removing the cpu name and price
            computerComponentData.getComputerList().put("CPU NAME", null);
            computerComponentData.getComputerList().put("CPU PRICE", null);
            computerComponentData.getComputerList().put("CPU SOCKET", null);
            Log.d(TAG, "removeSelectedCPUDetails: CPU NAME: " + computerComponentData.getComputerList().get("CPU NAME"));
            Log.d(TAG, "removeSelectedCPUDetails: CPU PRICE: " + computerComponentData.getComputerList().get("CPU PRICE"));

            //Remove the image and set to a blank white square
            ImageView selectCPUImage = findViewById(R.id.imageOfSelectedCPU);
            selectCPUImage.setImageResource(R.drawable.blank_image);

            //Refresh the activity without the 'animation'
            //https://stackoverflow.com/a/17488862
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);

        }
    }

    private void removeSelectedCPUCoolerDetails() {
        if (computerComponentData.getComputerList().get("CPU COOLER NAME") != null && computerComponentData.getComputerList().get("CPU COOLER PRICE") != null) {

            //Update the total cost and total wattage counts
            double cpuCoolerPrice = Double.parseDouble(computerComponentData.getComputerList().get("CPU COOLER PRICE"));
            estimatedPrice = estimatedPrice - cpuCoolerPrice;
            computerComponentData.getComputerList().put("LIST COSTS", String.format(Locale.getDefault(), "%.2f", estimatedPrice));
            Log.d(TAG, "removeSelectedCPUCoolerDetails: Total Costs " + computerComponentData.getComputerList().get("LIST COSTS"));

            int cpuCoolerTDP = Integer.parseInt(computerComponentData.getComputerList().get("CPU COOLER TDP"));
            int temp = estimatedWattage - cpuCoolerTDP;
            Log.d(TAG, "removeSelectedCPUCoolerDetails: " + temp);
            computerComponentData.getComputerList().put("ESTIMATED WATTAGE", String.valueOf(temp));
            //If the original item CPU COOLER TDP key from the storage hashmap is not removed the old tdp value will remain
            computerComponentData.getComputerList().remove("CPU COOLER TDP");
            Log.d(TAG, "removeSelectedCPUCoolerDetails: estimated wattage: " + computerComponentData.getComputerList().get("ESTIMATED WATTAGE"));


            //Update view fields for CPU Cooler
            cpuCoolerPriceField.setText("");
            cpuCoolerNameField.setText("");

            //Update storage hashmap by removing the cpu cooler name and price
            computerComponentData.getComputerList().put("CPU COOLER NAME", null);
            computerComponentData.getComputerList().put("CPU COOLER PRICE", null);
            Log.d(TAG, "removeSelectedCPUCoolerDetails: CPU COOLER NAME: " + computerComponentData.getComputerList().get("CPU COOLER NAME"));
            Log.d(TAG, "removeSelectedCPUCoolerDetails: CPU COOLER PRICE: " + computerComponentData.getComputerList().get("CPU COOLER PRICE"));

            //Remove the image and set to a blank white square
            ImageView selectCPUCoolerImage = findViewById(R.id.imageOfSelectedCPUCooler);
            selectCPUCoolerImage.setImageResource(R.drawable.blank_image);

            //Refresh the activity without the 'animation'
            //https://stackoverflow.com/a/17488862
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);

        }
    }

    private void removeSelectedMotherboardDetails() {
        if (computerComponentData.getComputerList().get("MOTHERBOARD NAME") != null && computerComponentData.getComputerList().get("MOTHERBOARD PRICE") != null) {

            //Update the total cost and total wattage counts
            double motherboardPrice = Double.parseDouble(computerComponentData.getComputerList().get("MOTHERBOARD PRICE"));
            estimatedPrice = estimatedPrice - motherboardPrice;
            computerComponentData.getComputerList().put("LIST COSTS", String.format(Locale.getDefault(), "%.2f", estimatedPrice));
            Log.d(TAG, "removeSelectedMotherboardDetails: Total Costs " + computerComponentData.getComputerList().get("LIST COSTS"));

            int motherboardTDP = Integer.parseInt(computerComponentData.getComputerList().get("MOTHERBOARD TDP"));
            int temp = estimatedWattage - motherboardTDP;
            Log.d(TAG, "removeSelectedMotherboardDetails: " + temp);
            computerComponentData.getComputerList().put("ESTIMATED WATTAGE", String.valueOf(temp));
            //If the original item Motherboard TDP key from the storage hashmap is not removed the old tdp value will remain
            computerComponentData.getComputerList().remove("MOTHERBOARD TDP");
            Log.d(TAG, "removeSelectedMotherboardDetails: estimated wattage: " + computerComponentData.getComputerList().get("ESTIMATED WATTAGE"));


            //Update view fields for Motherboard
            motherboardPriceField.setText("");
            motherboardNameFiled.setText("");

            //Update storage hashmap by removing the motherboard name and price
            computerComponentData.getComputerList().put("MOTHERBOARD NAME", null);
            computerComponentData.getComputerList().put("MOTHERBOARD PRICE", null);
            computerComponentData.getComputerList().put("MOTHERBOARD SOCKET", null);
            Log.d(TAG, "removeSelectedMotherboardDetails: MOTHERBOARD NAME: " + computerComponentData.getComputerList().get("MOTHERBOARD NAME"));
            Log.d(TAG, "removeSelectedMotherboardDetails: MOTHERBOARD PRICE: " + computerComponentData.getComputerList().get("MOTHERBOARD PRICE"));

            //Remove the image and set to a blank white square
            ImageView selectMotherboardImage = findViewById(R.id.imageOfSelectedMotherboard);
            selectMotherboardImage.setImageResource(R.drawable.blank_image);

            //Refresh the activity without the 'animation'
            //https://stackoverflow.com/a/17488862
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);

        }
    }

    private void removeSelectedMemoryDetails() {
        if (computerComponentData.getComputerList().get("MEMORY NAME") != null && computerComponentData.getComputerList().get("MEMORY PRICE") != null) {

            //Update the total cost and total wattage counts
            double memoryPrice = Double.parseDouble(computerComponentData.getComputerList().get("MEMORY PRICE"));
            estimatedPrice = estimatedPrice - memoryPrice;
            computerComponentData.getComputerList().put("LIST COSTS", String.format(Locale.getDefault(), "%.2f", estimatedPrice));
            Log.d(TAG, "removeSelectedMemoryDetails: Total Costs " + computerComponentData.getComputerList().get("LIST COSTS"));

            int memoryTDP = Integer.parseInt(computerComponentData.getComputerList().get("MEMORY TDP"));
            int temp = estimatedWattage - memoryTDP;
            Log.d(TAG, "removeSelectedMemoryDetails: " + temp);
            computerComponentData.getComputerList().put("ESTIMATED WATTAGE", String.valueOf(temp));
            //If the original item Memory TDP key from the storage hashmap is not removed the old tdp value will remain
            computerComponentData.getComputerList().remove("MEMORY TDP");
            Log.d(TAG, "removeSelectedMemoryDetails: estimated wattage: " + computerComponentData.getComputerList().get("ESTIMATED WATTAGE"));


            //Update view fields for Memory
            memoryNameField.setText("");
            memoryPriceField.setText("");

            //Update storage hashmap by removing the memory name and price
            computerComponentData.getComputerList().put("MEMORY NAME", null);
            computerComponentData.getComputerList().put("MEMORY PRICE", null);
            Log.d(TAG, "removeSelectedMemoryDetails: MEMORY NAME: " + computerComponentData.getComputerList().get("MEMORY NAME"));
            Log.d(TAG, "removeSelectedMemoryDetails: MEMORY PRICE: " + computerComponentData.getComputerList().get("MEMORY PRICE"));

            //Remove the image and set to a blank white square
            ImageView selectMemoryImage = findViewById(R.id.imageOfSelectedMemory);
            selectMemoryImage.setImageResource(R.drawable.blank_image);

            //Refresh the activity without the 'animation'
            //https://stackoverflow.com/a/17488862
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);

        }
    }

    private void removeSelectedStorageDetails() {
        if (computerComponentData.getComputerList().get("STORAGE NAME") != null && computerComponentData.getComputerList().get("STORAGE PRICE") != null) {

            //Update the total cost and total wattage counts
            double storagePrice = Double.parseDouble(computerComponentData.getComputerList().get("STORAGE PRICE"));
            estimatedPrice = estimatedPrice - storagePrice;
            computerComponentData.getComputerList().put("LIST COSTS", String.format(Locale.getDefault(), "%.2f", estimatedPrice));
            Log.d(TAG, "removeSelectedStorageDetails: Total Costs " + computerComponentData.getComputerList().get("LIST COSTS"));

            int storageTDP = Integer.parseInt(computerComponentData.getComputerList().get("STORAGE TDP"));
            int temp = estimatedWattage - storageTDP;
            Log.d(TAG, "removeSelectedStorageDetails: " + temp);
            computerComponentData.getComputerList().put("ESTIMATED WATTAGE", String.valueOf(temp));
            //If the original item Storage TDP key from the storage hashmap is not removed the old tdp value will remain
            computerComponentData.getComputerList().remove("STORAGE TDP");
            Log.d(TAG, "removeSelectedStorageDetails: estimated wattage: " + computerComponentData.getComputerList().get("ESTIMATED WATTAGE"));


            //Update view fields for Storage
            storageNameField.setText("");
            storagePriceField.setText("");

            //Update storage hashmap by removing the storage name and price
            computerComponentData.getComputerList().put("STORAGE NAME", null);
            computerComponentData.getComputerList().put("STORAGE PRICE", null);
            Log.d(TAG, "removeSelectedStorageDetails: STORAGE NAME: " + computerComponentData.getComputerList().get("STORAGE NAME"));
            Log.d(TAG, "removeSelectedStorageDetails: STORAGE PRICE: " + computerComponentData.getComputerList().get("STORAGE PRICE"));

            //Remove the image and set to a blank white square
            ImageView selectStorageImage = findViewById(R.id.imageOfSelectedStorage);
            selectStorageImage.setImageResource(R.drawable.blank_image);

            //Refresh the activity without the 'animation'
            //https://stackoverflow.com/a/17488862
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);

        }
    }

    private void removeSelectedGPUDetails() {
        if (computerComponentData.getComputerList().get("GPU NAME") != null && computerComponentData.getComputerList().get("GPU PRICE") != null) {

            //Update the total cost and total wattage counts
            double gpuPrice = Double.parseDouble(computerComponentData.getComputerList().get("GPU PRICE"));
            estimatedPrice = estimatedPrice - gpuPrice;
            computerComponentData.getComputerList().put("LIST COSTS", String.format(Locale.getDefault(), "%.2f", estimatedPrice));
            Log.d(TAG, "removeSelectedGPUDetails: Total Costs " + computerComponentData.getComputerList().get("LIST COSTS"));

            int gpuTDP = Integer.parseInt(computerComponentData.getComputerList().get("GPU TDP"));
            int temp = estimatedWattage - gpuTDP;
            Log.d(TAG, "removeSelectedGPUDetails: " + temp);
            computerComponentData.getComputerList().put("ESTIMATED WATTAGE", String.valueOf(temp));
            //If the original item GPU TDP key from the storage hashmap is not removed the old tdp value will remain
            computerComponentData.getComputerList().remove("GPU TDP");
            Log.d(TAG, "removeSelectedGPUDetails: estimated wattage: " + computerComponentData.getComputerList().get("ESTIMATED WATTAGE"));


            //Update view fields for GPU
            gpuNameField.setText("");
            gpuPriceField.setText("");

            //Update storage hashmap by removing the GPU name and price
            computerComponentData.getComputerList().put("GPU NAME", null);
            computerComponentData.getComputerList().put("GPU PRICE", null);
            Log.d(TAG, "removeSelectedGPUDetails: GPU NAME: " + computerComponentData.getComputerList().get("GPU NAME"));
            Log.d(TAG, "removeSelectedGPUDetails: GPU PRICE: " + computerComponentData.getComputerList().get("GPU PRICE"));

            //Remove the image and set to a blank white square
            ImageView selectGPUImage = findViewById(R.id.imageOfSelectedGPU);
            selectGPUImage.setImageResource(R.drawable.blank_image);

            //Refresh the activity without the 'animation'
            //https://stackoverflow.com/a/17488862
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);

        }
    }

    private void removeSelectedCaseDetails() {
        if (computerComponentData.getComputerList().get("CASE NAME") != null && computerComponentData.getComputerList().get("CASE PRICE") != null) {

            //Update the total cost count
            double casePrice = Double.parseDouble(computerComponentData.getComputerList().get("CASE PRICE"));
            estimatedPrice = estimatedPrice - casePrice;
            computerComponentData.getComputerList().put("LIST COSTS", String.format(Locale.getDefault(), "%.2f", estimatedPrice));
            Log.d(TAG, "removeSelectedCaseDetails: Total Costs " + computerComponentData.getComputerList().get("LIST COSTS"));


            //Update view fields for case
            caseNameField.setText("");
            casePriceField.setText("");

            //Update storage hashmap by removing the case name and price
            computerComponentData.getComputerList().put("CASE NAME", null);
            computerComponentData.getComputerList().put("CASE PRICE", null);
            Log.d(TAG, "removeSelectedCaseDetails: CASE NAME: " + computerComponentData.getComputerList().get("CASE NAME"));
            Log.d(TAG, "removeSelectedCaseDetails: CASE PRICE: " + computerComponentData.getComputerList().get("CASE PRICE"));

            //Remove the image and set to a blank white square
            ImageView selectCaseImage = findViewById(R.id.imageOfSelectedCase);
            selectCaseImage.setImageResource(R.drawable.blank_image);

            //Refresh the activity without the 'animation'
            //https://stackoverflow.com/a/17488862
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);

        }
    }

    private void removeSelectedPSUDetails() {
        if (computerComponentData.getComputerList().get("PSU NAME") != null && computerComponentData.getComputerList().get("PSU PRICE") != null) {

            //Update the total cost count
            double casePrice = Double.parseDouble(computerComponentData.getComputerList().get("PSU PRICE"));
            estimatedPrice = estimatedPrice - casePrice;
            computerComponentData.getComputerList().put("LIST COSTS", String.format(Locale.getDefault(), "%.2f", estimatedPrice));
            Log.d(TAG, "removeSelectedPSUDetails: Total Costs " + computerComponentData.getComputerList().get("LIST COSTS"));


            //Update view fields for power supply
            psuNameField.setText("");
            psuPriceField.setText("");

            //Update storage hashmap by removing the case name and price
            computerComponentData.getComputerList().put("PSU NAME", null);
            computerComponentData.getComputerList().put("PSU PRICE", null);
            Log.d(TAG, "removeSelectedPSUDetails: PSU NAME: " + computerComponentData.getComputerList().get("PSU NAME"));
            Log.d(TAG, "removeSelectedPSUDetails: PSU PRICE: " + computerComponentData.getComputerList().get("PSU PRICE"));

            //Remove the image and set to a blank white square
            ImageView selectPowerSupply = findViewById(R.id.imageOfSelectedPowerSupply);
            selectPowerSupply.setImageResource(R.drawable.blank_image);

            //Refresh the activity without the 'animation'
            //https://stackoverflow.com/a/17488862
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);

        }
    }


    //If a price is not null then retrieve the part cost in the DataStorage Hashmap and assign the total
    //cost to the relevant view. This is a bit inefficient though I must say
    private void calculateEstimatedPrice() {
        if (computerComponentData.getComputerList().get("CPU PRICE") != null) {
            double cpuPrice = Double.parseDouble(computerComponentData.getComputerList().get("CPU PRICE"));
            Log.d(TAG, "calculateEstimatedPrice: " + cpuPrice);
            estimatedPrice = estimatedPrice + cpuPrice;
            computerComponentData.getComputerList().put("LIST COSTS", String.format(Locale.getDefault(), "%.2f", estimatedPrice));
            Log.d(TAG, "calculateEstimatedPrice: " + computerComponentData.getComputerList().get("LIST COSTS"));
        }
        if (computerComponentData.getComputerList().get("CPU COOLER PRICE") != null) {
            double cpuCoolerPrice = Double.parseDouble(computerComponentData.getComputerList().get("CPU COOLER PRICE"));
            Log.d(TAG, "calculateEstimatedPrice: " + cpuCoolerPrice);
            estimatedPrice = estimatedPrice + cpuCoolerPrice;
            computerComponentData.getComputerList().put("LIST COSTS", String.format(Locale.getDefault(), "%.2f", estimatedPrice));
            Log.d(TAG, "calculateEstimatedPrice: " + computerComponentData.getComputerList().get("LIST COSTS"));
        }
        if (computerComponentData.getComputerList().get("MOTHERBOARD PRICE") != null) {
            double motherboardPrice = Double.parseDouble(computerComponentData.getComputerList().get("MOTHERBOARD PRICE"));
            Log.d(TAG, "calculateEstimatedPrice: " + motherboardPrice);
            estimatedPrice = estimatedPrice + motherboardPrice;
            computerComponentData.getComputerList().put("LIST COSTS", String.format(Locale.getDefault(), "%.2f", estimatedPrice));
            Log.d(TAG, "calculateEstimatedPrice: " + computerComponentData.getComputerList().get("LIST COSTS"));
        }
        if (computerComponentData.getComputerList().get("MEMORY PRICE") != null) {
            double memoryPrice = Double.parseDouble(computerComponentData.getComputerList().get("MEMORY PRICE"));
            Log.d(TAG, "calculateEstimatedPrice: " + memoryPrice);
            estimatedPrice = estimatedPrice + memoryPrice;
            computerComponentData.getComputerList().put("LIST COSTS", String.format(Locale.getDefault(), "%.2f", estimatedPrice));
            Log.d(TAG, "calculateEstimatedPrice: " + computerComponentData.getComputerList().get("LIST COSTS"));
        }
        if (computerComponentData.getComputerList().get("STORAGE PRICE") != null) {
            double storagePrice = Double.parseDouble(computerComponentData.getComputerList().get("STORAGE PRICE"));
            Log.d(TAG, "calculateEstimatedPrice: " + storagePrice);
            estimatedPrice = estimatedPrice + storagePrice;
            computerComponentData.getComputerList().put("LIST COSTS", String.format(Locale.getDefault(), "%.2f", estimatedPrice));
            Log.d(TAG, "calculateEstimatedPrice: " + computerComponentData.getComputerList().get("LIST COSTS"));
        }
        if (computerComponentData.getComputerList().get("GPU PRICE") != null) {
            double gpuPrice = Double.parseDouble(computerComponentData.getComputerList().get("GPU PRICE"));
            Log.d(TAG, "calculateEstimatedPrice: " + gpuPrice);
            estimatedPrice = estimatedPrice + gpuPrice;
            computerComponentData.getComputerList().put("LIST COSTS", String.format(Locale.getDefault(), "%.2f", estimatedPrice));
            Log.d(TAG, "calculateEstimatedPrice: " + computerComponentData.getComputerList().get("LIST COSTS"));
        }
        if (computerComponentData.getComputerList().get("CASE PRICE") != null) {
            double casePrice = Double.parseDouble(computerComponentData.getComputerList().get("CASE PRICE"));
            Log.d(TAG, "calculateEstimatedPrice: " + casePrice);
            estimatedPrice = estimatedPrice + casePrice;
            computerComponentData.getComputerList().put("LIST COSTS", String.format(Locale.getDefault(), "%.2f", estimatedPrice));
            Log.d(TAG, "calculateEstimatedPrice: " + computerComponentData.getComputerList().get("LIST COSTS"));
        }
        if (computerComponentData.getComputerList().get("PSU PRICE") != null) {
            double psuPrice = Double.parseDouble(computerComponentData.getComputerList().get("PSU PRICE"));
            Log.d(TAG, "calculateEstimatedPrice: " + psuPrice);
            estimatedPrice = estimatedPrice + psuPrice;
            computerComponentData.getComputerList().put("LIST COSTS", String.format(Locale.getDefault(), "%.2f", estimatedPrice));
            Log.d(TAG, "calculateEstimatedPrice: " + computerComponentData.getComputerList().get("LIST COSTS"));
        }

    }

    //If a product has been selected then get its tdp value if it has one and assign that value to
    //the counting total wattage count
    private void calculateEstimatedWattage() {
        if (computerComponentData.getComputerList().get("CPU TDP") != null) {
            int cpuTDP = Integer.parseInt(computerComponentData.getComputerList().get("CPU TDP"));
            estimatedWattage = estimatedWattage + cpuTDP;
            computerComponentData.getComputerList().put("ESTIMATED WATTAGE", String.valueOf(estimatedWattage));
            Log.d(TAG, "calculateEstimatedWattage: " + computerComponentData.getComputerList().get("ESTIMATED WATTAGE"));
        }
        if (computerComponentData.getComputerList().get("CPU COOLER TDP") != null) {
            int cpuCoolerTDP = Integer.parseInt(computerComponentData.getComputerList().get("CPU COOLER TDP"));
            estimatedWattage = estimatedWattage + cpuCoolerTDP;
            computerComponentData.getComputerList().put("ESTIMATED WATTAGE", String.valueOf(estimatedWattage));
            Log.d(TAG, "calculateEstimatedWattage: " + computerComponentData.getComputerList().get("ESTIMATED WATTAGE"));
        }
        if (computerComponentData.getComputerList().get("MOTHERBOARD TDP") != null) {
            int motherboardTDP = Integer.parseInt(computerComponentData.getComputerList().get("MOTHERBOARD TDP"));
            estimatedWattage = estimatedWattage + motherboardTDP;
            computerComponentData.getComputerList().put("ESTIMATED WATTAGE", String.valueOf(estimatedWattage));
            Log.d(TAG, "calculateEstimatedWattage: " + computerComponentData.getComputerList().get("ESTIMATED WATTAGE"));
        }
        if (computerComponentData.getComputerList().get("GPU TDP") != null) {
            int gpuTDP = Integer.parseInt(computerComponentData.getComputerList().get("GPU TDP"));
            estimatedWattage = estimatedWattage + gpuTDP;
            computerComponentData.getComputerList().put("ESTIMATED WATTAGE", String.valueOf(estimatedWattage));
            Log.d(TAG, "calculateEstimatedWattage: " + computerComponentData.getComputerList().get("ESTIMATED WATTAGE"));
        }
        if (computerComponentData.getComputerList().get("MEMORY TDP") != null) {
            int memoryTDP = Integer.parseInt(computerComponentData.getComputerList().get("MEMORY TDP"));
            estimatedWattage = estimatedWattage + memoryTDP;
            computerComponentData.getComputerList().put("ESTIMATED WATTAGE", String.valueOf(estimatedWattage));
            Log.d(TAG, "calculateEstimatedWattage: " + computerComponentData.getComputerList().get("ESTIMATED WATTAGE"));
        }
        if (computerComponentData.getComputerList().get("STORAGE TDP") != null) {
            int storageTDP = Integer.parseInt(computerComponentData.getComputerList().get("STORAGE TDP"));
            estimatedWattage = estimatedWattage + storageTDP;
            computerComponentData.getComputerList().put("ESTIMATED WATTAGE", String.valueOf(estimatedWattage));
            Log.d(TAG, "calculateEstimatedWattage: " + computerComponentData.getComputerList().get("ESTIMATED WATTAGE"));
        }


    }

    //Store the users title into the DataStorage hashmap
    public void saveUserTitleData() {
        listTitleName = enterTitleForPCList.getText().toString();
        listDescriptionString = listDescription.getText().toString();

        computerComponentData.getComputerList().put("title", listTitleName);
        Log.d(TAG, "saveData: " + computerComponentData.getComputerList().get("title"));

        computerComponentData.getComputerList().put("description", listDescriptionString);
        Log.d(TAG, "saveData: " + computerComponentData.getComputerList().get("description"));

    }

    //When the user opens the activity load the potential data from the DataStorage hashmap
    public void loadUserData() {
        listTitleName = computerComponentData.getComputerList().get("title");
        Log.d(TAG, "loadData: " + computerComponentData.getComputerList().get("title"));
        computerComponentData.getComputerList().get("description");
        Log.d(TAG, "saveData: " + computerComponentData.getComputerList().get("description"));
        estimatedPriceString = computerComponentData.getComputerList().get("LIST COSTS");
        estimatedWattageString = computerComponentData.getComputerList().get("ESTIMATED WATTAGE");

    }

    //Update the users view with the data from the hashmap
    public void updateUserDataViews() {
        enterTitleForPCList.setText(listTitleName);
        listDescription.setText(listDescriptionString);
        if (estimatedPrice == 0.00) {
            estimatedPriceString = "";
        } else {
            estimatedPriceView.setText((getResources().getString(R.string.cost_of_list) + estimatedPriceString));
        }

        if (estimatedWattage == 0) {
            estimatedWattageView.setText((getResources().getString(R.string.estimated_wattage_of_list)));
        } else {
            estimatedWattageView.setText((getResources().getString(R.string.estimated_wattage_of_list) + " " + estimatedWattageString + " W"));
        }
    }

    //Save the list to Firestore
    //Just trying to do it roughly right now to see I can do so and then retrieve the data properly again
    private void saveUsersListToFirestore() {


        //Initialise the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        String currentUser = mAuth.getUid();

        //Trying to write something to firestore first before touching it up properly
        computerComponentData.getComputerList().put("userID", currentUser);

        //Update the title and description details in the hashmap again just in case they were changed slightly
        listTitleName = enterTitleForPCList.getText().toString();
        listDescriptionString = listDescription.getText().toString();

        //Store data into hashmap
        computerComponentData.getComputerList().put("title", listTitleName);
        Log.d(TAG, "saveData: " + computerComponentData.getComputerList().get("title"));
        computerComponentData.getComputerList().put("description", listDescriptionString);
        Log.d(TAG, "saveData: " + computerComponentData.getComputerList().get("description"));

        db.collection("user-lists")
                .add( computerComponentData.getComputerList())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        //Return back to the MainActivity and display toast message
                        Intent returnToMainActivity = new Intent(CreateComputerListActivity.this,
                                MainActivity.class);
                        Toast.makeText(CreateComputerListActivity.this, "List Saved Successfully", Toast.LENGTH_LONG).show();
                        startActivity(returnToMainActivity);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });


    }


}