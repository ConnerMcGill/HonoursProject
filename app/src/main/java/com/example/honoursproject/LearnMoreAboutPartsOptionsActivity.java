/*
Honours Project - PC part Builder
File: LearnMoreAboutPartsOptions Class
Author: Conner McGill - B00320975
Date: 2021/02/09

Summary of file:
This class is the landing page as an option to take the users to specific guides about individual
components. When a user clicks on a button to view the guide they would like to view they are taken
to a new activity with the data being retrieved first before being passed to the new activity
through an intent

This class is a subclass of the GuidesLandingPageActivity class which is a landing page
for the rest of the guides within the app. It's a subclass due to how similar it is to the
GuidesLandingPage class considering it just leads to other guides.
It ends up reusing code from the GuidesLandingPage which inherits from the MainActivity
that deals with letting the user logout
*/

package com.example.honoursproject;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LearnMoreAboutPartsOptionsActivity extends GuidesLandingPageActivity implements View.OnClickListener {

    //Tag used for debugging the firestore data retrieval
    private static final String TAG = "LearnMoreAboutPartsOptionsTAG";

    //Initialise Firestore instance
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //These keys are used to store the data retrieved from the document before being passed
    //to the variables below
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";

    //Variables that are used store the retrieved data and pass it through an intent to the
    //ViewGuideActivity
    private String title;
    private String description;

    //This variable stores the string in the button that is used to retrieve the document name in
    //the firestore database
    private String buttonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_more_about_parts_options);

        //References to UI elements in the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Learn about different parts");

        //References to the buttons within the activity
        final Button learnMoreAboutTheCPU = findViewById(R.id.learn_more_about_the_cpu_btn);
        learnMoreAboutTheCPU.setOnClickListener(this); //Call the onClick method below

        final Button learnMoreAboutTheCPUCooler = findViewById(R.id.learn_more_about_the_cpu_cooler_btn);
        learnMoreAboutTheCPUCooler.setOnClickListener(this); //Call the onClick method below

        final Button learnMoreAboutTheMotherboard = findViewById(R.id.learn_more_about_the_motherboard_btn);
        learnMoreAboutTheMotherboard.setOnClickListener(this); //Call the onClick method below

        final Button learnMoreAboutTheMemory = findViewById(R.id.learn_more_about_the_memory_btn);
        learnMoreAboutTheMemory.setOnClickListener(this); //Call the onClick method below

        final Button learnMoreAboutTheStorage = findViewById(R.id.learn_more_about_the_storage_btn);
        learnMoreAboutTheStorage.setOnClickListener(this); //Call the onClick method below

        final Button learnMoreAboutTheGPU = findViewById(R.id.learn_more_about_the_video_card_btn);
        learnMoreAboutTheGPU.setOnClickListener(this); //Call the onClick method below

        final Button learnMoreAboutTheCase = findViewById(R.id.learn_more_about_the_case_btn);
        learnMoreAboutTheCase.setOnClickListener(this); //Call the onClick method below

        final Button learnMoreAboutThePowerSupply = findViewById(R.id.learn_more_about_the_power_supply_btn);
        learnMoreAboutThePowerSupply.setOnClickListener(this); //Call the onClick method below

    }

    //OnClickListener for multiple buttons in the guide page gets the text for the button pressed
    //and then call the getPartGuideData() method

    //https://stackoverflow.com/questions/5620772/get-text-from-pressed-button
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.learn_more_about_the_cpu_btn:
            case R.id.learn_more_about_the_cpu_cooler_btn:
            case R.id.learn_more_about_the_motherboard_btn:
            case R.id.learn_more_about_the_memory_btn:
            case R.id.learn_more_about_the_storage_btn:
            case R.id.learn_more_about_the_video_card_btn:
            case R.id.learn_more_about_the_case_btn:
            case R.id.learn_more_about_the_power_supply_btn:
                Button btn = (Button) view;
                buttonText = btn.getText().toString();
                //testing that I can get the data for the button
                Log.d("my learn about parts btn text", buttonText);
                getPartGuideData();
                break;
            default:
                Log.d("my switch statement failed", "Something went wrong with the switch statement");
                Toast.makeText(LearnMoreAboutPartsOptionsActivity.this, "There has been an error with selecting a button...", Toast.LENGTH_SHORT).show();
        }
    }

    //Overriding the inflater menu with a shortcut back to the MainActivity class here
    //Create the inflater menu (three dots) in the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    //Add an option in the inflater menu (three dots) to let the user logout of their account by
    //calling the logoutUserFromAccount method or return to the MainActivity(Homepage) as a shortcut
    //for the guides screen
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                logoutUserFromAccount();
                return true;
            case R.id.item2:
                returnToMainActivity();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Return to the main activity shortcuts intent option in the inflater menu for the guides
    private void returnToMainActivity() {
        Toast.makeText(getApplicationContext(), "Returning to Home Page!",
                Toast.LENGTH_SHORT).show();

        Intent returnToMainActivityIntent = new Intent(LearnMoreAboutPartsOptionsActivity.this,
                MainActivity.class);
        startActivity(returnToMainActivityIntent);
    }

    private void getPartGuideData() {
        //Create reference for retrieving the guide document from the guides collection
        String documentName = buttonText;
        DocumentReference guidesRef = db.collection("guides").document(documentName);

        guidesRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        //The documentSnapshot contains all the data we need as long as the
                        //document we are looking for exists

                        //Check that the document exists:
                        if (documentSnapshot.exists()) {
                            title = documentSnapshot.getString(KEY_TITLE);
                            description = documentSnapshot.getString(KEY_DESCRIPTION);

                            //As firestore console does not recognise the \n regex command I have
                            //to manually change a string and add in a new line manually...
                            description = description.replace("newline", "\n\n");

                            //Testing that firestore can get the data
                            Log.d("my title", title);
                            Log.d("my description", description);

                            //Call the method below
                            openGuideActivity();

                        } else {
                            //Display toast as error message if the document can't be retrieved
                            Toast.makeText(LearnMoreAboutPartsOptionsActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LearnMoreAboutPartsOptionsActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    //Pass the relevant data into the new activity as an intent as well as opening the ViewGuideActivity
    private void openGuideActivity() {
        Intent openGuideActivity = new Intent(LearnMoreAboutPartsOptionsActivity.this,
                ViewGuideActivity.class);
        openGuideActivity.putExtra("TITLE", title);
        openGuideActivity.putExtra("DESCRIPTION", description);
        openGuideActivity.putExtra("IMAGE NAME", buttonText);
        startActivity(openGuideActivity);
    }


}