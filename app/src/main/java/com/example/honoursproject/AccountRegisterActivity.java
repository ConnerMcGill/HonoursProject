/*
Honours Project - PC part Builder
File: AccountRegisterActivity Class
Author: Conner McGill - B00320975
Date: 2020/08/11

Summary of file:

    This file contains code which allows a user to register for an account. The user must enter a
    valid email address and password. Once the users details are valid their details will be
    authenticated and stored on Firebase. The user will then be taken to the MainActivity of the
    app.

 */

package com.example.honoursproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class AccountRegisterActivity extends AppCompatActivity {


    //Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;

    //Declare instances of user interface elements
    private TextInputLayout emailRegisterText;
    private TextInputLayout passwordRegisterText;
    private Button registerForAccountButton;
    //Text view that will be used to allow user to switch to LoginAccountActivity
    private TextView alreadyHaveAnAccountTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_register);

        //Initialise user interface elements
        emailRegisterText = findViewById(R.id.text_register_enter_email);
        passwordRegisterText = findViewById(R.id.text_register_enter_password);
        alreadyHaveAnAccountTextView = findViewById(R.id.text_login_redirect);
        registerForAccountButton = findViewById(R.id.button_register);

        //Initialise Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();

        //Call register new user function when sign up button is pressed
        registerForAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewUser();
            }
        });
    }




    //Validate the users email address







    //Validate the users password


    private void registerNewUser() {

        //Store the email and password that was entered into the EditText fields
        String email, password;

        //email = emailRegisterText.getText().toString();
        //password = passwordRegisterText.getText().toString();

        //You would call the validation functions that check the email and password are fine
        //return if they're not
        //Then run the firebase auth thing and login

        //Then somewhere at the end of the code just have the intent for the textview that
        //switches to the login activity
    }


}