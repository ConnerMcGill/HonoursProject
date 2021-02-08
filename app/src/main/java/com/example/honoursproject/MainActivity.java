/*
Honours Project - PC part Builder
File: MainActivity Class
Author: Conner McGill - B00320975
Date: 2020/08/18

Summary of file:
Currently this is just a temp class setup to make sure that I can logout a user
properly. I will update this in due course

 */


package com.example.honoursproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Need to add onclick to card view to open other activity as it crashes the app

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                logoutUserFromAccount();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logoutUserFromAccount() {

        mAuth.signOut();
        returnToLoginActivity();

    }

    private void returnToLoginActivity() {

        Toast.makeText(getApplicationContext(), "Logging out!",
                Toast.LENGTH_SHORT).show();

        Intent returnToLoginIntent = new Intent(MainActivity.this,
                LoginAccountActivity.class);
        startActivity(returnToLoginIntent);
    }

    private void openGuideLandingPageActivity() {
        Intent openGuideLandingPage = new Intent(MainActivity.this,
                GuideLandingPageActivity.class);
        startActivity(openGuideLandingPage);
    }

}