/*
Honours Project - PC part Builder
File: LearnMoreAboutPartsOptions Class
Author: Conner McGill - B00320975
Date: 2021/02/09

Summary of file:
This class is the landing page as an option to take the users to specific guides about individual
components. This class is a subclass of the GuidesLandingPageActivity class which is a landing page
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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LearnMoreAboutPartsOptionsActivity extends GuidesLandingPageActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_more_about_parts_options);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Learn about different parts");

        final Button learnAboutTheCpu = findViewById(R.id.learn_more_about_the_cpu_btn);

        learnAboutTheCpu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                learnAboutTheCpuData();
                //openGuideActivity();
            }
        });


    }

    private void learnAboutTheCpuData() {
        //Create reference to the cpu guide document in the guides collection
        String documentName = "Learn about the CPU";
        DocumentReference guidesRef = db.collection("guides").document(documentName);

        //For the future I need to see if I can get some name or tag from the button itself to act
        //as the document name to make the code cleaner and have one function instead of having
        //tons of little functions trying to gather all the data

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

                            //Testing that firestore can get the data which it seems to be able to
                            Log.d("my title", title);
                            Log.d("my description", description);

                            openGuideActivity();

                        } else {
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

    private void openGuideActivity() {
        Intent openGuideActivity = new Intent(LearnMoreAboutPartsOptionsActivity.this,
                ViewGuideActivity.class);
        openGuideActivity.putExtra("TITLE", title);
        openGuideActivity.putExtra("DESCRIPTION", description);
        startActivity(openGuideActivity);
    }
}