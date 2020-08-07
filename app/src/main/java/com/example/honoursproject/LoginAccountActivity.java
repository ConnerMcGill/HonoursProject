/*
Honours Project - PC part Builder
File: LoginAccountActivity Class
Author: Conner McGill - B00320975
Date: 2020/08/07

Summary of file:

    This file contains code which allows a user to login to the main activity of the app.
    Functions in the class allows a user to login to their account with their email and
    password. This is validated on the client. If the values are valid the details are validated
    and authenticated through Firebase. The user can also sign up for an account by clicking on the
    text-line that will take the user to a new activity to register for an account. The user
    can also go to a new activity to request for a new password for their account if they have
    forgotten their password.

 */

package com.example.honoursproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginAccountActivity extends AppCompatActivity {

    //Create variables for user interface elements
    private TextView registerForAccountTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialise user interface elements
        registerForAccountTextView = findViewById(R.id.text_register_redirect);

        /*Create on click listener for text-view which will allow the user to click the text view
        and go to the register form activity. The activity will be opened by calling a function
        which calls an intent to start the AccountRegisterActivity. */
        registerForAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAccountRegisterActivity();
            }
        });

    }

    /*This function is called when a user clicks the registerForAccountTextView. This function
    sets up an intent which starts the AccountRegisterActivity class. */
    public void openAccountRegisterActivity() {
        Intent openAccountRegisterActivityIntent = new Intent
                (this, AccountRegisterActivity.class);
        startActivity(openAccountRegisterActivityIntent);

    }

}