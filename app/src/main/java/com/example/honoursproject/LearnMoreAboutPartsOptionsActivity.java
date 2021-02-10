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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LearnMoreAboutPartsOptionsActivity extends GuidesLandingPageActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_more_about_parts_options);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Learn about different parts");

        Button learnAboutTheCpu = findViewById(R.id.learn_more_about_the_cpu_btn);

        learnAboutTheCpu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGuideActivity();
            }
        });


    }

    private void openGuideActivity() {
        Intent openGuideActivity = new Intent(LearnMoreAboutPartsOptionsActivity.this,
                ViewGuideActivity.class);
        startActivity(openGuideActivity);
    }
}