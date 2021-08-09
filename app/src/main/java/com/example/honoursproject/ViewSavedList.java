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

public class ViewSavedList extends AppCompatActivity {

    //Tag used for debugging the firestore data retrieval
    private static final String TAG = "ViewSavedListTAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_saved_list);

        //Get all the data for the storage firestore document that was selected as a HashMap
        Intent intent = getIntent();
        final HashMap<String, Object> savedListHashMapData = (HashMap<String, Object>) intent.getSerializableExtra("hashMap");

        //Setup references to UI elements within the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Viewing saved list:");

        //Go back to the previous activity in the activity backstack
        //https://stackoverflow.com/questions/49350686/back-to-previous-activity-arrow-button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                returnToSelectSavedLists();
                finish();
            }
        });

        //Get the title name of the saved list:
        TextView nameOfSavedList = findViewById(R.id.nameOfClickedList);
        nameOfSavedList.setText((CharSequence) savedListHashMapData.get("title"));

        //Get the description of the saved list:
        TextView descriptionOfSavedList = findViewById(R.id.userClickedListDescription);
        descriptionOfSavedList.setText((CharSequence) savedListHashMapData.get("description"));


        //CPU Name:
        TextView nameOfCPUFromSavedList = findViewById(R.id.nameOfSelectedCPUFromUsersList);
        nameOfCPUFromSavedList.setText((CharSequence) savedListHashMapData.get("CPU NAME"));

        //CPU Price:
        TextView priceOfCPUFromSavedList = findViewById(R.id.priceValueOfSelectedCPUFromUsersList);
        priceOfCPUFromSavedList.setText((String.format(getResources().getString(R.string.price_of_product), (CharSequence) savedListHashMapData.get("CPU PRICE"))));

        //Image of CPU:
        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReferenceCPU = FirebaseStorage.getInstance().getReference().child(nameOfCPUFromSavedList.getText().toString() + ".jpg");
        Log.d("my cpu image", String.valueOf(storageReferenceCPU));

        try {
            //Create a placeholder that will store the image for the activity
            final File tempFile = File.createTempFile(nameOfCPUFromSavedList.getText().toString(), "jpg");
            //Try to retrieve the image from the firestore cloud storage
            storageReferenceCPU.getFile(tempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Temp toast message for testing
                            //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                            ((ImageView) findViewById(R.id.imageOfSelectedCPUFromUsersList)).setImageBitmap(bitmap);
                            Log.d("image retrieved", "The image has been retrieved successfully. Which makes sense if you can actually see it");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //If the image can't be retrieved for whatever reason then display the toast as an error message and write a log statement to the console
                    Toast.makeText(ViewSavedList.this, "Image failed to be retrieved", Toast.LENGTH_SHORT).show();
                    Log.d("image not retrieved", "The image has failed to be retrieved successfully. Which makes sense as you can't see the image");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


        //CPU Cooler Name:
        TextView nameOfCPUCoolerFromSavedList = findViewById(R.id.nameOfSelectedCPUCoolerFromUsersList);
        nameOfCPUCoolerFromSavedList.setText((CharSequence) savedListHashMapData.get("CPU COOLER NAME"));

        //CPU Cooler Price:
        TextView priceOfCPUCoolerFromSavedList = findViewById(R.id.priceValueOfSelectedCPUCoolerFromUsersList);
        priceOfCPUCoolerFromSavedList.setText((String.format(getResources().getString(R.string.price_of_product), (CharSequence) savedListHashMapData.get("CPU COOLER PRICE"))));

        //Image of CPU COOLER:
        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReferenceCPUCooler = FirebaseStorage.getInstance().getReference().child(nameOfCPUCoolerFromSavedList.getText().toString() + ".jpg");
        Log.d("my cpu cooler image", String.valueOf(storageReferenceCPUCooler));

        try {
            //Create a placeholder that will store the image for the activity
            final File tempFile = File.createTempFile(nameOfCPUCoolerFromSavedList.getText().toString(), "jpg");
            //Try to retrieve the image from the firestore cloud storage
            storageReferenceCPUCooler.getFile(tempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Temp toast message for testing
                            //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                            ((ImageView) findViewById(R.id.imageOfSelectedCPUCoolerFromUsersList)).setImageBitmap(bitmap);
                            Log.d("image retrieved", "The image has been retrieved successfully. Which makes sense if you can actually see it");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //If the image can't be retrieved for whatever reason then display the toast as an error message and write a log statement to the console
                    Toast.makeText(ViewSavedList.this, "Image failed to be retrieved", Toast.LENGTH_SHORT).show();
                    Log.d("image not retrieved", "The image has failed to be retrieved successfully. Which makes sense as you can't see the image");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


        //Motherboard Name:
        TextView nameOfMotherboardFromSavedList = findViewById(R.id.nameOfSelectedMotherboardFromUsersList);
        nameOfMotherboardFromSavedList.setText((CharSequence) savedListHashMapData.get("MOTHERBOARD NAME"));

        //Motherboard Price:
        TextView priceOfMotherboardFromSavedList = findViewById(R.id.priceValueOfSelectedMotherboardFromUsersList);
        priceOfMotherboardFromSavedList.setText((String.format(getResources().getString(R.string.price_of_product), (CharSequence) savedListHashMapData.get("MOTHERBOARD PRICE"))));

        //Image of Motherboard:
        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReferenceMotherboard = FirebaseStorage.getInstance().getReference().child(nameOfMotherboardFromSavedList.getText().toString() + ".jpg");
        Log.d("my motherboard image", String.valueOf(storageReferenceMotherboard));

        try {
            //Create a placeholder that will store the image for the activity
            final File tempFile = File.createTempFile(nameOfMotherboardFromSavedList.getText().toString(), "jpg");
            //Try to retrieve the image from the firestore cloud storage
            storageReferenceMotherboard.getFile(tempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Temp toast message for testing
                            //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                            ((ImageView) findViewById(R.id.imageOfSelectedMotherboardFromUsersList)).setImageBitmap(bitmap);
                            Log.d("image retrieved", "The image has been retrieved successfully. Which makes sense if you can actually see it");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //If the image can't be retrieved for whatever reason then display the toast as an error message and write a log statement to the console
                    Toast.makeText(ViewSavedList.this, "Image failed to be retrieved", Toast.LENGTH_SHORT).show();
                    Log.d("image not retrieved", "The image has failed to be retrieved successfully. Which makes sense as you can't see the image");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


        //Memory Name:
        TextView nameOfMemoryFromSavedList = findViewById(R.id.nameOfSelectedMemoryFromUsersList);
        nameOfMemoryFromSavedList.setText((CharSequence) savedListHashMapData.get("MEMORY NAME"));

        //Memory Price:
        TextView priceOfMemoryFromSavedList = findViewById(R.id.priceValueOfSelectedMemoryFromUsersList);
        priceOfMemoryFromSavedList.setText((String.format(getResources().getString(R.string.price_of_product), (CharSequence) savedListHashMapData.get("MEMORY PRICE"))));

        //Image of Memory:
        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReferenceMemory = FirebaseStorage.getInstance().getReference().child(nameOfMemoryFromSavedList.getText().toString() + ".jpg");
        Log.d("my memory image", String.valueOf(storageReferenceMemory));

        try {
            //Create a placeholder that will store the image for the activity
            final File tempFile = File.createTempFile(nameOfMemoryFromSavedList.getText().toString(), "jpg");
            //Try to retrieve the image from the firestore cloud storage
            storageReferenceMemory.getFile(tempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Temp toast message for testing
                            //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                            ((ImageView) findViewById(R.id.imageOfSelectedMemoryFromUsersList)).setImageBitmap(bitmap);
                            Log.d("image retrieved", "The image has been retrieved successfully. Which makes sense if you can actually see it");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //If the image can't be retrieved for whatever reason then display the toast as an error message and write a log statement to the console
                    Toast.makeText(ViewSavedList.this, "Image failed to be retrieved", Toast.LENGTH_SHORT).show();
                    Log.d("image not retrieved", "The image has failed to be retrieved successfully. Which makes sense as you can't see the image");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


        //Storage Name:
        TextView nameOfStorageFromSavedList = findViewById(R.id.nameOfSelectedStorageFromUsersList);
        nameOfStorageFromSavedList.setText((CharSequence) savedListHashMapData.get("STORAGE NAME"));

        //Storage Price:
        TextView priceOfStorageFromSavedList = findViewById(R.id.priceValueOfSelectedStorageFromUsersList);
        priceOfStorageFromSavedList.setText((String.format(getResources().getString(R.string.price_of_product), (CharSequence) savedListHashMapData.get("STORAGE PRICE"))));

        //Image of Storage:
        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReferenceStorage = FirebaseStorage.getInstance().getReference().child(nameOfStorageFromSavedList.getText().toString() + ".jpg");
        Log.d("my storage image", String.valueOf(storageReferenceStorage));

        try {
            //Create a placeholder that will store the image for the activity
            final File tempFile = File.createTempFile(nameOfStorageFromSavedList.getText().toString(), "jpg");
            //Try to retrieve the image from the firestore cloud storage
            storageReferenceStorage.getFile(tempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Temp toast message for testing
                            //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                            ((ImageView) findViewById(R.id.imageOfSelectedStorageFromUsersList)).setImageBitmap(bitmap);
                            Log.d("image retrieved", "The image has been retrieved successfully. Which makes sense if you can actually see it");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //If the image can't be retrieved for whatever reason then display the toast as an error message and write a log statement to the console
                    Toast.makeText(ViewSavedList.this, "Image failed to be retrieved", Toast.LENGTH_SHORT).show();
                    Log.d("image not retrieved", "The image has failed to be retrieved successfully. Which makes sense as you can't see the image");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


        //Video Card Name:
        TextView nameOfVideoCardFromSavedList = findViewById(R.id.nameOfSelectedGPUFromUsersList);
        nameOfVideoCardFromSavedList.setText((CharSequence) savedListHashMapData.get("GPU NAME"));

        //Video Card Price:
        TextView priceOfVideoCardFromSavedList = findViewById(R.id.priceValueOfSelectedGPUFromUsersList);
        priceOfVideoCardFromSavedList.setText((String.format(getResources().getString(R.string.price_of_product), (CharSequence) savedListHashMapData.get("GPU PRICE"))));

        //Image of Storage:
        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReferenceGPU = FirebaseStorage.getInstance().getReference().child(nameOfVideoCardFromSavedList.getText().toString() + ".jpg");
        Log.d("my gpu image", String.valueOf(storageReferenceGPU));

        try {
            //Create a placeholder that will store the image for the activity
            final File tempFile = File.createTempFile(nameOfVideoCardFromSavedList.getText().toString(), "jpg");
            //Try to retrieve the image from the firestore cloud storage
            storageReferenceGPU.getFile(tempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Temp toast message for testing
                            //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                            ((ImageView) findViewById(R.id.imageOfSelectedGPUFromUsersList)).setImageBitmap(bitmap);
                            Log.d("image retrieved", "The image has been retrieved successfully. Which makes sense if you can actually see it");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //If the image can't be retrieved for whatever reason then display the toast as an error message and write a log statement to the console
                    Toast.makeText(ViewSavedList.this, "Image failed to be retrieved", Toast.LENGTH_SHORT).show();
                    Log.d("image not retrieved", "The image has failed to be retrieved successfully. Which makes sense as you can't see the image");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


        //PC Case Name:
        TextView nameOfCaseFromSavedList = findViewById(R.id.nameOfSelectedCaseFromUsersList);
        nameOfCaseFromSavedList.setText((CharSequence) savedListHashMapData.get("CASE NAME"));

        //PC Case Price:
        TextView priceOfCaseFromSavedList = findViewById(R.id.priceValueOfSelectedCaseFromUsersList);
        priceOfCaseFromSavedList.setText((String.format(getResources().getString(R.string.price_of_product), (CharSequence) savedListHashMapData.get("CASE PRICE"))));

        //Image of Case:
        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReferenceCase = FirebaseStorage.getInstance().getReference().child(nameOfCaseFromSavedList.getText().toString() + ".jpg");
        Log.d("my case image", String.valueOf(storageReferenceCase));

        try {
            //Create a placeholder that will store the image for the activity
            final File tempFile = File.createTempFile(nameOfCaseFromSavedList.getText().toString(), "jpg");
            //Try to retrieve the image from the firestore cloud storage
            storageReferenceCase.getFile(tempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Temp toast message for testing
                            //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                            ((ImageView) findViewById(R.id.imageOfSelectedCaseFromUsersList)).setImageBitmap(bitmap);
                            Log.d("image retrieved", "The image has been retrieved successfully. Which makes sense if you can actually see it");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //If the image can't be retrieved for whatever reason then display the toast as an error message and write a log statement to the console
                    Toast.makeText(ViewSavedList.this, "Image failed to be retrieved", Toast.LENGTH_SHORT).show();
                    Log.d("image not retrieved", "The image has failed to be retrieved successfully. Which makes sense as you can't see the image");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


        //PC Power Supply Name:
        TextView nameOfPSUFromSavedList = findViewById(R.id.nameOfSelectedPowerSupplyFromUsersList);
        nameOfPSUFromSavedList.setText((CharSequence) savedListHashMapData.get("PSU NAME"));

        //PC Power Supply Price:
        TextView priceOfPSUFromSavedList = findViewById(R.id.priceValueOfSelectedPowerSupplyFromUsersList);
        priceOfPSUFromSavedList.setText((String.format(getResources().getString(R.string.price_of_product), (CharSequence) savedListHashMapData.get("PSU PRICE"))));

        //Image of PSU:
        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReferencePSU = FirebaseStorage.getInstance().getReference().child(nameOfPSUFromSavedList.getText().toString() + ".jpg");
        Log.d("my case image", String.valueOf(storageReferencePSU));

        try {
            //Create a placeholder that will store the image for the activity
            final File tempFile = File.createTempFile(nameOfPSUFromSavedList.getText().toString(), "jpg");
            //Try to retrieve the image from the firestore cloud storage
            storageReferencePSU.getFile(tempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Temp toast message for testing
                            //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                            ((ImageView) findViewById(R.id.imageOfSelectedPowerSupplyFromUsersList)).setImageBitmap(bitmap);
                            Log.d("image retrieved", "The image has been retrieved successfully. Which makes sense if you can actually see it");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //If the image can't be retrieved for whatever reason then display the toast as an error message and write a log statement to the console
                    Toast.makeText(ViewSavedList.this, "Image failed to be retrieved", Toast.LENGTH_SHORT).show();
                    Log.d("image not retrieved", "The image has failed to be retrieved successfully. Which makes sense as you can't see the image");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


        //Estimated Price Of List:
        TextView estimatedPriceOfSavedList = findViewById(R.id.estimatedPriceOfUsersList);
        estimatedPriceOfSavedList.setText((getResources().getString(R.string.cost_of_list) + (CharSequence) savedListHashMapData.get("LIST COSTS")));

        //Estimated Wattage Of List:
        TextView estimatedWattageOfSavedList = findViewById(R.id.estimatedWattageOfUsersList);
        estimatedWattageOfSavedList.setText(getResources().getString(R.string.estimated_wattage_of_list) + " " + (CharSequence) savedListHashMapData.get("ESTIMATED WATTAGE") + " W");


        //Button to return the user to the saved lists activity:
        Button returnToSelectSavedListsActivity = findViewById(R.id.returnToUserSavedListMenuOverview);
        returnToSelectSavedListsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openSelectSavedListActivity = new Intent(ViewSavedList.this,
                        SelectSavedList.class);
                startActivity(openSelectSavedListActivity);
            }
        });

    }

    private void returnToSelectSavedLists() {
        Intent openSelectSavedList = new Intent
                (this, SelectSavedList.class);
        startActivity(openSelectSavedList);
    }

    //Over ride back button
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ViewSavedList.this, SelectSavedList.class));
        finish();
    }
}