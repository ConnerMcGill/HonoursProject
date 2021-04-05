/*
Honours Project - PC part Builder
File: ViewGuideActivity Class
Author: Conner McGill - B00320975
Date: 2021/02/10

Summary of file:
This class receives the variables from the different guides classes which are passed through as intents
The data is then assigned to their respective elements and an image is retrieved from the firestore
storage and assigned to the ImageView element
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

public class ViewGuideActivity extends GuidesLandingPageActivity {

    //Variable used to get the name of the image for the guide which will be named after the title of the guide
    String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_guide);

        TextView titleForGuide = findViewById(R.id.titleForGuide);
        TextView descriptionForGuide = findViewById(R.id.textForGuideDescription);

        //Get the variables data from the previous activity guide and insert them into the
        //appropriate elements
        imageName = getIntent().getStringExtra("IMAGE NAME");
        Log.d("my image name", imageName);
        titleForGuide.setText(getIntent().getStringExtra("TITLE"));
        descriptionForGuide.setText(getIntent().getStringExtra("DESCRIPTION"));

        //Setup references to UI elements within the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("TITLE"));

        //Go back to the previous activity in the activity backstack
        //https://stackoverflow.com/questions/49350686/back-to-previous-activity-arrow-button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                finish();
            }
        });

        //In order to get the image for the guide a storage reference needs to be created
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(imageName + ".jpg");
        Log.d("my image", String.valueOf(storageReference));

        try {
            //Create a placeholder that will store the image for the activity
            final File tempFile = File.createTempFile(imageName, "jpg");
            //Try to retrieve the image from the firestore cloud storage
            storageReference.getFile(tempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Temp toast message for testing
                            //Toast.makeText(ViewGuideActivity.this, "Image retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                            ((ImageView) findViewById(R.id.imageForGuide)).setImageBitmap(bitmap);
                            Log.d("image retrieved", "The image has been retrieved successfully. Which makes sense if you can actually see it");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //If the image can't be retrieved for whatever reason then display the toast as an error message and write a log statement to the console
                    Toast.makeText(ViewGuideActivity.this, "Image failed to be retrieved", Toast.LENGTH_SHORT).show();
                    Log.d("image not retrieved", "The image has failed to be retrieved successfully. Which makes sense as you can't see the image");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
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

    //Return to the main activity shortcuts intent option in the inflater menu for the guidse
    private void returnToMainActivity() {
        Toast.makeText(getApplicationContext(), "Returning to Home Page!",
                Toast.LENGTH_SHORT).show();

        Intent returnToMainActivityIntent = new Intent(ViewGuideActivity.this,
                MainActivity.class);
        startActivity(returnToMainActivityIntent);
    }
}