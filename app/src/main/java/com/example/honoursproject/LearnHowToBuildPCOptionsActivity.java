/*
Honours Project - PC part Builder
File: LearnHowToBuildPCOptionsActivity Class
Author: Conner McGill - B00320975
Date: 2021/02/09

Summary of file:
This class is similar to the LearnMoreAboutPartsOptionsActivity class in which this class acts
as a landing page with multiple options to different guides on teaching the user on how to
build a PC step by step

This class is a subclass of the GuidesLandingPageActivity class which is a landing page
for the rest of the guides within the app. It's a subclass due to how similar it is to the
GuidesLandingPage class considering it just leads to other guides.
It ends up reusing code from the GuidesLandingPage which inherits from the MainActivity
that deals with letting the user logout
 */
package com.example.honoursproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LearnHowToBuildPCOptionsActivity extends GuidesLandingPageActivity implements View.OnClickListener {

    //Tag used for debugging the firestore data retrieval
    private static final String TAG = "LearnHowToBuildPCOptionsTAG";

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
        setContentView(R.layout.activity_learn_how_to_build_p_c_options);

        //References to UI elements in the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Learn how to build a PC");

        //References to the buttons within the activity
        final Button prepStageOnePCBuildTools = findViewById(R.id.prep_stage_one_pc_build_tools);
        prepStageOnePCBuildTools.setOnClickListener(this); //Call the onClick method below

        final Button prepStageTwoPCCases = findViewById(R.id.prep_stage_two_pc_cases);
        prepStageTwoPCCases.setOnClickListener(this); //Call the onClick method below

        final Button prepStageThreePCParts = findViewById(R.id.prep_stage_three_pc_parts);
        prepStageThreePCParts.setOnClickListener(this); //Call the onClick method below

        final Button stepOneInstallingTheCPU = findViewById(R.id.step_one_install_cpu);
        stepOneInstallingTheCPU.setOnClickListener(this); //Call the onClick method below

        final Button stepTwoOptionalInstallingM2Drive = findViewById(R.id.step_two_optional_install_m2_drive);
        stepTwoOptionalInstallingM2Drive.setOnClickListener(this); //Call the onClick method below

        final Button stepThreeInstallCPUCooling = findViewById(R.id.step_three_install_cpu_cooling);
        stepThreeInstallCPUCooling.setOnClickListener(this); //Call the onClick method below

        final Button stepFourInstallMemory = findViewById(R.id.step_four_install_memory);
        stepFourInstallMemory.setOnClickListener(this); //Call the onClick method below

        final Button stepFiveOptionalTestOutsideCase = findViewById(R.id.step_five_optional_test_run_outside_case);
        stepFiveOptionalTestOutsideCase.setOnClickListener(this); //Call the onClick method below

        final Button stepSixMountPowerSupply = findViewById(R.id.step_six_mount_power_supply);
        stepSixMountPowerSupply.setOnClickListener(this); //Call the onClick method below

        final Button stepSevenInstallMotherboard = findViewById(R.id.step_seven_install_motherboard);
        stepSevenInstallMotherboard.setOnClickListener(this); //Call the onClick method below

        final Button stepEightInstallGPU = findViewById(R.id.step_eight_install_gpu);
        stepEightInstallGPU.setOnClickListener(this); //Call the onClick method below

        final Button stepNineInstallStorage = findViewById(R.id.step_nine_install_storage);
        stepNineInstallStorage.setOnClickListener(this); //Call the onClick method below

        final Button stepTenInstallOperatingSystem = findViewById(R.id.step_ten_install_os);
        stepTenInstallOperatingSystem.setOnClickListener(this); //Call the onClick method below

        final Button stepElevenLearnMore = findViewById(R.id.step_eleven_learn_more);
        stepElevenLearnMore.setOnClickListener(this); //Call the onClick method below

    }

    //OnClickListener for multiple buttons in the guide page gets the text for the button pressed
    //and then call the getPartGuideData() method

    //https://stackoverflow.com/questions/5620772/get-text-from-pressed-button
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prep_stage_one_pc_build_tools:
            case R.id.prep_stage_two_pc_cases:
            case R.id.prep_stage_three_pc_parts:
            case R.id.step_one_install_cpu:
            case R.id.step_two_optional_install_m2_drive:
            case R.id.step_three_install_cpu_cooling:
            case R.id.step_four_install_memory:
            case R.id.step_five_optional_test_run_outside_case:
            case R.id.step_six_mount_power_supply:
            case R.id.step_seven_install_motherboard:
            case R.id.step_eight_install_gpu:
            case R.id.step_nine_install_storage:
            case R.id.step_ten_install_os:
            case R.id.step_eleven_learn_more:
                Button btn = (Button) view;
                buttonText = btn.getText().toString();
                //testing that I can get the data for the button
                Log.d("my learn about building btn text", buttonText);
                getLearnBuildStageGuideData();
                break;
            default:
                Log.d("my switch statement failed", "Something went wrong with the switch statement");
                Toast.makeText(LearnHowToBuildPCOptionsActivity.this, "There has been an error with selecting a button...", Toast.LENGTH_SHORT).show();
        }
    }

    private void getLearnBuildStageGuideData() {
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
                            Toast.makeText(LearnHowToBuildPCOptionsActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LearnHowToBuildPCOptionsActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    private void openGuideActivity() {
        Intent openGuideActivity = new Intent(LearnHowToBuildPCOptionsActivity.this,
                ViewGuideActivity.class);
        openGuideActivity.putExtra("TITLE", title);
        openGuideActivity.putExtra("DESCRIPTION", description);
        openGuideActivity.putExtra("IMAGE NAME", buttonText);
        startActivity(openGuideActivity);
    }

}