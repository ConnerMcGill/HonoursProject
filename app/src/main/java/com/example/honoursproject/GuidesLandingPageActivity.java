/*
Honours Project - PC part Builder
File: GuideLandingPage Class
Author: Conner McGill - B00320975
Date: 2021/02/09

Summary of file:
This class is the landing page as an option to take the users to the rest of the guides within
the app. This class is a subclass of the MainActivity class which is a landing page to the
rest of the options within the app. It's a subclass due to how similar it is to the MainActivity
class considering it just leads to other menus. It ends up reusing code from the MainActivity
that deals with letting the user logout

*/


package com.example.honoursproject;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class GuidesLandingPageActivity extends MainActivity implements View.OnClickListener {

    //Initialise UI elements
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guides_landing_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Guides Landing Page");

        CardView learnMoreAboutPartsCard = findViewById(R.id.learn_more_about_parts);
        learnMoreAboutPartsCard.setOnClickListener(this);

        CardView learnHowToBuildAPC = findViewById(R.id.learn_how_to_build_pc);
        learnHowToBuildAPC.setOnClickListener(this);

    }

    //Take user to respective activity for the button they click
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.learn_more_about_parts:
                openLearnMoreAboutPartsGuides();
                break;
            case R.id.learn_how_to_build_pc:
                openLearnHowToBuildAPCGuides();
                break;
            default:
                Log.d("my switch statement failed", "Something went wrong with the switch statement");
                Toast.makeText(GuidesLandingPageActivity.this, "There has been an error with selecting a button...", Toast.LENGTH_SHORT).show();
        }
    }

    //Open the learn more about parts guides activity
    private void openLearnMoreAboutPartsGuides() {
        Intent openLearnMoreAboutPartsGuideLandingPage = new Intent(GuidesLandingPageActivity.this,
                LearnMoreAboutPartsOptionsActivity.class);
        startActivity(openLearnMoreAboutPartsGuideLandingPage);
    }

    //Open the learn how to build a pc guides activity
    private void openLearnHowToBuildAPCGuides() {
        Intent openLearnHowToBuildPCGuideLandingPage = new Intent(GuidesLandingPageActivity.this,
                LearnHowToBuildPCOptionsActivity.class);
        startActivity(openLearnHowToBuildPCGuideLandingPage);
    }

}