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

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button logoutButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoutButton = findViewById(R.id.button_logout);

        mAuth = FirebaseAuth.getInstance();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUserFromAccount();
            }
        });

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

}