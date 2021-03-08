/*
Honours Project - PC part Builder
File: ViewCaseDetails Class
Author: Conner McGill - B00320975
Date: 2021/03/08

Summary of file:

    This class assigns the HashMap data that is retrieved from the SelectCase class and displays
    the detailed information in a nestedScrollView showing the extra details that the user would
    not see on the SelectCase recycler view

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

public class ViewCaseDetails extends AppCompatActivity {

    //Tag used for debugging the firestore data retrieval
    private static final String TAG = "ViewCaseDetailsTAG";

    //DataStorage Instance:
    DataStorage caseData = new DataStorage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_case_details);

        //Get all the data for the Case firestore document that was selected as a HashMap
        Intent intent = getIntent();
        final HashMap<String, Object> caseHashMapData = (HashMap<String, Object>) intent.getSerializableExtra("hashMap");
        //Randomly testing I can get some data here
        Log.d(TAG, "onCreate: " + caseHashMapData.get("name"));
        Log.d(TAG, "values: " + caseHashMapData.keySet());

        //Setup references to UI elements within the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Case Details:");

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
        //case which is the name of the image file in a String variable
        String caseImageName = (String) caseHashMapData.get("name");
        Log.d(TAG, "case Name: " + caseHashMapData.get("name"));

        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(caseImageName + ".jpg");
        Log.d("my case image", String.valueOf(storageReference));

        try {
            //Create a placeholder that will store the image for the activity
            final File tempFile = File.createTempFile(caseImageName, "jpg");
            //Try to retrieve the image from the firestore cloud storage
            storageReference.getFile(tempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Temp toast message for testing
                            //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                            ((ImageView) findViewById(R.id.imageOfClickedCase)).setImageBitmap(bitmap);
                            Log.d("image retrieved", "The image has been retrieved successfully. Which makes sense if you can actually see it");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //If the image can't be retrieved for whatever reason then display the toast as an error message and write a log statement to the console
                    Toast.makeText(ViewCaseDetails.this, "Image failed to be retrieved", Toast.LENGTH_SHORT).show();
                    Log.d("image not retrieved", "The image has failed to be retrieved successfully. Which makes sense as you can't see the image");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Assign the values into their respective text views:
        //Name of the gpu:
        TextView titleOfClickedCase = findViewById(R.id.nameOfClickedCase);
        titleOfClickedCase.setText((CharSequence) caseHashMapData.get("name"));

        //Price of the gpu:
        TextView priceOfClickedCase = findViewById(R.id.priceOfClickedCase);
        Log.d(TAG, "onCreate: " + caseHashMapData.get("price"));
        //Convert the price from the HashMap into a string so that it can be assigned into the TextView
        String price = Double.toString((Double) caseHashMapData.get("price"));
        Log.d(TAG, "price: " + price);
        priceOfClickedCase.setText("Price: £" + price);

        //Manufacturer of the case:
        TextView manufacturerOfCase = findViewById(R.id.caseManufacturerClickedValue);
        manufacturerOfCase.setText((CharSequence) caseHashMapData.get("manufacturer"));

        //Type of case:
        TextView typeOfCase = findViewById(R.id.caseTypeClickedValue);
        typeOfCase.setText((CharSequence) caseHashMapData.get("type"));

        //Colour of case:
        TextView colourOfCase = findViewById(R.id.caseColourClickedValue);
        colourOfCase.setText((CharSequence) caseHashMapData.get("colour"));

        //Side Panel of case:
        TextView sidePanelOfCase = findViewById(R.id.caseSidePanelClickedValue);
        sidePanelOfCase.setText((CharSequence) caseHashMapData.get("side-panel-window"));

        //Front Panel IO of case:
        ArrayList<String> frontPanelUsb = (ArrayList<String>) caseHashMapData.get("front-panel-usb");
        TextView frontPanelUsbOfCase = findViewById(R.id.caseFrontPanelUsbClickedValue);
        //Loop through the arrayList and assign the appropriate IO headers for that part
        //Since the size will be dynamic for each part it would make more sense to use a loop
        //rather than assigning all the data individually
        String fp = "";
        for (int i = 0; i < frontPanelUsb.size(); i++) {
            fp += "• " + frontPanelUsb.get(i) + "\n";
        }
        frontPanelUsbOfCase.setText(fp);
        Log.d(TAG, "onCreate: " + fp);

        //Motherboard Form Factor of case:
        ArrayList<String> motherboardFormFactor = (ArrayList<String>) caseHashMapData.get("motherboard-form-factor");
        TextView formFactorOfCase = findViewById(R.id.caseMotherboardFormFactorClickedValue);
        //Loop through the arrayList and assign the appropriate form factor values
        //Since the size will be dynamic for each part it would make more sense to use a loop
        //rather than assigning all the data individually
        String ff = "";
        for (int i = 0; i < motherboardFormFactor.size(); i++) {
            ff += "• " + motherboardFormFactor.get(i) + "\n";
        }
        formFactorOfCase.setText(ff);
        Log.d(TAG, "onCreate: " + ff);

        //Full Height Expansion Slots Of Case:
        TextView fullHeightExpansionSlots = findViewById(R.id.caseFullHeightExpansionSlotsClickedValue);
        //Convert the value from the HashMap into a string so that it can be assigned into the TextView
        String fullHeightExpansionSlotsNum = Long.toString((Long) caseHashMapData.get("full-height-expansion-slots"));
        fullHeightExpansionSlots.setText(fullHeightExpansionSlotsNum);

        //Half Height Expansion Slots Of Case:
        TextView HalfHeightExpansionSlots = findViewById(R.id.caseHalfHeightExpansionSlotsClickedValue);
        //Convert the value from the HashMap into a string so that it can be assigned into the TextView
        String HalfHeightExpansionSlotsNum = Long.toString((Long) caseHashMapData.get("half-height-expansion-slots"));
        HalfHeightExpansionSlots.setText(HalfHeightExpansionSlotsNum);

        //Maximum Video Card Length Of Case:
        ArrayList<String> maxVideoCardLength = (ArrayList<String>) caseHashMapData.get("maximum-video-card-length");
        TextView maxVideoCardLengthView = findViewById(R.id.caseMaxVideoCardLengthClickedValue);
        //Loop through the arrayList and assign the appropriate length values
        //Since the size will be dynamic for each part it would make more sense to use a loop
        //rather than assigning all the data individually
        String ml = "";
        for (int i = 0; i < maxVideoCardLength.size(); i++) {
            ml += "• " + maxVideoCardLength.get(i) + "\n";
        }
        maxVideoCardLengthView.setText(ml);
        Log.d(TAG, "onCreate: " + ml);

        //Dimensions of Case:
        ArrayList<String> dimensionsOfCase = (ArrayList<String>) caseHashMapData.get("dimensions");
        TextView dimensionsOfCaseView = findViewById(R.id.caseDimensionsClickedValue);
        //Loop through the arrayList and assign the appropriate length values
        //Since the size will be dynamic for each part it would make more sense to use a loop
        //rather than assigning all the data individually
        String dm = "";
        for (int i = 0; i < dimensionsOfCase.size(); i++) {
            dm += "• " + dimensionsOfCase.get(i) + "\n";
        }
        dimensionsOfCaseView.setText(dm);
        Log.d(TAG, "onCreate: " + dm);

        //Internal 3.5" bays Of Case:
        TextView internal3inchBays = findViewById(R.id.caseInternal3InchBaysClickedValue);
        //Convert the value from the HashMap into a string so that it can be assigned into the TextView
        String internal3inchBaysString = Long.toString((Long) caseHashMapData.get("internal-3-bays"));
        internal3inchBays.setText(internal3inchBaysString);

        //Internal 3.2" bays Of Case:
        TextView internal2inchBays = findViewById(R.id.caseInternal2InchBaysClickedValue);
        //Convert the value from the HashMap into a string so that it can be assigned into the TextView
        String internal2inchBaysString = Long.toString((Long) caseHashMapData.get("internal-2-bays"));
        internal2inchBays.setText(internal2inchBaysString);

        //Volume of case:
        ArrayList<String> volumeOfCase = (ArrayList<String>) caseHashMapData.get("volume");
        TextView volumeOfCaseView = findViewById(R.id.caseVolumeClickedValue);
        //Loop through the arrayList and assign the appropriate length values
        //Since the size will be dynamic for each part it would make more sense to use a loop
        //rather than assigning all the data individually
        String vo = "";
        for (int i = 0; i < volumeOfCase.size(); i++) {
            vo += "• " + volumeOfCase.get(i) + "\n";
        }
        volumeOfCaseView.setText(vo);
        Log.d(TAG, "onCreate: " + vo);

        //External Review of case:
        TextView externalReviewOfCase = findViewById(R.id.caseExternalReviewClickedValue);
        externalReviewOfCase.setText((CharSequence) caseHashMapData.get("external-review"));

        //Add case Details to CreateComputerListActivity if add button is clicked:
        Button addCaseToListBtn = findViewById(R.id.addClickedCaseToListBtn);
        addCaseToListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pass the data back to the CreateComputerListActivity by storing the data in the DataStorage
                //hashmap and then returning to the CreateComputerListActivity:

                String caseName = (String) caseHashMapData.get("name");
                Double casePrice = (Double) caseHashMapData.get("price");
                String casePriceString = Double.toString(casePrice);

                Intent passCaseDataToCreateComputerActivity = new Intent
                        (ViewCaseDetails.this, CreateComputerListActivity.class);

                caseData.getComputerList().put("CASE NAME", caseName);
                caseData.getComputerList().put("CASE PRICE", casePriceString);
                Log.d(TAG, "onSuccess: " + caseData.getComputerList().get("CASE NAME"));
                Log.d(TAG, "onSuccess: " + caseData.getComputerList().get("CASE PRICE"));


                startActivity(passCaseDataToCreateComputerActivity);

            }
        });


    }
}